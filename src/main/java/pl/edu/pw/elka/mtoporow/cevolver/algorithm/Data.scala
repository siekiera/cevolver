package pl.edu.pw.elka.mtoporow.cevolver.algorithm

/**
 * Klasa reprezentująca część algorytmu zależną od danych
 * Data utworzenia: 12.06.15, 17:21
 * @author Michał Toporowski
 */
trait Data[I] {
  // TODO:: to trzeba wywalić, albo przerobić, dane i tak są przechowywane statycznie
  var data: I = _
}
