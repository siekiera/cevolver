package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util
import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel
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
    // TODO:: zastanowić się, czy tego nie przerobić - zamiast odległości od 0 trzymać od ostatniego
    candidate.distances.distances.mapToSelf(new UnivariateFunction {
      override def value(x: Double): Double = {
        var r = x + randomCoeffScalar * rng.nextGaussian()
        if (r < 0) r = 0
        if (r > MeasurementParams.getTotalLength) r = MeasurementParams.getTotalLength
        r
      }
    })
    // zabezpieczenie na zamianę kolejności - TODO to chyba nie jest optymalne
    MatrixOps.sort(candidate.distances.distances)
    candidate
  }
}
