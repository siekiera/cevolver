package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.scalatest.FunSuite
import pl.edu.pw.elka.mtoporow.cevolver.TestDataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt.MicrostripLineModelAlt
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, Distances}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Test linii mikropaskowej
 * Data utworzenia: 20.11.15, 14:59
 * @author Michał Toporowski
 */
class MicrostripLineModelTest extends FunSuite {

  test("Microstrip line model response calculation test") {
    testResponse((d, p) => new MicrostripLineModel(d, p))
  }

  test("Microstrip line model alternative response calculation test") {
    testResponse((d, p) => new MicrostripLineModelAlt(d, p))
  }

  private def testResponse(modelProducer: (Distances, MicrostripParams) => AbstractCanalModel): Unit = {
    val dists = TestDataHolder.dists
    val externallyCalculatedResponse = TestDataHolder.externallyCalculatedResponse
    val result = modelProducer(dists, DataHolder.getCurrent.measurementParams.getMicrostripParams)
    val calculatedResponse = result.response(DataHolder.getCurrent.measurementParams)
    println("Odpowiedź obliczona dla wczytanych danych: " + calculatedResponse)
    println("Odpowiedź obliczona przez program zewnętrzny: " + externallyCalculatedResponse)
    val errorAbs = MatrixOps.relativeErrorAbs(calculatedResponse.value, externallyCalculatedResponse.value)
    printf("Błąd względny na amplitudach: (%s)\n", errorAbs.mkString(", "))
    val avgErrorAbs = MatrixOps.avg(errorAbs)
    printf("Wartość średnia: %s, min: %s, max: %s\n", avgErrorAbs, errorAbs.min, errorAbs.max)
    val errorPhase = MatrixOps.absoluteErrorPhase(calculatedResponse.value, externallyCalculatedResponse.value)
    printf("Błąd bezwzględny na fazach: (%s)\n", errorPhase.mkString(", "))
    val avgErrorPhase = MatrixOps.avg(errorPhase)
    printf("Wartość średnia: %s, min: %s, max: %s\n", avgErrorPhase, errorPhase.min, errorPhase.max)

    assert(avgErrorAbs <= 0.05)
    assert(avgErrorPhase <= 0.1)
  }


}
