package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util

import org.uncommons.watchmaker.framework.FitnessEvaluator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}

/**
 * Klasa X
 * Data utworzenia: 29.05.15, 20:15
 * @author Michał Toporowski
 */
class SimpleFitnessEvaluator extends FitnessEvaluator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def getFitness(candidate: EvolutionaryAlgorithm.C, population: util.List[_ <: EvolutionaryAlgorithm.C]): Double = {
    // TODO:: zaimplementować
    0.0
  }

  override def isNatural: Boolean = false
}
