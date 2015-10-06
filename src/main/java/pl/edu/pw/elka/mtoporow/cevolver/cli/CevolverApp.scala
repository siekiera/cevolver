package pl.edu.pw.elka.mtoporow.cevolver.cli

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
    val algInputStream = getClass.getClassLoader.getResourceAsStream("algorithm.properties")
    val envInputStream = getClass.getClassLoader.getResourceAsStream("micro20.properties")
    val parameters = new AlgorithmPropertiesReader(algInputStream).getParameters
    // TODO:: docelowo może dobrze byłoby powiązać EnvProperties z .s2p
    val expectedDists = new EnvPropertiesReader(envInputStream).getExpectedDistances
    //    val data = new CsvDataProvider(getClass.getClassLoader.getResource("sampledata.csv")).provide
    val data = new TouchstoneDataProvider(getClass.getClassLoader.getResource("micro20.s2p")).provide
    println("Rozpoczynam obliczenia")
    val result = new Solver().solve(parameters, data)
    println("Zakończono obliczenia")
    println("wynik: " + result)
    println("oczekiwany wynik: " + getExpectedResult(expectedDists))
  }

  private def getExpectedResult(expectedDists: Distances) = {
    new MicrostripLineModel(expectedDists, MeasurementParams.getMicrostripParams)
  }
}
