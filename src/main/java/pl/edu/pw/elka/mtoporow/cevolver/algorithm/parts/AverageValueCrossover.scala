package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util
import java.util.Random

import org.uncommons.maths.random.Probability
import org.uncommons.watchmaker.framework.operators.AbstractCrossover

/**
 * Krzyżowanie uśredniające
 * Data utworzenia: 09.01.16, 17:03
 * @author Michał Toporowski
 */
class AverageValueCrossover(private val probability: Probability) extends BaseCrossover(new DoubleAverageCO(probability), false)

private class DoubleAverageCO(private val probability: Probability) extends AbstractCrossover[Array[Double]](1, probability) {
  override def mate(parent1: Array[Double], parent2: Array[Double], numberOfCrossoverPoints: Int, rng: Random): util.List[Array[Double]] = {
    val child1 = new Array[Double](parent1.length)
    for (i <- 0 to parent1.length) {
      val random = rng.nextDouble()
      child1(i) = random * parent1(i) + (1 - random) * parent2(i)
    }
    util.Arrays.asList(child1)
  }
}
