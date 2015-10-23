package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util
import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Prosta implementacja mutacji
 * Data utworzenia: 29.05.15, 19:08
 * @author Michał Toporowski
 */
class SimpleMutation extends EvolutionaryOperator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {

  /**
   * Współczynnik przez który mnożona jest wartość losowa
   */
  private lazy val randomCoeffScalar = 0.01 * MeasurementParams.getTotalLength

  override def apply(selectedCandidates: util.List[EvolutionaryAlgorithm.C], rng: Random): util.List[EvolutionaryAlgorithm.C] = {
    Conversions.scalaToJavaList(Conversions.javaToScalaList(selectedCandidates).map(c => mutate(c, rng)))
  }

  /**
   * Mutuje pojedynczego osobnika za pomocą funkcji Gaussa
   *
   * @param candidate
   * @param rng
   * @return
   */
  private def mutate(candidate: EvolutionaryAlgorithm.C, rng: Random): EvolutionaryAlgorithm.C = {
    // Na razie mutujemy Gaussianem każdy współczynnik
    // wartości są bardzo małe i czasem wchodzi w liczby ujemne
    // rozważyć zmianę reprezentacji lub uzależnienie współcz. mutacji od wartości liczb
    // na razie - mnożenie przez randomCoeffScalar
    candidate.distances.distances.mapToSelf(new UnivariateFunction {
      override def value(x: Double): Double = {
        var r = x + randomCoeffScalar * rng.nextGaussian()
        if (r < 0) r = 0
        r
      }
    })
    // Sprawdzamy, czy nie przekroczyło maxa
    val sum = MatrixOps.sum(candidate.distances.distances)
    val diff = MeasurementParams.getTotalLength - sum
    if (diff < 0) {
      // Na razie zmieniamy tylko ostatni
      candidate.distances.distances.setEntry(candidate.distances.distances.getDimension - 1,
        candidate.distances.last + diff)
    }
    candidate
  }
}
