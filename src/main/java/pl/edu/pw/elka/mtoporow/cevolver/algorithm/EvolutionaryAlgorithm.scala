package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import org.uncommons.maths.random.JavaRNG
import org.uncommons.watchmaker.framework._
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse}

/**
 * Klasa bazowa dla algorytmów ewolucyjnych
 * Data utworzenia: 15.04.15
 *
 * @author Michał Toporowski
 */
class EvolutionaryAlgorithm {

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

  /**
   * Parametry algorytmu
   */
  var parameters: InternalAlgorithmParams = null

  /**
   * Rozwiązuje problem
   *
   * @param data dane wejściowe
   * @return wynik
   */
  def solve(data: I): C = {
    parameters.setData(data)
    val engine: EvolutionEngine[C] = new GenerationalEvolutionEngine[C](
      parameters.cf,
      parameters.eo,
      parameters.fe,
      parameters.ss,
      new JavaRNG())

    engine.addEvolutionObserver(new EvolutionObserver[C] {
      override def populationUpdate(data: PopulationData[_ <: C]): Unit = println("Pokolenie nr " + data.getGenerationNumber)
    })

    // TODO jest też engine.evolvePopulation() - do rozważenia
    val result = engine.evolve(parameters.populationSize, parameters.eliteCount, parameters.tc)

    result
  }
}

object EvolutionaryAlgorithm extends EvolutionaryAlgorithm

