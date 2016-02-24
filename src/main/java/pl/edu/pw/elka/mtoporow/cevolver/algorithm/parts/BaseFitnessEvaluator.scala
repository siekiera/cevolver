package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts

import java.util

import org.apache.commons.math3.complex.Complex
import org.uncommons.watchmaker.framework.FitnessEvaluator
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.EvolutionaryAlgorithm.C
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Część licząca przystosowanie osobników
 *
 * Data utworzenia: 29.05.15, 20:15
 * @author Michał Toporowski
 */
class BaseFitnessEvaluator(val punishmentRatio: Double, val punishmentFunction: PunishmentFunction) extends FitnessEvaluator[EvolutionaryAlgorithm.C] {
  private val data = DataHolder.getCurrent.canalResponse

  override def getFitness(candidate: EvolutionaryAlgorithm.C, population: util.List[_ <: EvolutionaryAlgorithm.C]): Double = {
    val fitness = FitnessFunction.apply(candidate, data, punishmentRatio)
    if (punishmentFunction == null || punishmentRatio <= 0.0)
      fitness
    else
      fitness + punishmentRatio * punishmentFunction(candidate, data)
  }

  override def isNatural: Boolean = false
}

/**
 * Funkcja kary
 */
trait PunishmentFunction {
  def apply(candidate: EvolutionaryAlgorithm.C, realResp: CanalResponse): Double
}

/**
 * Funkcja celu
 */
object FitnessFunction {
  def apply(candidate: EvolutionaryAlgorithm.C, realResp: CanalResponse, punishmentRatio: Double): Double = {
    val candResponse = candidate.response(DataHolder.getCurrent.measurementParams)
    val diff = candResponse.value.subtract(realResp.value)
    val fitness = MatrixOps.asIterable(diff).map(sqrAbs).sum
    fitness
  }

  private def sqrAbs(z: Complex) = {
    val abs = z.abs()
    abs * abs
  }
}

/**
 * Domyślna f. kary, karająca za d < 0
 */
object DefaultPF extends PunishmentFunction {

  /**
   * Funkcja kary
   * i
   * @return
   */
  def apply(candidate: EvolutionaryAlgorithm.C, realResp: CanalResponse): Double = {
    val minVal = 0.0 //MeasurementParams.getMinMicrostripLength
    var p = 0.0
    // Jeśli różnica pomiędzy kolejnymi odległościami jest mniejsza od minVal - karamy
    // Jeśli kolejność się odwróci, również będzie kara
    for (i <- MatrixOps.asIterable(candidate.distances.distances)) {
      if (i < minVal) {
        p += minVal - i
      }
    }
    p
  }
}

/**
 * Funkcja kary uzależnona od fazy
 */
object PhaseDependentPF extends PunishmentFunction {
  override def apply(candidate: C, realResp: CanalResponse): Double = {
    MatrixOps.avg(MatrixOps.compare(candidate.lastResponse().value, realResp.value, compareSqr))
  }

  private def compareSqr(x: Complex, y: Complex): Double = {
    val diff = x.getArgument - y.getArgument
    diff * diff
  }
}