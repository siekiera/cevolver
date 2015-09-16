package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

/**
 * Parametry linii mikropaskowej
 * Data utworzenia: 06.05.15, 20:34
 *
 * @param w wysokość paska
 * @param t wysokość paska
 * @param h wysokość dielektrycznego podłoża
 * @param epsr względna przenikalność elektryczna podłoża
 * @param discW wysokość nieciągłości
 * @param discL długość nieciągłości
 *              TODO:: do ustalenia, czy 2 ostatnie powinny być tu podawane
 *
 * @author Michał Toporowski
 */
class MicrostripParams(val w: Double,
                       val t: Double,
                       val h: Double,
                       val epsr: Double,
                       val discW: Double,
                       val discL: Double)
