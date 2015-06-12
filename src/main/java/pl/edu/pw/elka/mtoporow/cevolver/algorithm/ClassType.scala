package pl.edu.pw.elka.mtoporow.cevolver.algorithm

/**
 * Typ reprezentujący parametr klasowy
 * Data utworzenia: 22.05.15, 12:30
 * @author Michał Toporowski
 */
abstract class ClassType[T] extends Enumeration {
  //  type Params = Map[String, Any]
  //  type Instantiator = (Params => T)
  //  protected case class ClassTypeVal(private val instantiator: Instantiator) extends super.Val {
  //    def instantiate(params: Params) = instantiator.apply(params)
  //  }

}

////
////object EOType extends ClassType[CanalProblemAlgorithm.EO] {
////  //FIXME
////  val EO1 = ClassTypeVal(p => new CanalProblemAlgorithm.EO {
////    override def apply(selectedCandidates: util.List[RealVector], rng: Random): util.List[RealVector] = ???
////  })
////}
////
////object CFType extends ClassType[CanalProblemAlgorithm.CF] {
////}
////
////object SSType extends ClassType[CanalProblemAlgorithm.SS] {
////
////}
////
//
//abstract class APT extends Enumeration {
//  protected case class APTVal(paramTypes: Map[String, _ >: Class]) extends super.Val
//}
////
////object EOType extends APT {
////  val DEFAULT = APTVal(Map())
//////  val DEFAULT = APTVal(Map("x" -> classOf[String]))
////}


trait Data[I] {
  var data: I = _
}