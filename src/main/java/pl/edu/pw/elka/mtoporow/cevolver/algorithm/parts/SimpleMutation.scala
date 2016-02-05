package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.uncommons.maths.random.Probability
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Prosta implementacja mutacji
 * Data utworzenia: 29.05.15, 19:08
 *
 * @param probability prawdopodobieństwo mutacji
 * @author Michał Toporowski
 */
class SimpleMutation(private val probability: Probability) extends BaseMutation(probability) {

  /**
   * Współczynnik przez który mnożona jest wartość losowa
   */
  private lazy val randomCoeffScalar = 0.01 * DataHolder.getCurrent.measurementParams.getTotalLength
  private lazy val minVal = DataHolder.getCurrent.measurementParams.getMinMicrostripLength

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
        var r = x + randomCoeffScalar * rng.nextGaussian()
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
