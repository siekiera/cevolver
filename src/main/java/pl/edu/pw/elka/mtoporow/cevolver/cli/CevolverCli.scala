package pl.edu.pw.elka.mtoporow.cevolver.cli

import java.io.{OutputStreamWriter, File}
import java.util.Properties

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionResult
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.VerboseLevel
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.{Conversions, PropertiesUtil, TimeUtil}
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.{ConsoleRowWriter, CellArray, Exporter}
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver
import pl.edu.pw.elka.mtoporow.cevolver.ext.chart.ChartData
import pl.edu.pw.elka.mtoporow.cevolver.ext.diag.{PopulationStatistics, FitnessProbe}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps
import pl.edu.pw.elka.mtoporow.cevolver.util.GeneralConstants
import pl.edu.pw.elka.mtoporow.cevolver.util.export.SerializationUtil

import scala.io.Source

/**
 * Interfejs wiersza polecenia dla aplikacji
 * Data utworzenia: 06.02.16, 11:31
 * @author Michał Toporowski
 */
object CevolverCli {

  private val properties = new Properties()
  private var lastResult: EvolutionResult = null
  private var lastSolver: Solver = null

  def main(args: Array[String]) {
    loadDefaultProperties()
    about()
    help()
    print(": ")
    for (ln <- Source.stdin.getLines()) {
      try {
        val input = ln.trim.split(" ")
        input match {
          case Array("") =>
          case Array("q") =>
            println("Do widzenia!")
            return
          case Array("about") => about()
          case Array("help") => help()
          case Array("load", dataset@_) => load(dataset)
          case Array("set", key@_, value@_) => setProperty(key, value)
          case Array("remove", key@_) => removeProperty(key)
          case Array("props") => printAllProperties()
          case Array("mparams") => printMeasurementParams()
          case Array("datasets") => printDataSets()
          case Array("pop", count@_) => printLastPopulation(getInt(count))
          case Array("store") => store()
          case Array("fprobe") => fitnessProbe()
          case Array("fpcsv") => fitnessProbeCsv()
          case Array("run") => run(VerboseLevel.allOff())
          case Array("run", verbose@_) => run(VerboseLevel(verbose))
          case Array("stats") => popStats()
          case Array("continue") => continue()
          case Array("multi", n@_) => runMultipleTimes(getInt(n), VerboseLevel.allOff())
          case Array("multi", n@_, verbose@_) => runMultipleTimes(getInt(n), VerboseLevel(verbose))
          case _ => println("Nieznane polecenie!")
        }
      } catch {
        case e: Exception => println("Błąd: " + e.getMessage)
      }
      print(": ")
    }
  }

  /**
   * Ładuje zestaw danych
   * @param datasetId identyfikator zestawu danych
   */
  private def load(datasetId: String): Unit = {
    try {
      println("Ładowanie... " + datasetId)
      DataHolder.load(datasetId)
      lastSolver = null
      printf("Załadowano zbiór danych: %s\n", datasetId)
    } catch {
      case e: Exception => println("Nie można załadować zbioru danych: " + e.getMessage)
    }
  }

  /**
   * Uruchamia obliczenia
   */
  private def run(verboseLevel: VerboseLevel): Unit = {
    if (!DataHolder.isLoaded) {
      println("Nie załadowano danych!")
      return
    }
    try {
      printAllProperties()
      val algParameters = new AlgorithmPropertiesReader(properties).getParameters
      val expectedDists = DataHolder.getCurrent.expectedDistances
      printMeasurementParams()
      println("Rozpoczynam obliczenia...")
      lastSolver = new Solver()
      val result = lastSolver.solveWithAllResults(algParameters, verboseLevel)
      printf("Zakończono obliczenia po %s pokoleniach, czas obliczeń: %s\n", result.generationCount, result.durationSec)
      println("Oczekiwany wynik: " + expectedDists.toStringMM)
      println("Najlepszy wynik z populacji: ")
      CevolverApp.printAllResults(result.population, 1)
      lastResult = result
    } catch {
      case e: Exception => println("Błąd podczas uruchamiania algorytmu: " + e.getMessage)
    }
  }

