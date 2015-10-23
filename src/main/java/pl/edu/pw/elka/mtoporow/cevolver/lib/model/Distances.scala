package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.linear.RealVector

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
  def last = distances.getEntry(distances.getDimension - 1)
}
