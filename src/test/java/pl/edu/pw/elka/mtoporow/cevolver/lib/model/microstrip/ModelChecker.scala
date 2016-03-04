package pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip

import java.util

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.CanalResponse
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

/**
 * Klasa obliczająca poprawność modelu mikropaska
 * Data utworzenia: 11.02.16, 15:05
 * @author Michał Toporowski
 */
class ModelChecker private(val datasetIterator: util.Iterator[String]) {
  private var _calculatedResponse: CanalResponse = null
  private var _externallyCalculatedResponse: CanalResponse = null

  /**
   * Tworzy instancję klasy dla wszystkich zestawów danych
   *
   * @return instancja
   */
  def this() = this(DataHolder.getAvailableDataSets.iterator())

  /**
   * Tworzy instancję klasy dla zestawów danych o nazwach pasujących do wyrażenia regularnego
   *
   * @param regex wyrażenie reg.
   * @return
   */
  def this(regex: String) = this(DataHolder.getMatchingDataSets(regex))

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
    val result = MicrostripLineModelFactory.newModel(dists)
    _calculatedResponse = result.response(DataHolder.getCurrent.measurementParams)
  }

  def errorAbs() = MatrixOps.absoluteErrorAbs(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def errorPhase() = MatrixOps.absoluteErrorPhase(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def relErrorAbs() = MatrixOps.relativeErrorAbs(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def relErrorPhase() = MatrixOps.relativeErrorPhase(_calculatedResponse.value, _externallyCalculatedResponse.value)

  def calculatedResponse() = _calculatedResponse

  def externallyCalculatedResponse() = _externallyCalculatedResponse


}
