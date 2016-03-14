package pl.edu.pw.elka.mtoporow.cevolver.engine

import java.util

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline
import org.uncommons.watchmaker.framework.selection.{RankSelection, RouletteWheelSelection, StochasticUniversalSampling, TournamentSelection}
import org.uncommons.watchmaker.framework.termination.{GenerationCount, Stagnation, TargetFitness}
import pl.edu.pw.elka.mtoporow.cevolver.algorithm._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts._

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
   * @return wynik (najlepszy osobnik)
   */
  def solve(parameters: AlgorithmParameters): EvolutionaryAlgorithm.C = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, VerboseLevel.allOn())
    algorithm.solve()
  }

  /**
   * Rozwiązuje problem
   *
   * @param parameters parametry
   * @return wynik (lista osobników posortowana od najlepszego)
   */
  def solveWithAllResults(parameters: AlgorithmParameters) = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, VerboseLevel.allOn())
    algorithm.solveWithAllResults()
  }

  /**
   * Rozwiązuje problem
   *
   * @param parameters parametry
   * @param verboseLevel poziom logowania
   * @return wynik (lista osobników posortowana od najlepszego)
   */
  def solveWithAllResults(parameters: AlgorithmParameters, verboseLevel: VerboseLevel) = {
    val internalParameters = convertParams(parameters)
    val algorithm = new EvolutionaryAlgorithm(internalParameters, verboseLevel)
    algorithm.solveWithAllResults()
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
    // TODO:: rozważyć wykorzystanie tu klasy MicrostripLineModelFactory
    val cf = (DataHolder.getCurrent.measurementParams.getModelType, parameters.candidateFactory.partType) match {
      case (ModelType.LONGBREAK, CFType.FIXED_WIDTH) => new FixedWidthCandidateFactory(parameters.breakCount)
      case (ModelType.LONGBREAK, CFType.VARIABLE_WIDTH) => new LongbreakCandidateFactory(parameters.breakCount)
      case (ModelType.SHORTBREAK, _) => new ShortbreakCandidateFactory(parameters.breakCount)
    }
    result.cf = cf
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
        case EOType.PARAMETER_AVG_VAL_CROSSOVER => new ParameterAverageValueCrossover(probability)
        case EOType.SELF_ADAPTATING_MUTATION => new SelfAdaptatingMutation(cf.traitCount(), probability)
      }
      operators.add(operator)
    }
    result.eo = new EvolutionPipeline[EvolutionaryAlgorithm.C](operators)
    val pf = parameters.fitnessEvaluator.partType match {
      case FEType.DEFAULT => DefaultPF
      case FEType.PHASE_DEPENDENT => PhaseDependentPF
      case _ => null
    }
    result.fe = new BaseFitnessEvaluator(parameters.fitnessEvaluator.paramValueCasted[Double](RegisteredParams.PUNISHMENT_RATIO), pf)
    result.ss = parameters.selectionStrategy.partType match {
      case SSType.RANK => new RankSelection()
      case SSType.ROULETTE_WHEEL => new RouletteWheelSelection()
      case SSType.SUS => new StochasticUniversalSampling()
      case SSType.TOURNAMENT => new TournamentSelection(readProbability(parameters.selectionStrategy))
      case _ => null
    }
    result.es = parameters.selectionStrategy.partType match {
      case SSType.ES_PLUS => ESParams(plusSelection = true, parameters.selectionStrategy.paramValueCasted(RegisteredParams.OFFSPRING_MOD))
      case SSType.ES_COMMA => ESParams(plusSelection = false, parameters.selectionStrategy.paramValueCasted(RegisteredParams.OFFSPRING_MOD))
      case _ => null
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
