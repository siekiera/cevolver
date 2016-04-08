//package pl.edu.pw.elka.mtoporow.cevolver.cli.export
//
//import java.util
//
//import pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix.MatrixOps
//import java.io._
//import java.util.Collection
//import java.util.function.Consumer
//
///**
// * Klasa eksportująca kolekcje do plików
// * Data utworzenia: 07.02.16, 20:07
// *
// * @author Michał Toporowski
// */
//class Exporter(private final val ) extends Closeable {
//  private final val writer: Writer = null
//
////  @throws(classOf[IOException])
////  private def this(file: File) = {
////    writer = new FileWriter(file)
////  }
//
////@throws(classOf[IOException])
////private def this()= {
////this()
////this.writer = new OutputStreamWriter(System.out)
////}
//
///**
// * Dodaje linię do pliku
// *
// * @param line linię
// * @throws IOException
// */
//@throws(classOf[IOException])
//private def appendLine(line: String)= {
//writer.append(line).append("\n")
//}
//
//@throws(classOf[IOException])
//def close ={
//writer.flush
//writer.close
//}
//}
//
//object Exporter {
//  /**
//   * Eksportuje kolekcję do pliku wykorzystując metodę toString
//   *
//   * @param file plik
//   * @param c    kolekcja
//   * @throws IOException
//   */
////  @throws(classOf[IOException])
////  def serialize(file: File, c: util.Collection[Object]) {
////    try {
////      val exporter: Exporter = new Exporter(file)
////      try {
////        for (obj <- c) {
////          exporter.appendLine(obj.toString)
////        }
////      } finally {
////        if (exporter != null) exporter.close()
////      }
////    }
////  }
//
//  /**
//   * Eksportuje macierz do pliku
//   *
//   * @param file   plik
//   * @param matrix macierz
//   * @throws IOException
//   */
//  @throws(classOf[IOException])
//  def serialize(file: File, matrix: Array[Array[Double]]) {
//    try {
//      val exporter: Exporter = new Exporter(file)
//      try {
//        for (row <- matrix) {
//          exporter.appendLine(MatrixOps.mkString(row, ";"))
//        }
//      } finally {
//        if (exporter != null) exporter.close()
//      }
//    }
//  }
//
//  @throws(classOf[IOException])
//  def serialize(file: File, consumer: Consumer[Exporter]) {
//    try {
//      val exporter: Exporter = new Exporter(file)
//      try {
//        consumer.accept(exporter)
//      } finally {
//        if (exporter != null) exporter.close()
//      }
//    }
//  }
//
//
//}
//
