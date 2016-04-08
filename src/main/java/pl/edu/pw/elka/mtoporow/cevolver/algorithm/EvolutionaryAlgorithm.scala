package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import java.lang.Double
import java.util.{ArrayList => JArrayList, Objects}

import org.uncommons.maths.random.MersenneTwisterRNG
import org.uncommons.watchmaker.framework._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm._
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.VerboseLevel
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.AsyncRunner
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.JavaVectorOps

/**
 * Klasa bazowa dla algorytmów ewolucyjnych
 * Data utworzenia: 15.04.15
 *
 * @author Michał Toporowski
 */
class EvolutionaryAlgorithm(private val parameters: InternalAlgorithmParams, private val verboseLevel: VerboseLevel) {
  private val rng = new MersenneTwisterRNG()
  private var progressWriteExecutor: AsyncRunner = null
  private val fitnessTrace = new JArrayList[Double]()
  private var engine: EvolutionEngine[C] = null
  private var lastResult: EvolutionResult = null
  private var lastGenerations = 0

  /**
   * Rozwiązuje problem zwracając najlepszego kandydata
   *
   * @return wynik
   */
  def solve(): C = {
    progressWriteExecutor = new AsyncRunner()
    val engine = prepareEngine()
    val result = engine.evolve(parameters.populationSize, parameters.eliteCount, parameters.tc)
    progressWriteExecutor.shutdown()
    result
  }

  /**
   * Rozwiązuje problem
   *
   * @return wszystkie wyniki
   */
  def solveWithAllResults() = {
    engine = prepareEngine()
    doSolve()
  }

  /**
   * Kontynuuje ewolucję w miejscu zakończonym przez solveWithAllResults
   *
   * @return
   */
  def continue(): EvolutionResult = {
    Objects.requireNonNull(engine, "Silnik nieustawiony!")
    doSolve()
  }

  /**
   * Rozwiązuje problem
   *
   * @return
   */
  private def doSolve(): EvolutionResult = {
    progressWriteExecutor = new AsyncRunner()
    val startMoment = System.currentTimeMillis()
    val population = if (lastResult == null) {
      engine.evolvePopulation(parameters.populationSize, parameters.eliteCount, parameters.tc)
    } else {
      engine.evolvePopulation(parameters.populationSize, parameters.eliteCount, JavaVectorOps.getCandidates(lastResult.population), parameters.tc)
    }
    val duration = System.currentTimeMillis() - startMoment
    progressWriteExecutor.shutdown()
    lastResult = new EvolutionResult(population, duration, fitnessTrace)
    lastGenerations = lastResult.generationCount
    lastResult
  }

  /**
   * Przygotowuje silnik algorytmu ewolucyjnego
   *
   * @return obiekt typu EvolutionEngine
   */
  private def prepareEngine() = {
    engine = if (parameters.es != null) {
      new EvolutionStrategyEngine[C](
        parameters.cf,
        parameters.eo,
        parameters.fe,
        parameters.es.plusSelection,
        parameters.es.offspringModifier,
        rng
      )
    } else {
      new GenerationalEvolutionEngine[C](
        parameters.cf,
        parameters.eo,
        parameters.fe,
        parameters.ss,
        rng)
    }

    engine.addEvolutionObserver(new EvolutionObserver[C] {
      override def populationUpdate(data: PopulationData[_ <: C]): Unit = {
        progressWriteExecutor.execute(() => {
          if (verboseLevel.generationCount) println("Pokolenie nr " + (lastGenerations + data.getGenerationNumber))
          if (verboseLevel.distances) println("Najlepszy wynik: " + data.getBestCandidate.distances.toStringMM)
          if (verboseLevel.response) println("Najlepszy wynik (odpowiedź): " + data.getBestCandidate.lastResponse())
          if (verboseLevel.fitness) println("F. celu: " + data.getBestCandidateFitness)
          fitnessTrace.add(data.getBestCandidateFitness)
        })
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

