package pl.edu.pw.elka.mtoporow.cevolver.lib.util.matrix

import java.util.Random

import org.apache.commons.math3.analysis.UnivariateFunction
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.linear._
import pl.edu.pw.elka.mtoporow.cevolver.lib.util.maths.MiscMathOps

/**
 * Zawiera operacje na macierzach i wektorach
 * Data utworzenia: 21.06.15, 10:09
 * @author Michał Toporowski
 */
object MatrixOps {
  type ComplexVector = FieldVector[Complex]

  /**
   * Tworzy wektor liczb zespolonych z wektorów Re oraz Im
   *
   * @param reVector wektor współczynników rzeczywistych
   * @param imVector wektor współczynników urojonych
   * @return
   */
  def createComplexVector(reVector: RealVector, imVector: RealVector): FieldVector[Complex] = {
    val complexArray = (asIterable(reVector), asIterable(imVector)).zipped.map((re, im) => new Complex(re, im)).toArray
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
    asIterable(vector).reduce(function)
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
    asIterable(vector).reduce((a, b) => new Complex(function.apply(a, b))).getReal
  }

  /**
   * Tworzy losowy wektor liczb rzeczywistych
   *
   * @param random
   * @param length
   * @return
   */
  def randomRealVector(random: Random, length: Int) = {
    MatrixUtils.createRealVector((1 to length).map(x => random.nextDouble()).toArray)
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
  def sum(realVector: RealVector): Double = asIterable(realVector).sum

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
   * Odwraca wektor liczb rzeczywistych
   *
   * @param realVector wektor
   * @return wektor z odwróconą kolejnością
   */
  def invert(realVector: RealVector) = {
    new ArrayRealVector(realVector.toArray.reverse)
  }

  /**
   * Porównuje 2 wektory liczb zespolonych
   *
   * @param measured wektor wartości zmierzonych
   * @param precise wektor wartości dokładnych
   * @param function funkcja porównująca
   * @return wynik jako tablica liczb rzeczywistych
   */
  def compare(measured: ComplexVector, precise: ComplexVector, function: (Complex, Complex) => Double) = {
    (asIterable(measured), asIterable(precise)).zipped.map(function).toArray
  }

  /**
   * Liczy błąd względny na modułach (amplitudach) elementów wektora liczb zespolonych
   *
   * @param measured wektor wartości zmierzonych
   * @param precise wektor wartości dokładnych
   * @return wynik jako tablica liczb rzeczywistych
   */
  def relativeErrorAbs(measured: ComplexVector, precise: ComplexVector) = compare(measured, precise, MiscMathOps.relativeErrorAbs)

  /**
   * Liczy błąd bezwzględny na modułach (amplitudach) elementów wektora liczb zespolonych
   *
   * @param measured wektor wartości zmierzonych
   * @param precise wektor wartości dokładnych
   * @return wynik jako tablica liczb rzeczywistych
   */
  def absoluteErrorAbs(measured: ComplexVector, precise: ComplexVector) = compare(measured, precise, MiscMathOps.absoluteErrorAbs)

  /**
   * Liczy błąd względny na kątach (fazach) elementów wektora liczb zespolonych
   *
   * @param measured wektor wartości zmierzonych
   * @param precise wektor wartości dokładnych
   * @return wynik jako tablica liczb rzeczywistych
   */
  def relativeErrorPhase(measured: ComplexVector, precise: ComplexVector) = compare(measured, precise, MiscMathOps.relativeErrorPhase)

  /**
   * Liczy błąd bezwzględny na kątach (fazach) elementów wektora liczb zespolonych
   *
   * @param measured wektor wartości zmierzonych
   * @param precise wektor wartości dokładnych
   * @return wynik jako tablica liczb rzeczywistych
   */
  def absoluteErrorPhase(measured: ComplexVector, precise: ComplexVector) = compare(measured, precise, MiscMathOps.absoluteErrorPhase)

  /**
   * Liczy wartość średnią tablicy liczb rzeczywistych
   *
   * @param vals tablica
   * @return wartość średnia
   */
  def avg(vals: Array[Double]) = vals.sum / vals.length

  /**
   * Liczy minimum tablicy
   *
   * @param vals tablica
   * @return minimum
   */
  def min(vals: Array[Double]) = vals.min

  /**
   * Liczy maksimum tablicy
   *
   * @param vals tablica
   * @return maksimum
   */
  def max(vals: Array[Double]) = vals.max

  /**
   * Tworzy łańcuch znaków z tablicy przy użyciu separatora
   *
   * @param vals tablica
   * @param sep separator
   * @return String
   */
  def mkString(vals: Array[Double], sep: String) = vals.mkString(sep)

  /**
   * Konwertuje wektor liczb zespolonych do Stringa
   *
   * @param v wektor
   */
  def complexVecToString(v: FieldVector[Complex]) = "[" + asIterable(v).map(c => c.toString + "|" + c.abs + "|").mkString(", ") + "]"

  /**
   * Zwraca iterowalną kolekcję
   *
   * @param v wektor
   * @return kolekcja implementująca Iterable[Double]
   */
  def asIterable(v: RealVector): Iterable[Double] = {
    val dim = v.getDimension
    new Iterable[Double] {
      override def iterator = new Iterator[Double] {
        private var i = 0

        override def hasNext: Boolean = i < dim

        override def next(): Double = {
          val e = v.getEntry(i)
          i += 1
          e
        }
      }
    }
  }

  /**
   * Zwraca iterowalną kolekcję
   *
   * @param v wektor
   * @return kolekcja implementująca Iterable[Complex]
   */
  def asIterable(v: FieldVector[Complex]): Iterable[Complex] = {
    val dim = v.getDimension
    new Iterable[Complex] {
      override def iterator = new Iterator[Complex] {
        private var i = 0

        override def hasNext: Boolean = i < dim

        override def next(): Complex = {
          val e = v.getEntry(i)
          i += 1
          e
        }
      }
    }
  }

}
