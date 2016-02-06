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

  var eliteCount: Int = _

  var cf: EvolutionaryAlgorithm.CF = _

  var fe: EvolutionaryAlgorithm.FE = _

  var eo: EvolutionaryAlgorithm.EO = _

  var ss: EvolutionaryAlgorithm.SS = _

  var tc: EvolutionaryAlgorithm.TC = _
}
