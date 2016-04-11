package pl.edu.pw.elka.mtoporow.cevolver.ext.diag

import org.uncommons.watchmaker.framework.EvaluatedCandidate
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.util.Conversions
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.{EvolutionResult, EvolutionaryAlgorithm}
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.Cell
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.Distances
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Statystyki populacji wynikowej
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
  @Cell("best 25% candidates' fitness")
  val best25Fitness = MatrixOps.avg(fitnesses.slice(0, fitnesses.size / 4))
  
  val lengthsDiffs = rowStatistics.map(_.lengthsDiff)
  @Cell("average vector square distance from expected")
  val avgLengthsDiff = MatrixOps.avg(lengthsDiffs)
  @Cell("best 10% average vector square distance from expected")
  val best10PLengthsDiff = MatrixOps.sliceAvg(lengthsDiffs, rowStatistics.size / 10)
  @Cell("best 25% average vector square distance from expected")
  val best25PLengthsDiff = MatrixOps.sliceAvg(lengthsDiffs, rowStatistics.size / 4)
  @Cell("best 10 average vector square distance from expected")
  val best10LengthsDiff = MatrixOps.sliceAvg(lengthsDiffs, 10)

  val relativeErrors = rowStatistics.map(_.avgRelativeError)
  @Cell("average relative error on lengths")
  val avgRelativeError = MatrixOps.avg(relativeErrors)
  @Cell("best 10% average relative error on lengths")
  val best10PRelativeError = MatrixOps.sliceAvg(relativeErrors, rowStatistics.size / 10)
  @Cell("best 25% relative error on lengths")
  val best25PRelativeError = MatrixOps.sliceAvg(relativeErrors, rowStatistics.size / 4)
  @Cell("best 10 average relative error on lengths")
  val best10RelativeError = MatrixOps.sliceAvg(relativeErrors, 10)

  @Cell("execution time [s]")
  val executionTime = evolutionResult.durationSec

  def calcRowStatistics(candidate: EVC): RowStatistics = new RowStatistics(candidate, DataHolder.getCurrent.expectedDistances)

  class RowStatistics(candidate: EVC, expDists: Distances) {
    @Cell("distances")
    val distances = candidate.getCandidate.distances.distances
    @Cell("fitness")
    val fitness = candidate.getFitness
    @Cell("vector square distance from expected lengths")
    val lengthsDiff = candidate.getCandidate.lengths.getDistance(expDists.lengths)
    @Cell("vector L1 distance from expected lengths")
    val lengthsL1Diff = candidate.getCandidate.lengths.getL1Distance(expDists.lengths)
    @Cell("relative error %")
    val relativeError = MatrixOps.relativeErrorPercentage(candidate.getCandidate.lengths, expDists.lengths)
    @Cell("average relative error %")
    val avgRelativeError = MatrixOps.avg(relativeError)
  }

}
