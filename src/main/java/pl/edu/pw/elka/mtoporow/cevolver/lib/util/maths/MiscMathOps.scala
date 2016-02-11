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
   * Zwraca błąd bezwzględny liczony na fazach (kątach) liczb zespolonych
   *
   * @param x wartość zmierzona
   * @param p wartość dokładna
   * @return błąd bezwzględny
   */
  def absoluteErrorPhase(x: Complex, p: Complex) = absoluteError(x.getArgument, p.getArgument)
}
