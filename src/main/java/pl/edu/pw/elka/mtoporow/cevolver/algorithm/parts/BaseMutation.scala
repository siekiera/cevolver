package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util
import java.util.Random

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.EvolutionaryOperator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}

/**
 * Klasa bazowa dla operatorów mutacji
 * Data utworzenia: 09.01.16, 16:00
 * @author Michał Toporowski
 */
abstract class BaseMutation(private val probability: Probability) extends EvolutionaryOperator[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {

  override def apply(selectedCandidates: util.List[EvolutionaryAlgorithm.C], rng: Random): util.List[EvolutionaryAlgorithm.C] = {
    Conversions.scalaToJavaList(Conversions.javaToScalaList(selectedCandidates).map(c => mutateWithProbability(c, rng)))
  }

  /**
   * Mutuje pojedynczego osobnika z prawdopodobieństwem probability
   *
   * @param candidate osobnik
   * @param rng generator liczb pseudolosowych
   * @return
   */
  private def mutateWithProbability(candidate: EvolutionaryAlgorithm.C, rng: Random) = {
    if (probability.doubleValue() > rng.nextDouble()) {
      mutate(candidate, rng)
    } else {
      candidate
    }
  }

  /**
   * Mutuje pojedyńczy osobnik za pomocą funkcji Gaussa
   *
   * @param candidate osobnik
   * @param rng generator liczb pseudolosowych
   * @return
   */
  protected def mutate(candidate: EvolutionaryAlgorithm.C, rng: Random): EvolutionaryAlgorithm.C

}
