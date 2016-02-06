package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

/**
 * Parametry linii mikropaskowej
 * Data utworzenia: 06.05.15, 20:34
 *
 * @param w wysokość paska
 * @param t wysokość paska
 * @param h wysokość dielektrycznego podłoża
 * @param epsr względna przenikalność elektryczna podłoża
 * @param biggerW wysokość grubszego paska
 *              TODO:: do ustalenia, czy  ostatnie powinno być tu podawane
 *
 * @author Michał Toporowski
 */
class MicrostripParams(val w: Double,
                       val t: Double,
                       val h: Double,
                       val epsr: Double,
                       val biggerW: Double) {

  override def toString = s"(w=$w, t=$t, h=$h, epsr=$epsr, biggerW=$biggerW)"
}
