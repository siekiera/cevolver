package pl.edu.pw.elka.mtoporow.cevolver.engine

import java.util

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline
import org.uncommons.watchmaker.framework.selection.{RankSelection, RouletteWheelSelection, StochasticUniversalSampling, TournamentSelection}
import org.uncommons.watchmaker.framework.termination.{GenerationCount, Stagnation, TargetFitness}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{AlgorithmParameters, AlgorithmPartParams, EvolutionaryAlgorithm, InternalAlgorithmParams}
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
   * @param parameters parametry
   * @param data dane
   * @return wynik (najlepszy osobnik)
   */
  def solve(parameters: AlgorithmParameters, data: CanalResponse): EvolutionaryAlgorithm.C = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, VerboseLevel.allOn())
    algorithm.solve(data)
  }

  /**
   * Rozwiązuje problem
   *
   * @param parameters parametry
   * @param data dane
   * @return wynik (lista osobników posortowana od najlepszego)
   */
  def solveWithAllResults(parameters: AlgorithmParameters, data: CanalResponse) = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, VerboseLevel.allOn())
    algorithm.solveWithAllResults(data)
  }

  /**
   * Rozwiązuje problem
   *
   * @param parameters parametry
   * @param data dane
   * @param verboseLevel poziom logowania
   * @return wynik (lista osobników posortowana od najlepszego)
   */
  def solveWithAllResults(parameters: AlgorithmParameters, data: CanalResponse, verboseLevel: VerboseLevel) = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, verboseLevel)
    algorithm.solveWithAllResults(data)
  }

  /**
   * Konwertuje parametry zewnętrzne (deklaratywne) na wewnętrzne (programowe)
   *
   * @param parameters parametry zewnętrzne
   * @return parametry wewnętrzne
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
      val probability = readProbability(opType)
      val operator = opType.partType match {
        case EOType.SIMPLE_MUTATION => new SimpleMutation(probability)
        case EOType.DIST_ARRAY_CROSSOVER => new DistArrayCrossover(probability)
        case EOType.INVERSION => new Inversion(probability)
        case EOType.STANDARD_GAUSSIAN_MUTATION => new StandardGaussianMutation(probability,
          opType.paramValueCasted(RegisteredParams.STANDARD_DEVIATION))
        case EOType.AVERAGE_VALUE_CROSSOVER => new AverageValueCrossover(probability)
      }
      operators.add(operator)
    }
    result.eo = new EvolutionPipeline[EvolutionaryAlgorithm.C](operators)
    // TODO - dodać case'y też tu
    result.fe = new SimpleFitnessEvaluator(parameters.fitnessEvaluator.paramValueCasted[Double](RegisteredParams.PUNISHMENT_RATIO))
    result.ss = parameters.selectionStrategy.partType match {
      case SSType.RANK => new RankSelection()
      case SSType.ROULETTE_WHEEL => new RouletteWheelSelection()
      case SSType.SUS => new StochasticUniversalSampling()
      case SSType.TOURNAMENT => new TournamentSelection(readProbability(parameters.selectionStrategy))
    }

    result.tc = parameters.terminationCondition.partType match {
      case TCType.GENERATION_COUNT => new GenerationCount(parameters.terminationCondition.paramValueCasted[Int](RegisteredParams.GENERATION_COUNT))
      case TCType.STAGNATION => new Stagnation(parameters.terminationCondition.paramValueCasted[Int](RegisteredParams.GENERATION_COUNT), result.fe.isNatural)
      case TCType.TARGET_FITNESS => new TargetFitness(parameters.terminationCondition.paramValueCasted(RegisteredParams.TARGET_FITNESS), result.fe.isNatural)
    }
    result
  }

  private def readProbability(partParams: AlgorithmPartParams[_]) = new Probability(partParams.paramValueCasted(RegisteredParams.PROBABILITY))
}
