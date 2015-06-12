package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import java.util
import java.util.Random

import org.uncommons.watchmaker.framework.CandidateFactory

/**
 * Klasa X
 * Data utworzenia: 06.05.15, 21:03
 * @author Michał Toporowski
 */
@Deprecated
class MicrostripLineModelFactory extends CandidateFactory[MicrostripLineModel] {
  override def generateInitialPopulation(populationSize: Int, rng: Random): util.List[MicrostripLineModel] = ???

  override def generateInitialPopulation(populationSize: Int, seedCandidates: util.Collection[MicrostripLineModel], rng: Random): util.List[MicrostripLineModel] = ???

  override def generateRandomCandidate(rng: Random): MicrostripLineModel = ???
}
