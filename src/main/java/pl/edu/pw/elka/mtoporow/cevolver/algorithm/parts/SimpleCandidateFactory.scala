package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.apache.commons.math3.linear.ArrayRealVector
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.{MicrostripParams, MicrostripLineModel}

/**
 * Prosta fabryka osobników
 * Data utworzenia: 29.05.15, 19:04
 * @author Michał Toporowski
 */
class SimpleCandidateFactory extends AbstractCandidateFactory[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def generateRandomCandidate(rng: Random): EvolutionaryAlgorithm.C = {
    // TODO:: zaimplementować
    new MicrostripLineModel(new Distances(new ArrayRealVector(2)), new MicrostripParams)
  }
}
