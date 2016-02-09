package pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldVector
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.MiscMathOps

/**
 * Klasa X
 * Data utworzenia: 09.02.16, 20:17
 * @author Michał Toporowski
 */
class ComplexVectorComparator(val v1: FieldVector[Complex], val v2: FieldVector[Complex]) {

  def compareAbs(): Array[Double] = {
    (MatrixOps.asIterable(v1), MatrixOps.asIterable(v2)).zipped.map(MiscMathOps.relativeErrorAbs).toArray
  }
}
