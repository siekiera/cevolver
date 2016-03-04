package pl.edu.pw.elka.mtoporow.cevolver.util

import java.io.File

import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder
import pl.edu.pw.elka.mtoporow.cevolver.ext.chart.ChartData
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.microstrip.ModelChecker
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps
import pl.edu.pw.elka.mtoporow.cevolver.util.export.SerializationUtil

/**
 * Narzędzie produkujące wyniki sprawdzania modelu
 * Data utworzenia: 11.02.16, 15:30
 *
 * Sposób uruchamiania:
 * ModelDataProducer -> dla wszystkich zestawów
 * ModelDataProducer [regex] -> dla zestawów spełniających wyrażenie
 *
 * @author Michał Toporowski
 */
object ModelDataProducer {

  def main(args: Array[String]): Unit = {
    val checker = if (args.isEmpty) new ModelChecker() else new ModelChecker(args(0))
    produce(checker)
  }


  private def produce(checker: ModelChecker) {
    val dir = GeneralConstants.OUTPUT_DIR
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

      println(s"Utworzono dane dla zestawu $datasetId")
    }
  }

  private def newDataForFreqs(name: String) = new ChartData(name, "Częstotliwości", DataHolder.getCurrent.measurementParams.getFrequencies.toArray)

}
