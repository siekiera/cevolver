package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.linear.RealVector
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.Units
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
   * Zwraca odległości bezwzględne (tzn. od początku linii)
   *
   * @return wektor odległości
   */
  def absolute = MatrixOps.asSums(distances)

  /**
   * Konwertuje odległości na milimetry i przedstawia w postaci łańcucha znaków
   * @return String
   */
  def toStringMM = "odległości [mm]: (" + MatrixOps.asIterable(distances).map(Units.MILLI.fromSI(_).toString).mkString(", ") + ")"

  /**
   * Rozmiar wektora
   *
   * @return rozmiar wektora
   */
  def size = distances.getDimension
}
