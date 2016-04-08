package pl.edu.pw.elka.mtoporow.cevolver.ext.diag

import org.uncommons.watchmaker.framework.EvaluatedCandidate
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{EvolutionResult, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Cell
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa X
 * Data utworzenia: 04.04.16, 12:10
 * @author Michał Toporowski
 */
class PopulationStatistics(evolutionResult: EvolutionResult) {
  type EVC = EvaluatedCandidate[EvolutionaryAlgorithm.C]
  val population = Conversions.javaToScalaList(evolutionResult.population)
  val rowStatistics = population.map(calcRowStatistics)
  val fitnesses = population.map(evc => evc.getFitness)
  @Cell("average fitness")
  val avgFitness = MatrixOps.avg(fitnesses)
  @Cell("best 10% candidates' fitness")
  val best10Fitness = MatrixOps.avg(fitnesses.slice(0, fitnesses.size / 10))


  def calcRowStatistics(candidate: EVC): RowStatistics = new RowStatistics(candidate)

  class RowStatistics(candidate: EVC) {
    @Cell("distances")
    def distances = candidate.getCandidate.distances.distances
    @Cell("fitness")
    def fitness = candidate.getFitness

  }

}
