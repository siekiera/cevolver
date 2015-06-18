package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import org.apache.commons.math3.complex.Complex
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.matrix.{SMatrix, TMatrix}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.PhysicalConstants

/**
 * Mikropasek
 * Data utworzenia: 14.06.15, 18:53
 *
 * @param w wysokość paska
 * @param l długość paska
 * @param t wysokość paska
 * @param h wysokość dielektrycznego podłoża
 * @param epsr względna przenikalność elektryczna podłoża
 *
 * @author Michał Toporowski
 */
class Microstrip(
                  val w: Double,
                  val l: Double,
                  val t: Double,
                  val h: Double,
                  val epsr: Double) {

  private val wph = w / h

  /**
   * Względna skuteczna przenikalność elektryczna (eps_ef)
   */
  private val epsef = {
    // względna skuteczna przenikalność elektr. (Korszeń, s. 18)
    if (wph < 1.0) {
      (epsr + 1) / 2 + ((epsr - 1) / 2) * (1.0 / math.sqrt(1.0 + 12.0 * h / w) + 0.041 * (1.0 - wph) * (1.0 - wph))
    } else {
      (epsr + 1) / 2 + ((epsr - 1) / 2) * (1.0 / math.sqrt(1.0 + 12.0 * h / w))
    }
  }

  /**
   * Oblicza impedancję charakterystyczną (Z0)
   */
  def charImpedance(): Double = {
    // obliczenie (Korszeń s. 18) - różne wzory dla w / h < 1 i w / h >= 1
    // uwaga: do rozważenia - dla dużej precyzji można zastąpić szerokość szerokością skuteczną (w_s)
    if (wph < 1.0) {
      (60 / math.sqrt(epsef)) * math.log(8 * h / w + w / (4 * h))
    } else {
      120 * math.Pi / (math.sqrt(epsef) * (wph + 1.393 + (2.0 / 3.0) * math.log(wph + 1.444)))
    }
  }

  /**
   * Oblicza długość elektryczną (theta)
   * @param f częstotliwość fali
   * @return
   */
  private def electricalLength(f: Double): Double = {
    // prędkość fazowa fali - Korszeń, s. 19
    def vphi = PhysicalConstants.LIGHT_SPEED_MPS / math.sqrt(epsef)
    // długość elektryczna
    2 * math.Pi * l * f / vphi
  }

  /**
   * Oblicza macierz S mikropaska
   *
   * @param f częstotliwość fali
   * @param z01 impedancja charakterystyczna prowadnic mikrofalowych podłączonych do wrót linii Z01
   * @return
   */
  def sMatrix(f: Double, z01: Complex): SMatrix = {
    // wyznaczenie macierzy S (Korszeń s. 20)
    // s11 = s22 = j(r^2 - 1)sin(theta) / 2rcos(theta) + j(r^2 + 1)sin(theta), gdzie r = z0/z01
    val theta = electricalLength(f)
    val z0 = Complex.valueOf(charImpedance())
    val r = z0.divide(z01)
    val r2 = r.multiply(r)
    val denominator = r.multiply(2.0).multiply(math.cos(theta)).add(Complex.I.multiply(r2.add(1)).multiply(math.sin(theta)))
    val s11 = Complex.I.multiply(r2.subtract(1)).multiply(math.sin(theta))
      .divide(denominator)
    val s22 = s11
    // s12 = s21 = 2r / 2rcos(theta) + j(r^2 + 1)sin(theta)
    val s12 = r.multiply(2.0).divide(denominator)
    val s21 = s12
    new SMatrix(s11, s12, s21, s22)
  }

  /**
   * Oblicza macierz T mikropaska
   *
   * @param f częstotliwość fali
   * @param z01 impedancja charakterystyczna prowadnic mikrofalowych podłączonych do wrót linii Z01
   * @return
   */
  def tMatrix(f: Double, z01: Complex): TMatrix = {
    // wyznaczenie macierzy T (Korszeń s. 20)
    val theta = electricalLength(f)
    val z0 = Complex.valueOf(charImpedance())
    val r = z0.divide(z01)
    // t11 = cos(theta) - j * 1/2 (r + 1/r) sin(theta)
    // t22 = cos(theta) + j * 1/2 (r + 1/r) sin(theta)
    val costheta = math.cos(theta)
    val sintheta = math.sin(theta)
    val commonSubstrate = Complex.I.multiply(0.5).multiply(r.add(Complex.ONE.divide(r))).multiply(sintheta)
    val t11 = commonSubstrate.add(costheta)
    val t22 = commonSubstrate.subtract(costheta)
    // t12 = -t21 =  j * 1/2 (r - 1/r) sin(theta)
    val t12 = Complex.I.multiply(0.5).multiply(r.add(Complex.ONE.divide(r))).multiply(sintheta)
    val t21 = t12.negate()
    new TMatrix(t11, t12, t21, t22)
  }


}
