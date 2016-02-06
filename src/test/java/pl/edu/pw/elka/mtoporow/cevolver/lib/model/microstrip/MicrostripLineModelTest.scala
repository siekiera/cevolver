package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.scalatest.FunSuite
import pl.edu.pw.elka.mtoporow.cevolver.TestDataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.cli.CevolverApp
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt.MicrostripLineModelAlt

/**
 * Test linii mikropaskowej
 * Data utworzenia: 20.11.15, 14:59
 * @author Michał Toporowski
 */
class MicrostripLineModelTest extends FunSuite {

  test("Microstrip line model response calculation test") {
    testResponse()
  }

  /**
   * Wczytujemy dane i porównujemy odpowiedź obliczoną dla nich z odpowiedzią obliczoną przez program zewnętrzny
   */
  private def testResponse() = {
    val dists = TestDataHolder.dists
    val externallyCalculatedResponse = TestDataHolder.externallyCalculatedResponse
    val result = CevolverApp.getExpectedResult(dists)
    val calculatedResponse = result.response(DataHolder.getCurrent.measurementParams)
    println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)
    println("Odpowiedź obliczona przez program zewnętrzny: " + externallyCalculatedResponse)
    assert(calculatedResponse.value.equals(externallyCalculatedResponse.value))
  }

  test("Microstrip line model alternative response calculation test") {
    val dists = TestDataHolder.dists
    val externallyCalculatedResponse = TestDataHolder.externallyCalculatedResponse
    val result = new MicrostripLineModelAlt(dists, DataHolder.getCurrent.measurementParams.getMicrostripParams)
    val calculatedResponse = result.response(DataHolder.getCurrent.measurementParams)
    println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)
    println("Odpowiedź obliczona przez program zewnętrzny: " + externallyCalculatedResponse)
    assert(calculatedResponse.value.equals(externallyCalculatedResponse.value))
  }

}