  /**
   * Kontynuuje obliczenia w ostatnio zakończonym miejscu
   */
  private def continue(): Unit = {
    if (lastSolver == null) {
      println("Nie ma rozpoczętej ewolucji do kontynuowania. Użyj komendy run")
      return
    }
    val expectedDists = DataHolder.getCurrent.expectedDistances
    println("Kontynuuję obliczenia...")
    val result = lastSolver.continue()
    printf("Zakończono obliczenia po %s pokoleniach, czas obliczeń: %s\n", result.generationCount, result.durationSec)
    println("Oczekiwany wynik: " + expectedDists.toStringMM)
    println("Najlepszy wynik z populacji: ")
    CevolverApp.printAllResults(result.population, 1)
    lastResult = result
  }

  /**
   * Uruchamia algorytm wiele razy zapisując ślad funkcji celu oraz średnią
   *
   * @param n liczba iteracji
   * @param verboseLevel poziom logowania
   */
  private def runMultipleTimes(n: Int, verboseLevel: VerboseLevel): Unit = {
    val results = new Array[Array[Double]](n)
    val times = new Array[Double](n)
    val avgFitnessVals = new Array[Double](n)
    val bestAvgFitnessVals = new Array[Double](n)
    val bestPercentage = 10.0
    for (i <- 0 until n) {
      // Uruchamiamy
      run(verboseLevel)
      // Zapamiętujemy wynik
      results(i) = JavaVectorOps.toDoubleArray(lastResult.fitnessTrace)
      times(i) = lastResult.durationSec
      val population = Conversions.javaToScalaList(lastResult.population)
      avgFitnessVals(i) = population.map(_.getFitness).sum / population.size
      val bestCount = (bestPercentage * population.size / 100.0).toInt
      bestAvgFitnessVals(i) = population.slice(0, bestCount).map(_.getFitness).sum / bestCount
    }
    // Zapisujemy wynik
    val timePart = TimeUtil.nowAsNoSepString()
    val dir = GeneralConstants.subdir(timePart)
    // Ślad funkcji celu
    Exporter.serialize(new File(dir, "funkcja_celu.csv"), results)
    // Ślad funkcji celu jako chartData
    val chartFile = new File(dir, "funkcja_celu.cht")
    val chartData: ChartData = new ChartData("Wykres wartości funkcji celu", "Nr pokolenia", null)
    for (i <- 0 until n) {
      chartData.addSeries("Przebieg: " + i, results(i))
    }
    SerializationUtil.serialize(chartFile, chartData)
    // Średnia wartość f. celu
    val avg = JavaVectorOps.avg(JavaVectorOps.createMatrix(results))
    Exporter.serialize(new File(dir, "średnia.csv"), avg)
    val avgChartData = new ChartData("Wykres średnich wartości f. celu", "Nr pokolenia", null)
    avgChartData.addSeries("Wartość średnia f. celu", avg)
    SerializationUtil.serialize(new File(dir, "średnia.cht"), avgChartData)
    // Parametry algorytmu
    PropertiesUtil.storeToFile(properties, new File(dir, "algorithm.properties"), "Zestaw danych: " + DataHolder.getCurrentId)
    // Wynikowa populacja ostatniego
    val population = Conversions.javaToScalaList(lastResult.population).map(c => c.getCandidate.distances.distances.toArray).toArray
    Exporter.serialize(new File(dir, "populacja.csv"), population)
    // Czasy wykonania
    Exporter.serialize(new File(dir, "czasy.csv"), Array(times))
    // Uśredniona wartość FC wszystkich i bestPercentage% najlepszych
    Exporter.serialize(new File(dir, "fc.csv"), Array(avgFitnessVals, bestAvgFitnessVals))
  }

  /**
   * Ładuje domyślne parametry algorytmu
   */
  private def loadDefaultProperties(): Unit = {
    properties.load(getClass.getClassLoader.getResourceAsStream("algorithm.properties"))
  }

  /**
   * Ustawia właściwość
   *
   * @param key klucz
   * @param value wartość
   */
  private def setProperty(key: String, value: String): Unit = {
    properties.setProperty(key, value)
  }

  /**
   * Listuje wszystkie właściwości algorytmu
   */
  private def printAllProperties(): Unit = {
    println("Parametry algorytmu: ")
    println(PropertiesUtil.storeToString(properties))
  }

  /**
   * Listuje parametry pomiaru
   */
  private def printMeasurementParams(): Unit = {
    println(DataHolder.getCurrent.measurementParams.toString)
  }

  /**
   * Usuwa parametr algorytmu
   *
   * @param key klucz
   */
  private def removeProperty(key: String): Unit = {
    properties.remove(key)
  }

