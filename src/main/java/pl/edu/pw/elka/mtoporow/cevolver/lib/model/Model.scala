package pl.edu.pw.elka.mtoporow.cevolver.lib.model

/**
 * Bazowy model
 * Data utworzenia: 06.05.15
 *
 * @tparam R typ odpowiedzi modelu
 * @author Michał Toporowski
 */
@Deprecated
trait Model[R] {
  // FIXME:: do zastanowienia, chyba niepotrzebne

  /**
   * Zwraca odpowiedź modelu
   *
   * @return odpowiedź modelu
   */
  def calculate(): R
}
