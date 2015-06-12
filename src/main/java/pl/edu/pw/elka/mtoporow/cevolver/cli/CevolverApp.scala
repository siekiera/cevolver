package pl.edu.pw.elka.mtoporow.cevolver.cli

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.data.CsvDataProvider
import pl.edu.pw.elka.mtoporow.cevolver.engine.Solver

/**
 * Klasa X
 * Data utworzenia: 15.05.15, 13:55
 * @author Michał Toporowski
 */
object CevolverApp {


  def main(args: Array[String]) {
    val inputStream = getClass.getClassLoader.getResourceAsStream("algorithm.properties")
    val parameters = new PropertiesReader(inputStream).getParameters
    val data = new CsvDataProvider(getClass.getClassLoader.getResource("sampledata.csv")).provide
    val result = new Solver().solve(parameters, data)
    println("result: " + result)
  }
}
