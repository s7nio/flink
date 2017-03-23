package org.apache.flink.ml.math.clustering

import breeze.linalg.{DenseVector => BreezDenseVector}
import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.{DenseVector, Vector}
import org.apache.flink.ml.math.Vector
import org.apache.flink.ml.metrics.distances.EuclideanDistanceMetric


/** Implements a KMeans algorithm.
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

  def newCentroid(vectors: Seq[LabeledVector]): DenseVector = {
    val v = vectors.map(_.vector match {
      case a: Vector => BreezDenseVector(a.toArray.map(_._2))
    }).toList.reduce((a, b) => a + b)
//    val tmp = v.dot(BreezDenseVector.fill(v.size) {
//      1.0 / vectors.length
//    })
    val foo = List.fill(1)(BreezDenseVector(vectors.length))
    val tmp = v :/ foo
    DenseVector()
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
