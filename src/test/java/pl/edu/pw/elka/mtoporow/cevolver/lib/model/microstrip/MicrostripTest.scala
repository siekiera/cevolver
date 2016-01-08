package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.util.Units

/**
 * Test algorytmu obliczającego parametry mikropaska
 * Data utworzenia: 17.06.15, 21:40
 * @author Michał Toporowski
 */
class MicrostripTest extends org.scalatest.FunSuite {

  test("characteristic impedance calculation test") {
    // Porównujemy wyniki działania z wynikami uzyskanymi z
    // http://www.microwaves101.com/calculators/866-microstrip-calculator
    // http://www.emtalk.com/mscalc.php
    val data = List(
      // (epsr, w, h, l, f, expectedZo, expectedELDeg)
      (7.5, 6.0, 4.0, 10.0, 4.0, 45.18, 110.85),
      (434.0, 32.0, 21.0, 10.0, 4.0, 6.08, 817.65),
      (434.0, 32.0, 21.0, 50.0, 4.0, 6.08, 4088.27),
      (434.0, 32.0, 21.0, 20.0, 6.0, 6.08, 2452.96),
      //      (51.23, 34.33, 234.33, 39.90),
      (51.23, 334.33, 234.33, 15.0, 4.0, 18.27, 421.69),
      (3.9, 3.937, 7.874, 15.748, 4.0, 100.78, /*119.18*/ 127.0)
    )
    val correct = data.map(input =>
      if (performTest(input._1, input._2, input._3, input._4, input._5, input._6, input._7)) 1 else 0
    ).sum
    println("Poprawnych wyników: " + correct + "/" + data.size)
    assert(correct == data.size)
  }

  /**
   * Przeprowadza test dla danych parametrów
   *
   * @param epsr epsr
   * @param wmm w (mm)
   * @param hmm h (mm)
   * @param lmm l (mm)
   * @param fGhz f (Ghz)
   * @param expectedZ0 oczek. Z0 (Om)
   * @param expectedELDeg oczek. EL (st.)
   * @return
   */
  private def performTest(epsr: Double, wmm: Double, hmm: Double, lmm: Double, fGhz: Double, expectedZ0: Double, expectedELDeg: Double) = {
    val microstrip = new Microstrip(Units.MILLI.toSI(wmm), Units.MILLI.toSI(lmm), 0, Units.MILLI.toSI(hmm), epsr)
    val z0 = microstrip.charImpedance()
    val el = microstrip.electricalLength(Units.GIGA.toSI(fGhz))
    println("Obliczone Z0: " + z0 + "; oczekiwane: " + expectedZ0)
    (100 * z0).round == (100 * expectedZ0).round
    val expectedEL = Units.DEG.toSI(expectedELDeg)
    println("Obliczona dł. elektr.: " + el + "; oczekiwana: " + expectedEL)
    (10 * el).round == (10 * expectedEL).round
  }

}
