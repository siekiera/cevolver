package pl.edu.pw.elka.mtoporow.cevolver.cli

import java.io.{File, StringWriter}
import java.util.Properties

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionResult
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.VerboseLevel
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.{TimeUtil, PropertiesUtil, Conversions}
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Exporter
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver

import scala.concurrent.duration.Duration
import scala.io.Source

/**
 * Interfejs wiersza polecenia dla aplikacji
 * Data utworzenia: 06.02.16, 11:31
 * @author Michał Toporowski
 */
object CevolverCli {

  private val properties = new Properties()
  private var lastResult: EvolutionResult = null

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
          case Array("pop", count@_) => printLastPopulation(count)
          case Array("store") => store()
          case Array("run") => run(VerboseLevel.allOff())
          case Array("run", verbose@_) => run(VerboseLevel(verbose))
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
      val result = new Solver().solveWithAllResults(algParameters, verboseLevel)
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
  private def printLastPopulation(count: String): Unit = {
    if (lastResult == null) {
      println("Brak danych!")
    } else {
      CevolverApp.printAllResults(lastResult.population, count.toInt)
    }
  }

  /**
   * Zapisuje do plików parametry algorytmu i ślad funkcji celu
   */
  private def store(): Unit = {
    // Katalog: ${USER_HOME}/cevolver_out
    val home = System.getProperty("user.home")
    val dir = new File(home, "cevolver_out")
    dir.mkdirs()
    val timePart = TimeUtil.nowAsNoSepString()
    // Parametry algorytmu
    PropertiesUtil.storeToFile(properties, new File(dir, timePart + ".properties"), null)
    if (lastResult != null) {
      // Ślad funkcji celu
      Exporter.serialize(new File(dir, timePart + ".csv"), lastResult.fitnessTrace)
    }
  }

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
