package pl.edu.pw.elka.mtoporow.cevolver.algorithm

import java.util.{List => JList}

import org.uncommons.watchmaker.framework.EvaluatedCandidate

/**
 * Wynik działania algorytmu - zawiera m.in. stan populacji, czas działania
 * Data utworzenia: 07.02.16, 17:06
 *
 * @param population wynikowa populacja posortowana od najlepszego osobnika
 * @param duration czas trwania obliczeń w milisekundach
 * @param fitnessTrace lista wartości funkcji celu najlepszego osobnika w kolejnych pokoleniach
 *
 * @author Michał Toporowski
 */
class EvolutionResult(
                       val population: JList[EvaluatedCandidate[EvolutionaryAlgorithm.C]],
                       val duration: Long,
                       val fitnessTrace: JList[java.lang.Double]
                       ) {
  /**
   * Zwraca liczbę pokoleń
   * @return liczba pokoleń
   */
  def generationCount = fitnessTrace.size()

  /**
   * Zwraca czas trwania eksperymentu w sekundach
   *
   * @return liczba sekund
   */
  def durationSec = duration / 1000.0
}