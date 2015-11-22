package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import org.scalatest.FunSuite
import pl.edu.pw.elka.mtoporow.cevolver.TestDataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel

/**
 * Test funkcji oceny jakości
 * Data utworzenia: 20.11.15, 15:42
 * @author Michał Toporowski
 */
class FitnessFunctionTest extends FunSuite {

  test("Test funkcji celu - dla siebie samego 0") {
    val c = new MicrostripLineModel(TestDataHolder.dists, MeasurementParams.getMicrostripParams)
    val fitness = FitnessFunction.apply(c, c.response(), 0)
    assert(fitness == 0.0)
  }
  test("Test funkcji celu - dla dobrych wyników ma być 0") {
    val c = new MicrostripLineModel(TestDataHolder.dists, MeasurementParams.getMicrostripParams)
    val fitness = FitnessFunction.apply(c, TestDataHolder.externallyCalculatedResponse, 0)
    assert(fitness == 0.0)
  }
}
