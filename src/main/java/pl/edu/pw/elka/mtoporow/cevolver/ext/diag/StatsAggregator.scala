package pl.edu.pw.elka.mtoporow.cevolver.ext.diag

import java.io.File

import org.apache.commons.math3.linear.MatrixUtils
import pl.edu.pw.elka.mtoporow.cevolver.cli.export.{Exporter, FileRowWriter, RowWriter}
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps

import scala.io.Source

/**
 * Agregator plików ze statystykami
 *
 * Wywołanie: StatsAggregator &lt;katalog&gt; [plik1] [plik2]
 *
 * Założenie: struktura jest następująca:
 * <ul><li> katalog <ul>
 * <li> podkatalog 1 <ul><li> plik1 </li><li> plik2 </li></ul></li>
 * <li> podkatalog 2 <ul><li> plik1 </li><li> plik2 </li></ul></li>
 * </ul></li></ul>
 *
 * Data utworzenia: 02.05.16, 12:08
 * @author Michał Toporowski
 */
object StatsAggregator {

  def main(args: Array[String]) {
    if (args.length < 2) throw new IllegalArgumentException("args.length < 2!")
    processDir(new File(args(0)), args.drop(1))
  }

  private def processDir(rootDir: File, fileNames: Array[String]): Unit = {
    val totalAgrFile = new File(rootDir, "total_agr.csv")
    val totalWriter = new FileRowWriter(totalAgrFile)
    rootDir.listFiles().filter(_.isDirectory).foreach(f =>
      fileNames.foreach(n => processFile(f, n, totalWriter))
    )
    totalWriter.finish()
    println(s"Zapisano ${totalAgrFile.getPath}")
  }

  private def processFile(dir: File, filename: String, totalWriter: RowWriter): Unit = {
    val file = new File(dir, filename)
    val src = Source.fromFile(file)
    // Odczytanie danych z CSV-ki do macierzy
    val data = src.getLines().drop(1).map(s => s.split(";").map(_.replace("\"", "").toDouble)).toArray
    val matrix = MatrixUtils.createRealMatrix(data)

    // Agregacja - do macierzy zawierającej średnią, max i min
    val aggregates = (0 until matrix.getColumnDimension).map(matrix.getColumn).map(c => Array(MatrixOps.avg(c), c.max, c.min)).toArray
    val aggregatedData = MatrixUtils.createRealMatrix(aggregates).transpose().getData

    // Zapis do pliku {nazwa}_agr.{roz}
    val outFile = new File(file.getParentFile, file.getName.replace(".", "_agr."))
    Exporter.serialize(outFile, aggregatedData)
    println(s"Zapisano ${outFile.getPath}")

    // Zapis do pliku z całością
    val f = aggregatedData.flatten
    totalWriter.yieldRow(Array(dir.getName) ++ f)
  }
}
