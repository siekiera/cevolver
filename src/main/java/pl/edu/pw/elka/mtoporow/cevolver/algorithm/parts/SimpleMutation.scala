package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util
import java.util.Random

import org.uncommons.watchmaker.framework.EvolutionaryOperator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}

/**
 * Prosta implementacja mutacji
 * Data utworzenia: 29.05.15, 19:08
 * @author Michał Toporowski
 */
class SimpleMutation extends EvolutionaryOperator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def apply(selectedCandidates: util.List[EvolutionaryAlgorithm.C], rng: Random): util.List[EvolutionaryAlgorithm.C] = {
    Conversions.scalaToJavaList(Conversions.javaToScalaList(selectedCandidates).map(c => mutate(c, rng)))
  }

  private def mutate(candidate: EvolutionaryAlgorithm.C, rng: Random): EvolutionaryAlgorithm.C = {
    // Na razie mutujemy Gaussianem każdy współczynnik
    candidate.distances.distances.mapAddToSelf(rng.nextGaussian())
    candidate
  }
}
