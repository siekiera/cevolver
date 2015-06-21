package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util

import org.apache.commons.math3.complex.Complex
import org.uncommons.watchmaker.framework.FitnessEvaluator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Funkcja celu
 *
 * Data utworzenia: 29.05.15, 20:15
 * @author Michał Toporowski
 */
class SimpleFitnessEvaluator extends FitnessEvaluator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def getFitness(candidate: EvolutionaryAlgorithm.C, population: util.List[_ <: EvolutionaryAlgorithm.C]): Double = {
    // TODO:: zaimplementować
    val candResponse = candidate.response()
    val diff = candResponse.value.subtract(data.value)
    MatrixOps.reduceComplexVector(diff, (a: Complex, b: Complex) => sqrAbs(a) + sqrAbs(b))
  }

  override def isNatural: Boolean = false

  private def sqrAbs(z: Complex) = {
    val abs = z.abs()
    abs * abs
  }
}
