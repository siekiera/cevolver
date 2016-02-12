package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.scalatest.FunSuite
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.alt.MicrostripLineModelAlt
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, Distances}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.collection.mutable

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
    val checker = new ModelChecker(modelProducer)
    val failed = new mutable.HashSet[String]()
    while (checker.loadNext()) {
      println("Zestaw danych: " + DataHolder.getCurrentId)
      println("Odpowiedź obliczona dla wczytanych danych: " + checker.calculatedResponse())
      println("Odpowiedź obliczona przez program zewnętrzny: " + checker.externallyCalculatedResponse())
      val errorAbs = checker.errorAbs()
      printf("Błąd bezwzględny na amplitudach: (%s)\n", errorAbs.mkString(", "))
      val avgErrorAbs = MatrixOps.avg(errorAbs)
      printf("Wartość średnia: %s, min: %s, max: %s\n", avgErrorAbs, errorAbs.min, errorAbs.max)
      val errorPhase = checker.errorPhase()
      printf("Błąd bezwzględny na fazach: (%s)\n", errorPhase.mkString(", "))
      val avgErrorPhase = MatrixOps.avg(errorPhase)
      printf("Wartość średnia: %s, min: %s, max: %s\n", avgErrorPhase, errorPhase.min, errorPhase.max)
      if (avgErrorAbs > 0.1 || avgErrorPhase > 0.2) {
        failed += DataHolder.getCurrentId
      }
    }
    assert(failed.isEmpty)
  }


}
