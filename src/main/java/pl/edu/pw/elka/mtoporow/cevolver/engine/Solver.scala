package pl.edu.pw.elka.mtoporow.cevolver.engine

import java.util

import org.uncommons.watchmaker.framework.operators.EvolutionPipeline
import org.uncommons.watchmaker.framework.selection.RankSelection
import org.uncommons.watchmaker.framework.termination.GenerationCount
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.{CFType, EOType, RegisteredParams}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts.{SimpleCandidateFactory, SimpleFitnessEvaluator, SimpleMutation}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{AlgorithmParameters, EvolutionaryAlgorithm, InternalAlgorithmParams}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse

/**
 * Silnik rozwiązujący problem
 * Data utworzenia: 15.04.15
 * @author Michał Toporowski
 */
class Solver {

  /**
   * Rozwiązuje problem
   *
   * @param parameters
   * @param data
   * @return
   */
  def solve(parameters: AlgorithmParameters, data: CanalResponse): EvolutionaryAlgorithm.C = {
    val algorithm = new EvolutionaryAlgorithm
    algorithm.parameters = convertParams(parameters)
    algorithm.solve(data)
  }

  /**
   * Konwertuje parametry zewnętrzne (deklaratywne) na wewnętrzne (programowe)
   *
   * @param parameters
   * @return
   */
  private def convertParams(parameters: AlgorithmParameters): InternalAlgorithmParams = {
    val result = new InternalAlgorithmParams
    result.populationSize = parameters.populationSize
    result.eliteCount = parameters.eliteCount
    result.cf = parameters.candidateFactory.partType match {
      case CFType.DEFAULT => new SimpleCandidateFactory
    }
    val operators = new util.ArrayList[EvolutionaryAlgorithm.EO]()
    for (opType <- parameters.operators) {
      val operator = opType.partType match {
        case EOType.DEFAULT => new SimpleMutation()
      }
      operators.add(operator)
    }
    result.eo = new EvolutionPipeline[EvolutionaryAlgorithm.C](operators)
    // TODO - dodać case'y też tu
    result.fe = new SimpleFitnessEvaluator
    result.ss = new RankSelection()
    result.tc = new GenerationCount(parameters.terminationCondition.paramValueCasted[Int](RegisteredParams.GENERATION_COUNT))
    result
  }
}
