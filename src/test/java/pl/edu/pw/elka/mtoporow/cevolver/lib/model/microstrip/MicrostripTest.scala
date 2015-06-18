package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

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
      (7.5, 6.0, 4.0, 45.18),
      (434.0, 32.0, 21.0, 6.08),
//      (51.23, 34.33, 234.33, 39.90),
      (51.23, 334.33, 234.33, 18.27),
      (51.23, 224.33, 234.33, 22.49)
    )
    val correct = data.map(input =>
      if (performTest(input._1, input._2, input._3, input._4)) 1 else 0
    ).sum
    println("Poprawnych wyników: " + correct + "/" + data.size)
    assert(correct == data.size)
  }

  /**
   * Przeprowadza test dla danych parametrów
   *
   * @param epsr
   * @param w
   * @param h
   * @param expectedZ0
   * @return
   */
  private def performTest(epsr: Double, w: Double, h: Double, expectedZ0: Double) = {
    val microstrip = new Microstrip(w, 0, 0, h, epsr)
    val z0 = microstrip.charImpedance()
    println("Obliczone Z0: " + z0 + "; oczekiwane: " + expectedZ0)
    (100 * z0).round == (100 * expectedZ0).round
  }

}
