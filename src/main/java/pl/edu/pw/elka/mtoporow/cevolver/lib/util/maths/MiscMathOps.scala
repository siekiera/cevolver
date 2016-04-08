package pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths

import org.apache.commons.math3.complex.Complex

/**
 * Różne obliczenia matematyczne
 * Data utworzenia: 09.02.16, 20:23
 * @author Michał Toporowski
 */
object MiscMathOps {


  /**
   * Zwraca błąd względny
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd względny
   */
  def relativeError(x: Double, p: Double) = math.abs(x / p - 1)

  /**
   * Zwraca błąd względny w procentach
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd względny
   */
  def relativeErrorPercentage(x: Double, p: Double) = 100 * math.abs(x / p - 1)

  /**
   * Zwraca błąd względny liczony na modułach liczb zespolonych
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd względny
   */
  def relativeErrorAbs(x: Complex, p: Complex) = relativeError(x.abs(), p.abs())

  /**
   * Zwraca błąd względny liczony na fazach (kątach) liczb zespolonych
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd względny
   */
  def relativeErrorPhase(x: Complex, p: Complex) = relativeError(x.getArgument, p.getArgument)

  /**
   * Zwraca błąd bezwzględny
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd bezwzględny
   */
  def absoluteError(x: Double, p: Double) = math.abs(x - p)

  /**
   * Zwraca błąd bezwzględny liczony na modułach liczb zespolonych
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd bezwzględny
   */
  def absoluteErrorAbs(x: Complex, p: Complex) = absoluteError(x.abs(), p.abs())

  /**
   * Zwraca błąd bezwzględny liczony na fazach (kątach) liczb zespolonych
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd bezwzględny
   */
  def absoluteErrorPhase(x: Complex, p: Complex) = {
    math.min(
      absoluteError(x.getArgument, p.getArgument),
      absoluteError(positiveArg(x), positiveArg(p)))
  }

  /**
   * Zwraca dodatni kąt liczby zespolonej (dodaje pi, jeśli jest ujemny)
   *
   * @param z liczba zespolona
   * @return kąt z zakresu [0, 2pi)
   */
  def positiveArg(z: Complex) = {
    val arg = z.getArgument
    if (arg < 0) arg + 2 * math.Pi else arg
  }
}
