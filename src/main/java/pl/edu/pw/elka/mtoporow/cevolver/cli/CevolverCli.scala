package pl.edu.pw.elka.mtoporow.cevolver.cli

import java.io.StringWriter
import java.util.Properties

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.VerboseLevel
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver

import scala.io.Source

/**
 * Interfejs wiersza polecenia dla aplikacji
 * Data utworzenia: 06.02.16, 11:31
 * @author Michał Toporowski
 */
object CevolverCli {

  private val properties = new Properties()

  def main(args: Array[String]) {
    loadDefaultProperties()
    about()
    help()
    print(": ")
    for (ln <- Source.stdin.getLines()) {
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
        case Array("run") => run(VerboseLevel.allOff())
        case Array("run", verbose@_) => run(VerboseLevel(verbose))
        case _ => println("Nieznane polecenie!")
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
    printAllProperties()
    val algParameters = new AlgorithmPropertiesReader(properties).getParameters
    val expectedDists = DataHolder.getCurrent.expectedDistances
    val data = DataHolder.getCurrent.canalResponse
    printMeasurementParams()
    println("Rozpoczynam obliczenia...")
    val results = new Solver().solveWithAllResults(algParameters, data, verboseLevel)
    println("Zakończono obliczenia")
    println("oczekiwany wynik: " + expectedDists.toStringMM)
    CevolverApp.printAllResults(results, 20)
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
    val sw = new StringWriter()
    properties.store(sw, null)
    println("Parametry algorytmu: ")
    println(sw.toString)
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
      "\n\trun [ndfr]\turuchomienie algorytmu, poziom logowania:" +
      "\n\t\tn - nr pokolenia" +
      "\n\t\td - odległości w najlepszym modelu" +
      "\n\t\tr - odpowiedź najlepszego modelu" +
      "\n\t\tf - wartość funkcji przystosowania najlepszego modelu" +
      "\n\tq\t\twyjście")
  }

}