  /**
   * Wypisuje dostępne zbiory danych
   */
  private def printDataSets(): Unit = {
    println("Dostępne zbiory danych:")
    println(Conversions.javaToScalaList(DataHolder.getAvailableDataSets).mkString(", "))
  }

  /**
   * Wypisuje populację z ostatniego eksperymentu
   * @param count liczba wyników do wypisana
   */
  private def printLastPopulation(count: Int): Unit = {
    if (lastResult == null) {
      println("Brak danych!")
    } else {
      CevolverApp.printAllResults(lastResult.population, count)
    }
  }

  /**
   * Zapisuje do plików parametry algorytmu i ślad funkcji celu
   */
  private def store(): Unit = {
    // Katalog: ${USER_HOME}/cevolver_out
    val dir = GeneralConstants.OUTPUT_DIR
    val timePart = TimeUtil.nowAsNoSepString()
    // Parametry algorytmu
    PropertiesUtil.storeToFile(properties, new File(dir, timePart + ".properties"), null)
    if (lastResult != null) {
      // Ślad funkcji celu
      Exporter.serialize(new File(dir, timePart + ".csv"), lastResult.fitnessTrace)
      // Ślad funkcji celu jako chartData
      val chartFile = new File(dir, timePart + ".cht")
      val chartData: ChartData = new ChartData("Wykres wartości funkcji celu", "Nr pokolenia", null)
      chartData.addSeries("Przebieg: " + timePart, lastResult.fitnessTrace)
      SerializationUtil.serialize(chartFile, chartData)
    }
  }

  /**
   * Sonduje wartość f. celu i zapisuje do pliku
   */
  private def fitnessProbe(): Unit = {
    if (!DataHolder.isLoaded) {
      println("Nie załadowano danych!")
      return
    }
    val chartData = FitnessProbe.asChartData()
    val file = new File(GeneralConstants.OUTPUT_DIR, DataHolder.getCurrentId + "_probe.cht")
    SerializationUtil.serialize(file, chartData)
  }

  private def popStats(): Unit = {
    val stats = new PopulationStatistics(lastResult)
    val writer = new ConsoleRowWriter
    CellArray.writeValues(stats.rowStatistics, writer)
    CellArray.writeValue(stats, writer)
    writer.finish()
  }

  /**
   * Sonduje wartość f. celu i zapisuje do pliku
   */
  private def fitnessProbeCsv(): Unit = {
    if (!DataHolder.isLoaded) {
      println("Nie załadowano danych!")
      return
    }
    val matrix = FitnessProbe.as2dMatrix()
    val file = new File(GeneralConstants.OUTPUT_DIR, DataHolder.getCurrentId + "_probe.csv")
    Exporter.serialize(file, matrix)
  }

  /**
   * Konwertuje string do integera
   *
   * @param string string
   * @return integer
   */
  private def getInt(string: String) = string.toInt

  /**
   * Wypisuje informacje o programie
   */
  private def about(): Unit = {
    println("Cevolver - narzędzie do obliczania odległości w kanałach transmisyjnych za pomocą algorytmów ewolucyjnych" +
      "\n Autor: Michał Toporowski")
  }

  /**
   * Wypisuje informację, jak używać CLI
   */
  private def help(): Unit = {
    println("Polecenia: " +
      "\n\thelp\t\tten komunikat" +
      "\n\tabout\t\tinformacje o programie" +
      "\n\tdatasets\twypisanie dostępnych zbiorów danych" +
      "\n\tload <id>\tzaładowanie zbioru danych" +
      "\n\tset <k> <v>\tustawienie wartości parametru algorytmu" +
      "\n\tremove <k>\tusunięcie parametru algorytmu" +
      "\n\tprops\t\twypisanie wszystkich parametrów algorytmu" +
      "\n\tmparams\t\twypisanie parametrów pomiaru" +
      "\n\tpop <n>\t\twypisanie n najlepszych osobników z ostatniej populacji" +
      "\n\trun [ndfr]\turuchomienie algorytmu, poziom logowania:" +
      "\n\t\tn - nr pokolenia" +
      "\n\t\td - odległości w najlepszym modelu" +
      "\n\t\tr - odpowiedź najlepszego modelu" +
      "\n\t\tf - wartość funkcji przystosowania najlepszego modelu" +
      "\n\tq\t\twyjście")
  }

}
