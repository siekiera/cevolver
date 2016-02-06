package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import org.uncommons.maths.random.JavaRNG
import org.uncommons.watchmaker.framework._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.VerboseLevel
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse}

/**
 * Klasa bazowa dla algorytmów ewolucyjnych
 * Data utworzenia: 15.04.15
 *
 * @author Michał Toporowski
 */
class EvolutionaryAlgorithm(private val parameters: InternalAlgorithmParams, private val verboseLevel: VerboseLevel) {

  /**
   * Rozwiązuje problem
   *
   * @return wynik
   */
  def solve(): C = {
    val engine = prepareEngine()
    val result = engine.evolve(parameters.populationSize, parameters.eliteCount, parameters.tc)
    result
  }

  /**
   * Rozwiązuje problem
   *
   * @param data dane wejściowe
   * @return wszystkie wyniki
   */
  def solveWithAllResults() = {
    val engine = prepareEngine()
    val result = engine.evolvePopulation(parameters.populationSize, parameters.eliteCount, parameters.tc)
    result
  }

  /**
   * Przygotowuje silnik algorytmu ewolucyjnego
   *
   * @return obiekt typu EvolutionEngine
   */
  private def prepareEngine() = {
    val engine: EvolutionEngine[C] = new GenerationalEvolutionEngine[C](
      parameters.cf,
      parameters.eo,
      parameters.fe,
      parameters.ss,
      new JavaRNG())

    engine.addEvolutionObserver(new EvolutionObserver[C] {
      override def populationUpdate(data: PopulationData[_ <: C]): Unit = {
        if (verboseLevel.generationCount) println("Pokolenie nr " + data.getGenerationNumber)
        if (verboseLevel.distances) println("Najlepszy wynik: " + data.getBestCandidate.distances.toStringMM)
        if (verboseLevel.response) println("Najlepszy wynik (odpowiedź): " + data.getBestCandidate.lastResponse())
        if (verboseLevel.fitness) println("F. celu: " + data.getBestCandidateFitness)
      }
    })
    engine
  }
}

object EvolutionaryAlgorithm {
  /**
   * Typ osobnika
   */
  type C = AbstractCanalModel

  /**
   * Typ wejścia (wyniku pomiaru)
   */
  type I = CanalResponse

  type EO = EvolutionaryOperator[C]
  type CF = CandidateFactory[C]
  type FE = FitnessEvaluator[C]
  type SS = SelectionStrategy[_ >: Object]
  type TC = TerminationCondition
}

