package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.linear.RealVector
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa reprezentująca odległości w kanale
 * Data utworzenia: 29.05.15, 18:29
 *
 * @param distances odległości pomiędzy kolejnymi miejscami nieciągłości
 * @author Michał Toporowski
 */
class Distances(val distances: RealVector) {
  /**
   * Zwraca ostatnią odległośc
   * @return ostatnia odległość
   */
  def last = MeasurementParams.getTotalLength - absolute.getEntry(distances.getDimension - 1)

  /**
   * Zwraca odległości bezwzględne (tzn. od początku linii)
   *
   * @return wektor odległości
   */
  def absolute = MatrixOps.asSums(distances)
}
