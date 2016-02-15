package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{AbstractCanalModel, CanalResponse, Distances}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa obliczająca poprawność modelu mikropaska
 * Data utworzenia: 11.02.16, 15:05
 * @author Michał Toporowski
 */
class ModelChecker(val modelProducer: (Distances, MicrostripParams) => AbstractCanalModel) {
  private val datasetIterator = DataHolder.getAvailableDataSets.iterator()
  private var _calculatedResponse: CanalResponse = null
  private var _externallyCalculatedResponse: CanalResponse = null

  def loadNext() = {
    val hasNext = datasetIterator.hasNext
    if (hasNext) {
      DataHolder.load(datasetIterator.next())
      testResponse()
    }
    hasNext
  }

  private def testResponse(): Unit = {
    val dists = DataHolder.getCurrent.expectedDistances
    _externallyCalculatedResponse = DataHolder.getCurrent.canalResponse
    val result = modelProducer(dists, DataHolder.getCurrent.measurementParams.getMicrostripParams)
    _calculatedResponse = result.response(DataHolder.getCurrent.measurementParams)
  }

  def errorAbs() = MatrixOps.absoluteErrorAbs(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def errorPhase() = MatrixOps.absoluteErrorPhase(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def relErrorAbs() = MatrixOps.relativeErrorAbs(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def relErrorPhase() = MatrixOps.relativeErrorPhase(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def calculatedResponse() = _calculatedResponse

  def externallyCalculatedResponse() = _externallyCalculatedResponse


}
