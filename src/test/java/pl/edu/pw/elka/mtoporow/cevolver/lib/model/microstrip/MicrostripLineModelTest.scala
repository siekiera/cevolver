package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.MatrixUtils
import org.scalatest.FunSuite
import pl.edu.pw.elka.mtoporow.cevolver.TestDataHolder
import pl.edu.pw.elka.mtoporow.cevolver.TestDataHolder._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.cli.CevolverApp
import pl.edu.pw.elka.mtoporow.cevolver.data.TouchstoneDataProvider
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt.MicrostripLineModelAlt
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

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
    val calculatedResponse = result.response()
    println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)
    println("Odpowiedź obliczona przez program zewnętrzny: " + externallyCalculatedResponse)
    assert(calculatedResponse.value.equals(externallyCalculatedResponse.value))
  }

  test("Microstrip line model alternative response calculation test") {
    val dists = TestDataHolder.dists
    val externallyCalculatedResponse = TestDataHolder.externallyCalculatedResponse
    val result = new MicrostripLineModelAlt(dists)
    val calculatedResponse = result.response()
    println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)
    println("Odpowiedź obliczona przez program zewnętrzny: " + externallyCalculatedResponse)
    assert(calculatedResponse.value.equals(externallyCalculatedResponse.value))
  }
/*
  test("Test pojedynczego fragmentu") {
    val extResponse = new TouchstoneDataProvider(getClass.getClassLoader.getResource("single.s2p")).provide
    val result = MeasurementParams.getFrequencies.toArray.map(f => {
      val params = MeasurementParams.getMicrostripParams
      new Microstrip(params.w, Units.MIL.toSI(50), params.t, params.h, params.epsr).tMatrix(f, MeasurementParams.getImpedance).toSMatrix.s11
    }).array
    val calculatedResponse = MatrixUtils.createFieldVector(result)
    println("Odpowiedź obliczona dla wczytanych danych: " + new CanalResponse(calculatedResponse))
    println("Odpowiedź obliczona przez program zewnętrzny: " + extResponse)
    assert(calculatedResponse.equals(extResponse.value))
  }
*/

  test("Dummy test for impedance") {
    val origZ0 = MeasurementParams.getImpedance
    for (z01 <- 1.0 to 200.0 by 1.0) {
      println("Impedancja: " + z01)
      MeasurementParams.setImpedance(Complex.valueOf(z01))
      val dists = TestDataHolder.dists
      val result = new MicrostripLineModelAlt(dists)
      val calculatedResponse = result.response()
      println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)

    }
    MeasurementParams.setImpedance(origZ0)
  }

}
