package pl.edu.pw.elka.mtoporow.cevolver.data

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse

/**
 * Cecha obiektu dostarczającego dane
 * Data utworzenia: 05.06.15, 13:28
 * @author Michał Toporowski
 */
trait DataProvider {
  /**
   * Dostarcza dane
   *
   * @return
   */
  def provide: CanalResponse
}
