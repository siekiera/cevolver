package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param

import java.lang.Double

import scala.collection.mutable

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
  val PUNISHMENT_RATIO = new ParamDef("punishment_ratio", classOf[Double])
  val PROBABILITY = new ParamDef("probability", classOf[Double])

  /**
   * Mapa zawierająca parametry wymagane przez dany typ algorytmu
   */
  val partsParamDefs: mutable.Map[AlgorithmPartType, Array[ParamDef]] = new mutable.HashMap[AlgorithmPartType, Array[ParamDef]]()
  put(TCType.DEFAULT, GENERATION_COUNT)
  put(FEType.DEFAULT, PUNISHMENT_RATIO)
  put(EOType.SIMPLE_MUTATION, PROBABILITY)
  put(EOType.DIST_ARRAY_CROSSOVER, PROBABILITY)

  /**
   * Umieszcza elementy w mapie partsParams
   *
   * @param t typ części algorytmu
   * @param params parametry
   * @return
   */
  private def put(t: AlgorithmPartType, params: ParamDef*) = partsParamDefs.put(t, params.toArray)
}
