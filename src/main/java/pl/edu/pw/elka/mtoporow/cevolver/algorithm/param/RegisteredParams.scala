package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param

import java.util

/**
 * Obiekt przechowujący zarejestrowane parametry liczbowe algorytmu
 * Data utworzenia: 31.05.15, 12:02
 * @author Michał Toporowski
 */
object RegisteredParams {

  /**
   * Opis parametru
   * @param name nazwa
   * @param cls klasa
   */
  class ParamDef(val name: String, val cls: Class[_])

  /* Parametry */
  val GENERATION_COUNT = new ParamDef("generation_count", classOf[Integer])

  /**
   * Mapa zawierająca parametry wymagane przez dany typ algorytmu
   */
  val partsParamDefs: util.Map[AlgorithmPartType, Array[ParamDef]] = new util.HashMap[AlgorithmPartType, Array[ParamDef]]()
  put(TCType.DEFAULT, GENERATION_COUNT)

  /**
   * Umieszcza elementy w mapie partsParams
   *
   * @param t typ części algorytmu
   * @param params parametry
   * @return
   */
  private def put(t: AlgorithmPartType, params: ParamDef*) = partsParamDefs.put(t, params.toArray)
}
