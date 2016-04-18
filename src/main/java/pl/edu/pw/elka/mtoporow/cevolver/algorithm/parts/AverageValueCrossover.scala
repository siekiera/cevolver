package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.apache.commons.math3.linear.RealVector
import org.uncommons.maths.random.Probability

/**
 * Krzyżowanie uśredniające
 * Data utworzenia: 09.01.16, 17:03
 * @author Michał Toporowski
 */
class AverageValueCrossover(probability: Probability) extends SymmetricCrossover(probability) {
  override protected def mateOne(parent1: RealVector, parent2: RealVector, numberOfCrossoverPoints: Int, rng: Random): RealVector = {
    val random = rng.nextDouble()
    parent1.combine(random, 1 - random, parent2)
  }
}
