package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.uncommons.maths.random.Probability
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Implementacja mutacji wykorzystująca funkcję Gaussa
 * Data utworzenia: 9.01.2016
 *
 * @param probability prawdopodobieństwo mutacji
 * @author Michał Toporowski
 */
class StandardGaussianMutation(private val probability: Probability, val standardDeviation: Double) extends BaseMutation(probability) {

  private lazy val minVal = 0.0 //MeasurementParams.getMinMicrostripLength

  /**
   * Mutuje pojedynczego osobnika za pomocą funkcji Gaussa
   *
   * @param candidate osobnik
   * @param rng generator liczb pseudolosowych
   * @return
   */
  override protected def mutate(candidate: EvolutionaryAlgorithm.C, rng: Random): EvolutionaryAlgorithm.C = {
    // Na razie mutujemy Gaussianem każdy współczynnik
    // wartości są bardzo małe i czasem wchodzi w liczby ujemne
    // rozważyć zmianę reprezentacji lub uzależnienie współcz. mutacji od wartości liczb
    // na razie - mnożenie przez randomCoeffScalar
    val newDists = candidate.distances.distances.map(new UnivariateFunction {
      override def value(x: Double): Double = {
        var r = x * (1 + standardDeviation * rng.nextGaussian())
        if (r < minVal) r = minVal
        r
      }
    })
    // Sprawdzamy, czy nie przekroczyło maxa
    val sum = MatrixOps.sum(newDists)
    val diff = DataHolder.getCurrent.measurementParams.getTotalLength - sum
    if (diff < 0) {
      // Na razie zmieniamy tylko ostatni TODO:: pewnie do zmiany
      newDists.setEntry(candidate.distances.distances.getDimension - 1,
        candidate.distances.last + diff)
    }
    candidate.createNew(new Distances(newDists))
  }
}
