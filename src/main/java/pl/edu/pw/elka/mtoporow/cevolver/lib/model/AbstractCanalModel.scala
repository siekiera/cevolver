package pl.edu.pw.elka.mtoporow.cevolver.lib.model

import org.apache.commons.math3.linear.RealVector
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.MeasurementParams

/**
 * Abstrakcyjny model kanału transmisyjnego
 * Data utworzenia: 06.05.15
 *
 * @author Michał Toporowski
 */
abstract class AbstractCanalModel {
  /**
   * Zmienna na użytek zapisywania tymczasowych parametrów algorytmu (np. odchyleń standardowych mutacji)
   */
  var algorithmTempVector: RealVector = _
  private var _lastResponse: CanalResponse = _

  /**
   * Wektor odległości modelu (wszystkich jego cech)
   *
   * @return
   */
  def distances: Distances

  /**
   * Zwraca wektor długości modelu (poszukiwane zmienne)
   *
   * @return
   */
  def lengths: RealVector

  /**
   * Odpowiedź kanału
   *
   * @param measurementParams parametry pomiaru
   * @return odpowiedź
   */
  def response(measurementParams: MeasurementParams): CanalResponse = {
    _lastResponse = calculateResponse(measurementParams)
    _lastResponse
  }

  /**
   * Zwraca ostatnio obliczoną odpowiedź
   * @return odpowiedź
   */
  def lastResponse() = _lastResponse

  /**
   * Tworzy nowy model tego samego typu z innymi odległościami
   *
   * @param distances odległości
   * @return nowy obiekt modelu
   */
  def createNew(distances: Distances): AbstractCanalModel

  /**
   * Tworzy nowy model tego samego typu z innymi odległościami
   *
   * @param distances odległości (jako wektor)
   * @return nowy obiekt modelu
   */
  def createNew(distances: RealVector): AbstractCanalModel = createNew(new Distances(distances))

  /**
   * Ustawia wektor tymczasowy i zwraca obiekt modelu
   *
   * @param vector wektor
   * @return
   */
  def withTempVector(vector: RealVector) = {
    algorithmTempVector = vector
    this
  }

  /**
   * Oblicza odpowiedź kanału
   *
   * @param measurementParams parametry pomiaru
   * @return odpowiedź
   */
  protected def calculateResponse(measurementParams: MeasurementParams): CanalResponse
}


