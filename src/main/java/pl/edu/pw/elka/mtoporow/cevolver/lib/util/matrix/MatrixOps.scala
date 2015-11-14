package pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix

import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear._

/**
 * Zawiera operacje na macierzach i wektorach
 * Data utworzenia: 21.06.15, 10:09
 * @author Michał Toporowski
 */
object MatrixOps {
  // TODO:: może warto zrobić refactor do ExtMatrix, ExtVector
  /**
   * Tworzy wektor liczb zespolonych z wektorów Re oraz Im
   *
   * @param reVector wektor współczynników rzeczywistych
   * @param imVector wektor współczynników urojonych
   * @return
   */
  def createComplexVector(reVector: RealVector, imVector: RealVector): FieldVector[Complex] = {
    val complexArray = (reVector.toArray, imVector.toArray).zipped.map((re, im) => new Complex(re, im)).array
    MatrixUtils.createFieldVector(complexArray)
  }

  /**
   * Redukuje wektor liczb zespolonych do liczby zespolonej
   *
   * @param vector
   * @param function
   * @return
   */
  def reduceComplexVector(vector: FieldVector[Complex], function: (Complex, Complex) => Complex): Complex = {
    vector.toArray.reduce(function)
  }

  /**
   * Redukuje wektor liczb zespolonych do liczby rzeczywistej
   *
   * @param vector
   * @param function
   * @return
   */
  def reduceComplexVector(vector: FieldVector[Complex], function: (Complex, Complex) => Double): Double = {
    // TODO to nie wygląda ładnie
    vector.toArray.reduce((a, b) => new Complex(function.apply(a, b))).getReal
  }

  /**
   * Tworzy losowy wektor liczb rzeczywistych
   *
   * @param random
   * @param length
   * @return
   */
  def randomRealVector(random: Random, length: Int) = {
    MatrixUtils.createRealVector(new Array[Double](length).map(x => random.nextDouble()))
  }

  /**
   * Dzieli wektor na dwie części - wektor długości n-1 oraz ostatni element
   *
   * @param realVector wektor
   * @return para (wektor, ostatni element)
   */
  def extractLast(realVector: RealVector) = {
    val subvector = realVector.getSubVector(0, realVector.getDimension - 1)
    val last = realVector.getEntry(realVector.getDimension - 1)
    (subvector, last)
  }

  /**
   * Odcina z wektora ostatni element
   *
   * @param realVector wektor
   * @return para (wektor, ostatni element)
   */
  def dropLast(realVector: RealVector) = realVector.getSubVector(0, realVector.getDimension - 1)

  /**
   * Sortuje elementy wektora
   *
   * @param realVector wektor do posortowania
   */
  def sort(realVector: RealVector) = realVector.setSubVector(0,
    new ArrayRealVector(realVector.toArray.sortWith(_ < _)))


  /**
   * Skaluje wektor
   * @param realVector wektor
   * @param newSum liczba, która ma być nową sumą wartości wektora
   * @return
   */
  def scale(realVector: RealVector, newSum: Double) = {
    val coeff = newSum / sum(realVector)
    realVector.mapMultiply(coeff)
  }

  /**
   * Sumuje wartości wektora
   *
   * @param realVector
   * @return
   */
  def sum(realVector: RealVector) = realVector.toArray.sum

  /**
   * Zamienia [a, b, c, ...] na [a, a+b, a+b+c, ...]
   *
   * @param realVector wektor
   */
  def asSums(realVector: RealVector) = {
    var sum = 0.0
    realVector.map(new UnivariateFunction {
      override def value(x: Double): Double = {
        sum += x
        sum
      }
    })
  }

  /**
   * Zamienia [a, a+b, a+b+c, ...] na [a, b, c, ...]
   *
   * @param realVector wektor
   */
  def fromSums(realVector: RealVector) = {
    var sum = 0.0
    realVector.map(new UnivariateFunction {
      override def value(x: Double): Double = {
        val newX = x - sum
        sum += x
        newX
      }
    })
  }

  /**
   * Konwertuje wektor liczb zespolonych do Stringa
   *
   * @param v
   */
  def complexVecToString(v: FieldVector[Complex]) = "[" + v.toArray.map(_.toString).mkString(", ") + "]"

  /**
   * Zwraca iterowalną kolekcję
   *
   * @param v wektor
   * @return kolekcja implementująca Iterable[Double]
   */
  def doubleIterable(v: RealVector): Iterable[Double] = {
    val dim = v.getDimension
    new Iterable[Double] {
      override def iterator = new Iterator[Double] {
        private var i = 0

        override def hasNext: Boolean = i < dim

        override def next(): Double = {
          i += 1
          v.getEntry(i - 1)
        }
      }
    }
  }

}
