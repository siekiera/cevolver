package pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets

import pl.edu.pw.elka.mtoporow.cevolver.lib.model.{CanalResponse, Distances}

/**
 * Klasa reprezentująca zestaw danych do uruchomienia algorytmu i testów
 * Data utworzenia: 05.02.16, 12:58
 * @author Michał Toporowski
 *
 * @param measurementParams parametry pomiaru (m.in. parametry fizyczne kanału transmisyjnego)
 * @param canalResponse odpowiedź rzeczywistego kanału
 * @param expectedDistances odległości w rzeczywistym kanale na potrzeby testów
 */
class DataSet(val measurementParams: MeasurementParams,
              val canalResponse: CanalResponse,
              val expectedDistances: Distances)
