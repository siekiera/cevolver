package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.param.MeasurementParams
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{Data, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Prosta fabryka osobników
 * Data utworzenia: 29.05.15, 19:04
 * @author Michał Toporowski
 */
class SimpleCandidateFactory extends AbstractCandidateFactory[EvolutionaryAlgorithm.C] with Data[EvolutionaryAlgorithm.I] {
  override def generateRandomCandidate(rng: Random): EvolutionaryAlgorithm.C = {
    // Tworzymy N+1 liczb losowych i skalujemy je tak, żeby ich suma wynosiła długość kabla
    val randomCoeffs = MatrixOps.asSums(MatrixOps.randomRealVector(rng, MeasurementParams.getDiscontinuitiesCount + 1))
    val distances = randomCoeffs.mapMultiply(MeasurementParams.getTotalLength / randomCoeffs.getEntry(randomCoeffs.getDimension - 1))
    new MicrostripLineModel(new Distances(distances), MeasurementParams.getMicrostripParams)
  }
}
