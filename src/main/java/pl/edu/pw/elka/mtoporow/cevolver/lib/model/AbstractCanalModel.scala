package pl.edu.pw.elka.mtoporow.cevolver.lib.model

/**
 * Abstrakcyjny model kanału transmisyjnego
 * Data utworzenia: 06.05.15
 *
 * @author Michał Toporowski
 */
abstract class AbstractCanalModel {
  /**
   * Odległości miejsc nieciągłości od początku
   *
   * @return
   */
  def distances: Distances

  /**
   * Odpowiedź kanału
   *
   * @return
   */
  def response(): CanalResponse

  /**
   * Tworzy nowy model tego samego typu z innymi odległościami
   *
   * @param distances odległości
   * @return
   */
  def createNew(distances: Distances): AbstractCanalModel
}


