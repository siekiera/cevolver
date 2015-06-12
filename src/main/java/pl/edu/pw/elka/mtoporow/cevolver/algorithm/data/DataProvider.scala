package pl.edu.pw.elka.mtoporow.cevolver.algorithm.data

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse

/**
 * Cecha obiektu dostarczającego dane
 * Data utworzenia: 05.06.15, 13:28
 * @author Michał Toporowski
 */
trait DataProvider {
  def provide: CanalResponse
}
