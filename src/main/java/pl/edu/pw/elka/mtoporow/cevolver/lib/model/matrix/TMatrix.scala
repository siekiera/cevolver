package pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.{Array2DRowFieldMatrix, FieldMatrix}

/**
 * Macierz T
 * Data utworzenia: 14.06.15, 19:01
 * @author Michał Toporowski
 */
class TMatrix(val t11: Complex, val t12: Complex, val t21: Complex, val t22: Complex) {

  def asMatrix: FieldMatrix[Complex] = new Array2DRowFieldMatrix[Complex](Array(Array(t11, t12), Array(t21, t22)))

  def toSMatrix: SMatrix = {
    val s11 = t12.divide(t22)
    val s12 = t11.multiply(t22).subtract(t12.multiply(t21)).divide(t22)
    val s21 = Complex.ONE.divide(t22)
    val s22 = t21.negate().divide(t22)
    new SMatrix(s11, s12, s21, s22)
  }

  /**
   * Mnoży macierze T (połączenie kaskadowe)
   *
   * @param that
   * @return
   */
  def *(that: TMatrix) = {
    val matrix = asMatrix.multiply(that.asMatrix).getData
    new TMatrix(matrix(0)(0), matrix(0)(1), matrix(1)(0), matrix(1)(1))
  }

  /**
   * Zwraca element s11 bez obliczania pozostałych elementów macierzy S
   */
  def s11: Complex = {
    t12.divide(t22)
  }
}
