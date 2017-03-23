package org.apache.flink.ml.math.clustering

import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.{DenseVector, Vector}
import org.apache.flink.ml.math.Vector
import org.apache.flink.ml.metrics.distances.EuclideanDistanceMetric


/** Implements a KMeans algorithm.
  *
  */
class KMeans extends App {

}

/** Companion object of KMeans.
  */
object KMeans {

  val k_default = 2

  def apply(): KMeans = {
    new KMeans()
  }

  //  val foo = DenseVector(1,1) + DenseVector(1,1)
  //  val fooFunc = (v1: DenseVector[Int], v2: DenseVector[Int]) => v1 + v2
  //  val fooFunc2 = (v1:Vector, v2:Vector): Vector => v1+v2

  def newCentroid(vectors: Seq[LabeledVector]): Vector = {
    //    val v:Vector = vectors.map(_.vector).sum
    //        val v = vectors.map(_.vector).foldLeft(_ + _)
    //    val v = vectors.map(_.vector).foldLeft(fooFunc)
    //    val v = vectors.map(_.vector).toList.reduce((a, b) => )
    val v = vectors.map(_.vector match { case DenseVector(_) => DenseVector() }).toList.reduce((a, b) => a + b)

    //    v.dot(DenseVector.fill(v.size) {
    //      1.0 / vectors.length
    //    })
  }

  var centroides: List[Vector] = List()

  def findNearestCentroid(cs: List[Vector], v: Vector): Int = {
    val l = cs.map(c => EuclideanDistanceMetric().distance(v, c))
    l.indexOf(l.min)
  }

  def fit(data: Seq[LabeledVector]): Unit = {

    var dataVector = data

    centroides = dataVector.slice(0, k_default).map(_.vector).toList

    for (n <- 0 to 100) {
      // assign
      dataVector.map(lv => LabeledVector(findNearestCentroid(centroides, lv.vector), lv.vector))

      // update
      centroides = dataVector.groupBy(_.label).map(k => newCentroid(k._2)).toList
    }

  }

  def predict(v: Vector): Int = {
    findNearestCentroid(centroides, v)
  }
}
