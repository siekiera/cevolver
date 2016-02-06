package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.MicrostripLineModel
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Prosta fabryka osobników
 * Data utworzenia: 29.05.15, 19:04
 * @author Michał Toporowski
 */
class SimpleCandidateFactory extends AbstractCandidateFactory[EvolutionaryAlgorithm.C] {
  private val measurementParams = DataHolder.getCurrent.measurementParams

  override def generateRandomCandidate(rng: Random): EvolutionaryAlgorithm.C = {
    // Tworzymy N+1 liczb losowych i skalujemy je tak, żeby ich suma wynosiła długość kabla
    val randomCoeffs = MatrixOps.randomRealVector(rng, measurementParams.getDiscontinuitiesCount + 1)
    val sum = MatrixOps.sum(randomCoeffs)
    val distances = randomCoeffs.mapMultiply(measurementParams.getTotalLength / sum)
    // do modelu wrzucamy wektor N-elementowy
    val modelDistances = new Distances(MatrixOps.dropLast(distances))
    new MicrostripLineModel(modelDistances, measurementParams.getMicrostripParams)
  }
}
