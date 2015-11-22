package pl.edu.pw.elka.mtoporow.cevolver

import pl.edu.pw.elka.mtoporow.cevolver.cli.EnvPropertiesReader
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider

/**
 * Klasa ładująca i przechowująca dane dla testu
 * Data utworzenia: 20.11.15, 15:43
 * @author Michał Toporowski
 */
object TestDataHolder {
  val dists = new EnvPropertiesReader(getClass.getClassLoader.getResourceAsStream("micro20.properties")).getExpectedDistances
  val externallyCalculatedResponse = new TouchstoneDataProvider(getClass.getClassLoader.getResource("micro20.s2p")).provide
}
