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
  val STANDARD_DEVIATION = new ParamDef("standardDeviation", classOf[Double])
  val TARGET_FITNESS = new ParamDef("targetFitness", classOf[Double])
  val OFFSPRING_MOD = new ParamDef("offspringMod", classOf[Integer])

  /**
   * Mapa zawierająca parametry wymagane przez dany typ algorytmu
   */
  val partsParamDefs: mutable.Map[AlgorithmPartType, Array[ParamDef]] = new mutable.HashMap[AlgorithmPartType, Array[ParamDef]]()
  put(TCType.GENERATION_COUNT, GENERATION_COUNT)
  put(TCType.STAGNATION, GENERATION_COUNT)
  put(TCType.TARGET_FITNESS, TARGET_FITNESS)
  put(FEType.DEFAULT, PUNISHMENT_RATIO)
  put(EOType.SIMPLE_MUTATION, PROBABILITY)
  put(EOType.DIST_ARRAY_CROSSOVER, PROBABILITY)
  put(EOType.INVERSION, PROBABILITY)
  put(EOType.STANDARD_GAUSSIAN_MUTATION, PROBABILITY, STANDARD_DEVIATION)
  put(EOType.SELF_ADAPTATING_MUTATION, PROBABILITY)
  put(EOType.AVERAGE_VALUE_CROSSOVER, PROBABILITY)
  put(EOType.PARAMETER_AVG_VAL_CROSSOVER, PROBABILITY)
  put(SSType.SUS, PROBABILITY)
  put(SSType.TOURNAMENT, PROBABILITY)
  put(SSType.ES_PLUS, OFFSPRING_MOD)
  put(SSType.ES_COMMA, OFFSPRING_MOD)

  /**
   * Umieszcza elementy w mapie partsParams
   *
   * @param t typ części algorytmu
   * @param params parametry
   * @return
   */
  private def put(t: AlgorithmPartType, params: ParamDef*) = partsParamDefs.put(t, params.toArray)

  /**
   * Pobiera definicje parametrów dla danego typu części algorytmu
   *
   * @param partType typ
   * @return definicje
   */
  def partParamDefs(partType: AlgorithmPartType) = RegisteredParams.partsParamDefs.getOrElse(partType, Array[RegisteredParams.ParamDef]())
}
