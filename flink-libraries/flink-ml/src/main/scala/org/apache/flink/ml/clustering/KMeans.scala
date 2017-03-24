package org.apache.flink.ml.clustering

import org.apache.flink.ml.common.LabeledVector
import org.apache.flink.ml.math.Breeze._
import org.apache.flink.ml.math.DenseVector
import org.apache.flink.ml.math.Vector
import org.apache.flink.ml.metrics.distances.EuclideanDistanceMetric

import scala.collection.immutable.IntMap


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

  // Calculates the centroid of vectors
  def newCentroid(vectors: Seq[LabeledVector]): DenseVector = {
    vectors
      .map(_.vector.asBreeze)
      .reduce(_ + _).*(1.0 / vectors.length)
      .fromBreeze.asInstanceOf[DenseVector]
  }

  def sumOfSquares(vectors: Seq[LabeledVector], centroid: LabeledVector): Double = {
    vectors
      .map { v =>
        val dd = v.vector.asBreeze - centroid.vector.asBreeze
        dd.dot(dd)
      }.reduce(_ + _)
  }

  // Determines nearest centroid for a vector v
  def findNearestCentroid(cs: Seq[LabeledVector], v: Vector): Int = {
    val l = cs.map(c => EuclideanDistanceMetric().distance(v, c.vector)).toList
    l.indexOf(l.min)
  }

  def withinClusterSumOfSquares(data: Seq[LabeledVector], cs: Seq[LabeledVector]): Double = {
    val all =for {
      cluster <- cs
    } yield {
      sumOfSquares(data.filter(_.label == cluster.label), cluster)
    }
    all.reduce(_ + _)
  }

  var centroides: Seq[LabeledVector] = Seq()

  def fit(data: Seq[LabeledVector]): Unit = {

    // first assigment of centroids
    val vectors = data.slice(0, k_default).map(_.vector)
    centroides = vectors.zipWithIndex.map(x => LabeledVector(x._2, x._1))

    def assign(cs: Seq[LabeledVector], lv: LabeledVector): LabeledVector = {
      LabeledVector(findNearestCentroid(cs, lv.vector), lv.vector)
    }

    def update(data: Seq[LabeledVector]): Seq[LabeledVector] = {
      val newCentroids = data.groupBy(_.label).map(k => newCentroid(k._2))

      newCentroids.zipWithIndex.map(x => LabeledVector(x._2, x._1)).toSeq
    }


    for (n <- 0 to 10) {
      // assign
      data.map(assign(centroides, _))

      // update
      centroides = update(data)
    }




  }

  def predict(v: DenseVector): Int = {
    findNearestCentroid(centroides, v)
  }
}
