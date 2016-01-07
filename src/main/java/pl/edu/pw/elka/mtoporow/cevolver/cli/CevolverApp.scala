package pl.edu.pw.elka.mtoporow.cevolver.cli

import org.uncommons.watchmaker.framework.EvaluatedCandidate
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider
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

    val algInputStream = getClass.getClassLoader.getResourceAsStream("algorithm.properties")
    val envInputStream = getClass.getClassLoader.getResourceAsStream("micro2_nboxp1.properties")
    val parameters = new AlgorithmPropertiesReader(algInputStream).getParameters
    // TODO:: docelowo może dobrze byłoby powiązać EnvProperties z .s2p
    val expectedDists = new EnvPropertiesReader(envInputStream).getExpectedDistances
    var data = new TouchstoneDataProvider(getClass.getClassLoader.getResource("micro2_nboxp1.s2p")).provide

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
    new MicrostripLineModel(expectedDists, MeasurementParams.getMicrostripParams)
  }

  private def printAllResults(results: java.util.List[EvaluatedCandidate[EvolutionaryAlgorithm.C]], maxResults: Int): Unit = {
    for (i <- 0 until results.size().min(maxResults)) {
      val ec = results.get(i)
      println(i.toString + ": Funkcja celu: " + ec.getFitness + "; c: " + ec.getCandidate.hashCode() + "; wynik: " + ec.getCandidate)
    }
  }
}
