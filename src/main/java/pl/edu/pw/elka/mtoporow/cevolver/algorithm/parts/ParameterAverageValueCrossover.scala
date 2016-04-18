package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.apache.commons.math3.linear.RealVector
import org.uncommons.maths.random.Probability
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa uśredniające po parametrach
 * Data utworzenia: 16.02.16, 11:04
 * @author Michał Toporowski
 */
class ParameterAverageValueCrossover(probability: Probability) extends SymmetricCrossover(probability) {
  override protected def mateOne(parent1: RealVector, parent2: RealVector, numberOfCrossoverPoints: Int, rng: Random): RealVector = {
    MatrixOps.combine(parent1, parent2, (d1, d2) => {
      val random = rng.nextDouble()
      random * d1 + (1 - random) * d2
    })
  }
}
