package pl.edu.pw.elka.mtoporow.cevolver.cli

import org.uncommons.watchmaker.framework.EvaluatedCandidate
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel

/**
 * Główna klasa aplikacji
 * Data utworzenia: 15.05.15, 13:55
 * @author Michał Toporowski
 */
object CevolverApp {


  def main(args: Array[String]) {
    val useFake = args.contains("--fake")

    // Ładujemy zestaw danych
    DataHolder.load("20")

    val algInputStream = getClass.getClassLoader.getResourceAsStream("algorithm.properties")
    val parameters = new AlgorithmPropertiesReader(algInputStream).getParameters

    val expectedDists = DataHolder.getCurrent.expectedDistances
    var data = DataHolder.getCurrent.canalResponse

    if (useFake) {
      // Dane "fałszywe" - liczone za pomocą tego kodu, a nie zewnętrznego programu
      data = getExpectedResult(expectedDists).response()
    }

    println("Rozpoczynam obliczenia")
    //    val result = new Solver().solve(parameters, data)
    val results = new Solver().solveWithAllResults(parameters, data)
    println("Zakończono obliczenia")
    //    println("wynik: " + result)
    println("oczekiwany wynik: " + getExpectedResult(expectedDists))
    printAllResults(results, 20)
  }

  def getExpectedResult(expectedDists: Distances) = {
    new MicrostripLineModel(expectedDists, DataHolder.getCurrent.measurementParams.getMicrostripParams)
  }

  def printAllResults(results: java.util.List[EvaluatedCandidate[EvolutionaryAlgorithm.C]], maxResults: Int): Unit = {
    println("Najlepsze wyniki")
    for (i <- 0 until results.size().min(maxResults)) {
      val ec = results.get(i)
      println(i.toString + ": Funkcja celu: " + ec.getFitness + "; c: " + ec.getCandidate.hashCode() + "; " + ec.getCandidate.distances.toStringMM)
    }
  }
}
