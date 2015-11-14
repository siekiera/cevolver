package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util

import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear.RealVector
import org.uncommons.watchmaker.framework.FitnessEvaluator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Funkcja celu
 *
 * Data utworzenia: 29.05.15, 20:15
 * @author Michał Toporowski
 */
class SimpleFitnessEvaluator(val punishmentRatio: Double) extends FitnessEvaluator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def getFitness(candidate: EvolutionaryAlgorithm.C, population: util.List[_ <: EvolutionaryAlgorithm.C]): Double = {
    // TODO:: zaimplementować
    // TODO:: czy to na pewno jest dobrze?
    val candResponse = candidate.response()
    val diff = candResponse.value.subtract(data.value)
    val fitness = diff.toArray.map(sqrAbs).sum
    if (punishmentRatio <= 0.0)
      fitness
    else
      fitness + punishment(candidate.distances.distances)
  }

  override def isNatural: Boolean = false

  private def sqrAbs(z: Complex) = {
    val abs = z.abs()
    abs * abs
  }

  /**
   * Funkcja kary
   *
   * @param distances wektor odległości
   * @return
   */
  private def punishment(distances: RealVector) = {
    val minVal = MeasurementParams.getMinMicrostripLength
    var p = 0.0
    // Jeśli różnica pomiędzy kolejnymi odległościami jest mniejsza od minVal - karamy
    // Jeśli kolejność się odwróci, również będzie kara
    for (i <- MatrixOps.doubleIterable(distances)) {
      if (i < minVal) {
        p += minVal - i
      }
    }
//    println(p * punishmentRatio)
    p * punishmentRatio
  }
}
