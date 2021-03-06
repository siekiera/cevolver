package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.FieldVector
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Odpowiedź kanału
 * Data utworzenia: 06.05.15
 *
 * @param value wektor zespolonych współczynników odbicia dla każdej z częstotliwości (w @see MeasurementParams)
 * @author Michał Toporowski
 */
class CanalResponse(val value: FieldVector[Complex]) {
  override def toString: String = "CanalResponse: Gamma: " + MatrixOps.complexVecToString(value)
}
