package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._

/**
 * Parametry algorytmu
 * Data utworzenia: 22.05.15, 13:36
 * @author Michał Toporowski
 */
class AlgorithmParameters {

  /**
   * Wielkość populacji
   */
  var populationSize: Int = _

  var eliteCount: Int = _

  var operators: List[AlgorithmPartParams[EOType]] = _

  var candidateFactory: AlgorithmPartParams[CFType] = _

  var selectionStrategy: AlgorithmPartParams[SSType] = null

  var terminationCondition: AlgorithmPartParams[TCType] = null

  var fitnessEvaluator: AlgorithmPartParams[FEType] = null


}
