package pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.{Array2DRowFieldMatrix, FieldMatrix}

/**
 * Macierz S
 * Data utworzenia: 14.06.15, 18:57
 * @author Michał Toporowski
 */
class SMatrix(val s11: Complex, s12: Complex, val s21: Complex, val s22: Complex) {

  def asMatrix: FieldMatrix[Complex] = new Array2DRowFieldMatrix[Complex](Array(Array(s11, s12), Array(s21, s22)))
}
