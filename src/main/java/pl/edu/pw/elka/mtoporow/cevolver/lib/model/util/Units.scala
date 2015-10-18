package pl.edu.pw.elka.mtoporow.cevolver.lib.model.util

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units.{U, D, Deg}

/**
 * Klasa zawierająca definicje jednostek
 *
 * Data utworzenia: 17.10.15 14:41
 * @author Michał Toporowski
 */
object Units {

  /**
   * Klasa reprezentująca jednostkę
   * @param valueInSI wartość danej jednostki wyrażona w jednostce z układu SI
   */
  private class BaseUnit(val valueInSI: Double) {
    def toSI(value: Double) = value * valueInSI
    def fromSI(value: Double) = value / valueInSI
  }

  val DEG = new BaseUnit(math.Pi / 180)
  val INCH = new BaseUnit(0.0254)
  val MILLI = new BaseUnit(0.001)
  val MEGA = new BaseUnit(1000000)
  val GIGA = new BaseUnit(1000000000)

  /**
   * Klasa opakowująca wartość wyrażoną w jednostce
   * @param value wartość
   */
  class U(val value: Double) {
    def toSI(unit: BaseUnit) = unit.toSI(value)
    def toSIU(unit: BaseUnit) = new U(unit.toSI(value))
    def fromSI(unit: BaseUnit) = unit.fromSI(value)
    def fromSIU(unit: BaseUnit) = new U(unit.fromSI(value))
  }
}
