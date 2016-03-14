package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.linear.RealVector

/**
 * Klasa reprezentująca odległości zawierające informacje o długościach i szerokościach
 * Data utworzenia: 29.02.16, 13:40
 * @author Michał Toporowski
 */
class LWDists private(distances: RealVector, val breakCount: Int) extends Distances(distances) {

  /**
   * Pobiera i-tą długość
   *
   * @param i
   * @return
   */
  def lEntry(i: Int) = distances.getEntry(i)

  /**
   * Pobiera i-tą szerokość
   *
   * @param i
   * @return
   */
  def wEntry(i: Int) = distances.getEntry(i + breakCount)

  /**
   * Zwraca wektor długości
   *
   * @return wektor
   */
  def lengths = distances.getSubVector(0, breakCount)

  /**
   * Konstruktor
   *
   * @param lengths wektor długości
   * @param widths wektor szerokości
   * @return
   */
  def this(lengths: RealVector, widths: RealVector) = this(lengths.append(widths), lengths.getDimension)
}

object LWDists {
  /**
   * Tworzy obiekt na potrzeby modelu shortbreak na podstawie istniejącego pełnego wektora
   *
   * @param distances wektor zawierający najpierw długości, a potem szerokości
   * @return
   */
  def shortbreak(distances: Distances): LWDists = {
    distances match {
      case dists: LWDists => dists
      case _ => new LWDists(distances.distances, distances.size / 2)
    }
  }

  /**
   * Tworzy obiekt na potrzeby modelu longbreak na podstawie istniejącego pełnego wektora
   *
   * @param distances wektor zawierający najpierw długości, a potem szerokości
   * @return
   */
  def longbreak(distances: Distances): LWDists = {
    distances match {
      case dists: LWDists => dists
      case _ => new LWDists(distances.distances, (1 + distances.size) / 2)
    }
  }
}
