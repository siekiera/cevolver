package pl.edu.pw.elka.mtoporow.cevolver.util

import java.io.File

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.ext.chart.ChartData
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.{MicrostripLineModel, ModelChecker}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps
import pl.edu.pw.elka.mtoporow.cevolver.util.export.SerializationUtil

/**
 * Narzędzie produkujące wyniki sprawdzania modelu
 * Data utworzenia: 11.02.16, 15:30
 * @author Michał Toporowski
 */
object ModelDataProducer {

  def main(args: Array[String]) {
    val dir = GeneralConstants.OUTPUT_DIR
    val checker = new ModelChecker((d, p) => new MicrostripLineModel(d, p))
    while (checker.loadNext()) {
      val datasetId = DataHolder.getCurrentId

      val cd = newDataForFreqs("Wykres amplitud zestawu nr " + DataHolder.getCurrentId)
      cd.addSeries("Amplitudy obliczone w modelu", MatrixOps.asIterable(checker.calculatedResponse().value).map(c => c.abs()).toArray)
      cd.addSeries("Amplitudy wzorcowe", MatrixOps.asIterable(checker.externallyCalculatedResponse().value).map(c => c.abs()).toArray)
      SerializationUtil.serialize(new File(dir, datasetId + "_amplitudy.dat"), cd)

      val cdPhase = newDataForFreqs("Wykres faz zestawu nr " + DataHolder.getCurrentId)
      cdPhase.addSeries("Fazy obliczone w modelu", MatrixOps.asIterable(checker.calculatedResponse().value).map(c => c.getArgument).toArray)
      cdPhase.addSeries("Fazy wzorcowe", MatrixOps.asIterable(checker.externallyCalculatedResponse().value).map(c => c.getArgument).toArray)
      SerializationUtil.serialize(new File(dir, datasetId + "_fazy.dat"), cdPhase)

      val cdAbsError = newDataForFreqs("Wykres błędów na amplitudach zestawu nr " + DataHolder.getCurrentId)
      cdAbsError.addSeries("Błąd bezwzględny na amplitudach", checker.errorAbs())
      SerializationUtil.serialize(new File(dir, datasetId + "_błąd_amp.dat"), cdAbsError)

      val cdPhaseError = newDataForFreqs("Wykres błędów na fazach zestawu nr")
      cdPhaseError.addSeries("Błąd bezwzględny na fazach", checker.errorPhase())
      SerializationUtil.serialize(new File(dir, datasetId + "_błąd_faz.dat"), cdPhaseError)

      val cdRelAbsError = newDataForFreqs("Wykres błędów na amplitudach zestawu nr " + DataHolder.getCurrentId)
      cdRelAbsError.addSeries("Błąd względny na amplitudach", checker.relErrorAbs())
      SerializationUtil.serialize(new File(dir, datasetId + "_błąd_w_amp.dat"), cdRelAbsError)

      val cdRelPhaseError = newDataForFreqs("Wykres błędów na fazach zestawu nr")
      cdRelPhaseError.addSeries("Błąd względny na fazach", checker.relErrorPhase())
      SerializationUtil.serialize(new File(dir, datasetId + "_błąd_w_faz.dat"), cdRelPhaseError)


    }
  }

  private def newDataForFreqs(name: String) = new ChartData(name, "Częstotliwości", DataHolder.getCurrent.measurementParams.getFrequencies.toArray)

}
