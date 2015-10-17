package pl.edu.pw.elka.mtoporow.cevolver.lib.model.util

/**
 * Klasa zawierająca metody konwersji jednostek
 *
 * Data utworzenia: 17.10.15 13:44
 * @author Michał Toporowski
 */
object UnitConversions {

  val MILLI = 0.001
  val MEGA = 1000000.0
  val GIGA = 1000000000.0

  /**
   *
   * @param deg kąt (stopnie)
   * @return kąt (rad)
   */
  def degToRad(deg: Double) = deg * math.Pi / 180

  def milToM(mil: Double) = mil * 2.45 * 0.00001



}
