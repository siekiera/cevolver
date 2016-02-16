package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util.Random

import org.uncommons.maths.random.Probability

/**
 * Klasa uśredniające po parametrach
 * Data utworzenia: 16.02.16, 11:04
 * @author Michał Toporowski
 */
class ParameterAverageValueCrossover(private val probability: Probability) extends BaseCrossover(new DoubleParAvgCO(probability), false)

private class DoubleParAvgCO(private val probability: Probability) extends BaseDoubleCrossover(probability) {
  override def mateOne(parent1: Array[Double], parent2: Array[Double], numberOfCrossoverPoints: Int, rng: Random): Array[Double] = {
    val child1 = new Array[Double](parent1.length)
    for (i <- 0 until parent1.length) {
      val random = rng.nextDouble()
      child1(i) = random * parent1(i) + (1 - random) * parent2(i)
    }
    child1
  }
}
