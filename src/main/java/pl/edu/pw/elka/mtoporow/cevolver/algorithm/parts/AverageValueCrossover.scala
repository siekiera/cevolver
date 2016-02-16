package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.uncommons.maths.random.Probability

/**
 * Krzyżowanie uśredniające
 * Data utworzenia: 09.01.16, 17:03
 * @author Michał Toporowski
 */
class AverageValueCrossover(private val probability: Probability) extends BaseCrossover(new DoubleAverageCO(probability), false)

private class DoubleAverageCO(private val probability: Probability) extends BaseDoubleCrossover(probability) {
  override def mateOne(parent1: Array[Double], parent2: Array[Double], numberOfCrossoverPoints: Int, rng: Random): Array[Double] = {
    val child1 = new Array[Double](parent1.length)
    val random = rng.nextDouble()
    for (i <- 0 until parent1.length) {
      child1(i) = random * parent1(i) + (1 - random) * parent2(i)
    }
    child1
  }
}
