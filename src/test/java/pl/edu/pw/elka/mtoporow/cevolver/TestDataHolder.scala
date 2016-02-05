package pl.edu.pw.elka.mtoporow.cevolver

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder

/**
 * Klasa ładująca i przechowująca dane dla testu
 * Data utworzenia: 20.11.15, 15:43
 * @author Michał Toporowski
 */
object TestDataHolder {
  DataHolder.load("20")

  def dists = DataHolder.getCurrent.expectedDistances

  def externallyCalculatedResponse = DataHolder.getCurrent.canalResponse
}
