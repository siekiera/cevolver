package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse, Distances}

/**
 * Model kanału w postaci linii mikropaskowej
 * Data utworzenia: 06.05.15, 20:24
 *
 * @author Michał Toporowski
 */
class MicrostripLineModel(val distances: Distances, val params: MicrostripParams)
  extends AbstractCanalModel {

  /**
   * Zwraca odpowiedź modelu
   *
   * @return odpowiedź modelu
   */
  override def response(): CanalResponse = ??? //TODO

}