package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.FixedWidthLineModel
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.{JavaVectorOps, MatrixOps}

/**
 * Fabryka osobników typu FixedWidthLineModel
 * Data utworzenia: 29.05.15, 19:04
 * @author Michał Toporowski
 */
class FixedWidthCandidateFactory(val breakCount: Int) extends BaseCandidateFactory {
  private val measurementParams = DataHolder.getCurrent.measurementParams

  override def generateRandomCandidate(rng: Random): EvolutionaryAlgorithm.C = {
    // Tworzymy N+1 liczb losowych i skalujemy je tak, żeby ich suma wynosiła długość kabla
    val randomCoeffs = JavaVectorOps.randomRealVector(rng, breakCount + 1, 0, 1)
    val sum = MatrixOps.sum(randomCoeffs)
    val distances = randomCoeffs.mapMultiply(measurementParams.getTotalLength / sum)
    // do modelu wrzucamy wektor N-elementowy
    val modelDistances = new Distances(MatrixOps.dropLast(distances))
    new FixedWidthLineModel(modelDistances, measurementParams.getMicrostripParams)
  }

  override def traitCount(): Int = breakCount
}
