package pl.edu.pw.elka.mtoporow.cevolver.algorithm

/**
 * Klasa reprezentująca parametry algorytmu ewolucyjnego
 *
 * Data utworzenia: 13.05.15, 15:50
 * @author Michał Toporowski
 */
class InternalAlgorithmParams {
  /**
   * Wielkość populacji
   */
  var populationSize: Int = _
  /**
   * Wielkość elity
   */
  var eliteCount: Int = _
  /**
   * Fabryka osobników
   */
  var cf: EvolutionaryAlgorithm.CF = _
  /**
   * Ewaluator przystosowania
   */
  var fe: EvolutionaryAlgorithm.FE = _
  /**
   * Operator ewolucyjny
   */
  var eo: EvolutionaryAlgorithm.EO = _
  /**
   * Strategia wyboru
   */
  var ss: EvolutionaryAlgorithm.SS = _
  /**
   * Kryterium zakończenia
   */
  var tc: EvolutionaryAlgorithm.TC = _
  /**
   * Strategia ewolucyjna (jeśli różna od null, wówczas ss nie jest brana pod uwagę)
   */
  var es: ESParams = _
}

/**
 * Parametry strategii selekcji
 *
 * @param plusSelection czy selekcja (μ,λ)
 * @param offspringModifier współczynnik λ/μ
 */
case class ESParams(plusSelection: Boolean, offspringModifier: Int)
