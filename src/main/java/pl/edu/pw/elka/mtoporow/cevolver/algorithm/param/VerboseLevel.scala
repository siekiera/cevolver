package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param

/**
 * Klasa zawierająca ustawienia poziomów logowania algorytmu
 * Data utworzenia: 06.02.16, 12:55
 * @author Michał Toporowski
 */
class VerboseLevel private() {
  var generationCount = false
  var distances = false
  var response = false
  var fitness = false
}

object VerboseLevel {
  /**
   * Tworzy obiekt typu VerboseLevel na podstawie ciągu znaków
   *
   * Flagi:
   * n - numer pokolenia
   * d - odległości
   * r - odpowiedź
   * f - funkcja przystosowania
   *
   * @param s ciąg znaczników
   * @return obiekt typu VerboseLevel
   */
  def apply(s: String): VerboseLevel = {
    val result = new VerboseLevel()
    for (c <- s.toCharArray) {
      c match {
        case 'n' => result.generationCount = true
        case 'd' => result.distances = true
        case 'r' => result.response = true
        case 'f' => result.fitness = true
      }
    }
    result
  }

  /**
   * Tworzy obiekt typu VerboseLevel z wszystkimi przełącznikami włączonymi
   * @return obiekt typu VerboseLevel
   */
  def allOn(): VerboseLevel = {
    apply("ndrf")
  }

  /**
   * Tworzy obiekt typu VerboseLevel z wszystkimi przełącznikami wyłączonymi
   * @return obiekt typu VerboseLevel
   */
  def allOff(): VerboseLevel = new VerboseLevel
}
