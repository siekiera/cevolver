package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}

/**
 * Prosta fabryka osobników
 * Data utworzenia: 29.05.15, 19:04
 * @author Michał Toporowski
 */
class SimpleCandidateFactory extends AbstractCandidateFactory[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def generateRandomCandidate(rng: Random): EvolutionaryAlgorithm.C = ???
}
