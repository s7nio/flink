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
  def newCentroid(v: Seq[LabeledVector]): DenseVector = {
    v.map(_.vector.asBreeze)
      .reduce(_ + _).*(1.0 / v.length)
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
    cs
      .map(c => sumOfSquares(data.filter(_.label == c.label), c))
      .reduce(_ + _)
  }

  def fit(data: Seq[LabeledVector]): Unit = {

    // first assigment of centroids
    val initCentroides = data.slice(0, k_default).map(_.vector).zipWithIndex.map(x => LabeledVector(x._2, x._1))
    //val initCentroides = Seq(LabeledVector(0.0, DenseVector(-20, 20)), LabeledVector(1.0, DenseVector(10, 10)))

    def assignVectorToCentroids(cs: Seq[LabeledVector], lv: LabeledVector): LabeledVector = {
      LabeledVector(findNearestCentroid(cs, lv.vector), lv.vector)
    }

    def updatedCentroids(d: Seq[LabeledVector]): Seq[LabeledVector] = {
      val newCentroids = d.groupBy(_.label).map(k => newCentroid(k._2))
      newCentroids.zipWithIndex.map(x => LabeledVector(x._2, x._1)).toSeq
    }

    //    centroids = initCentroides
    //    var newData = data
    //    for (n <- 0 to 1000) {
    //      newData = data.map(assignVectorToCentroids(centroids, _))
    //      centroids = updatedCentroids(newData)
    //      println(centroids)
    //    }


    //    val maxSteps = 10
    //
    //    def finalResult(initialCentroides: Seq[LabeledVector], ds: Seq[LabeledVector]): (Seq[LabeledVector], Seq[LabeledVector]) =
    //      (0 to maxSteps).foldRight((initialCentroides, ds))((n, agg) => {
    //
    //        val currentData = agg._2
    //        val currentCentroids = agg._1
    //        val newData = currentData.map(assignVectorToCentroids(currentCentroids, _))
    //        val newCentroids = updatedCentroids(newData)
    //        println(newCentroids)
    //
    //        (newCentroids, newData)
    //      })
    //
    //    val result = finalResult(initCentroides, data)
    //    centroids = result._1
    //    println(centroids)


    def finalResult(dataset: Seq[LabeledVector], centroids: Seq[LabeledVector], WCSS: Double): (Seq[LabeledVector], Seq[LabeledVector]) = {

      val currentWCSS: Double = withinClusterSumOfSquares(dataset, centroids)

      if (math.abs(WCSS - currentWCSS) > 0.001) {
        val newData = dataset.map(assignVectorToCentroids(centroids, _))
        val newCentroids = updatedCentroids(newData)
        println(newCentroids)
        println(WCSS, currentWCSS)
        finalResult(newData, newCentroids, currentWCSS)
      } else {
        println(centroids)
        println(WCSS, currentWCSS)
        (dataset, centroids)
      }

    }

    val result = finalResult(data, initCentroides, 100.0)
    centroids = result._2
    println(centroids)


  }

  var centroids: Seq[LabeledVector] = Seq()


  def predict(v: DenseVector): Int = {
    findNearestCentroid(centroids, v)
  }
}
