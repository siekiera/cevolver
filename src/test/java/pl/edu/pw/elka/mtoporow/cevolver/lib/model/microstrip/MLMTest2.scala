package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.ArrayRealVector
import org.scalatest.{OneInstancePerTest, FunSuite}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.TMatrix
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{CanalUtils, LWDists}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps

/**
 * Klasa X
 * Data utworzenia: 22.03.16, 20:05
 * @author Michał Toporowski
 */
class MLMTest2 extends FunSuite with OneInstancePerTest {

  test("Test s11 for even") {
    println("2 miejsca nieciągłości")
    runNormal("20", 0.2)
    runNormal("20", 0.5)
    runNormal("20", 0.1)
    runNormal("20", 1.4)
    runNormal("20", 5.0)
  }

  test("Test s11 for 1") {
    println("1 miejsce nieciągłości")
    run1("20", 50.0, 0.2)
    run1("20", 50.0, 0.5)
    run1("20", 50.0, 1.4)
    run1("20", 40.0, 0.2)
    run1("20", 40.0, 0.5)
    run1("20", 30.0, 0.2)
    run1("20", 30.0, 0.5)
    run1("20", 1.4, 0.2)
    run1("20", 1.4, 0.5)
    run1("20", 1.4, 1.4)
    run1("20", 5.0, 0.2)
    run1("20", 5.0, 0.5)
  }

  private def runNormal(ds: String, last: Double): Unit = {
    val res = run(ds, last, distsForNormal)
    println(s"Zestaw $ds, ostatni: $last, s11 = $res")
  }

  private def run1(ds: String, impedance: Double, last: Double): Unit = {
    val res = run(ds, last, distsFor1Alt, impedance)
    println(s"Zestaw $ds, ostatni: $last, impedancja na końcu: $impedance, s11 = $res")
  }


  private def run(ds: String, last: Double, distsSupplier: () => LWDists, imp: Double = 0.0): Complex = {
    DataHolder.lazyLoad(ds)
    val pars = DataHolder.getCurrent.measurementParams.getMicrostripParams
    val dists = distsSupplier()
    val model = new LongbreakLineModel(dists, pars)
    val z01 = DataHolder.getCurrent.measurementParams.getImpedance
    val freq = DataHolder.getCurrent.measurementParams.getFrequencies.getEntry(0) //pierwsza lepsza
    var tMatrix: TMatrix = model.calcTMatrix(pars.w, dists.lEntry(0), z01, freq)
    // liczymy na początku tak, jak w modelu
    for (i <- 1 until dists.breakCount) {
      val elementMat: TMatrix = model.calcTMatrix(dists.wEntry(i - 1), dists.lEntry(i), z01, freq)
      tMatrix = tMatrix.multiply(elementMat)
    }
    if ((dists.breakCount & 1) == 0) {
      // Parzysta
      // Ostatni taki sam, jak pierwszy
      val lastMat: TMatrix = model.calcTMatrix(pars.w, last, z01, freq)
      tMatrix = tMatrix.multiply(lastMat)
      tMatrix.getS11
    } else {
      // Nieparzysta
      // Ostatni pasek o innej szerokości
      val lastMat = model.calcTMatrix(dists.wEntry(dists.breakCount - 1), last, z01, freq)
      tMatrix = tMatrix.multiply(lastMat)
      // Dodatkowa impedancja wyrównująca
      val impS11: Complex = CanalUtils.s11ForImpedance(Complex.valueOf(imp), z01)
      tMatrix.getS11WithCascadeS11(impS11)
    }
  }

  def distsForNormal() = {
//    new LWDists(DataHolder.getCurrent.expectedDistances.distances,
//      JavaVectorOps.createVector(DataHolder.getCurrent.measurementParams.getMicrostripParams.biggerW))
    LWDists.longbreak(DataHolder.getCurrent.expectedDistances)
  }

  def distsFor1() = {
    // FIXME:: to jest dobrze?
    new LWDists(DataHolder.getCurrent.expectedDistances.distances.getSubVector(0, 1), new ArrayRealVector())
  }

  def distsFor1Alt() = {
    def distsFor2 = DataHolder.getCurrent.expectedDistances.distances
    new LWDists(distsFor2.getSubVector(0, 1), distsFor2.getSubVector(2, 1))
  }

//  def distsFor3() = {
//    val lengths = new ArrayRealVector()
//    new LWDists()
//  }
}
