package pl.edu.pw.elka.mtoporow.cevolver.algorithm

/**
 * Klasa bazowa dla algorytmów
 * Data utworzenia: 15.04.15
 * @author Michał Toporowski
 */
@Deprecated
trait Algorithm[D, R] {
  /**
   * Rozwiązuje problem
   * @param data dane wejściowe
   * @return wynik
   */
  def solve(data: D): R
}
