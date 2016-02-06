package pl.edu.pw.elka.mtoporow.cevolver.algorithm.util

import java.util.concurrent.Executors

/**
 * Klasa wykonująca zadania asynchronicznie
 * Data utworzenia: 06.02.16, 21:07
 * @author Michał Toporowski
 */
class AsyncRunner {
  private val executor = Executors.newFixedThreadPool(20)

  /**
   * Wykonuje zadanie asynchronicznie w tym wykonawcy
   *
   * @param runnable runnable
   */
  def execute(runnable: Runnable) = executor.execute(runnable)

  /**
   * Wykonuje asynchronicznie funkcję Scali w tym wykonawcy
   * @param function bezparametrowa funkcja
   */
  def execute(function: () => Unit) = {
    executor.execute(new Runnable {
      override def run(): Unit = function()
    })
  }

  /**
   * Wyłącza wykonywacza
   *
   */
  def shutdown(): Unit = executor.shutdownNow()
}
