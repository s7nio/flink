package org.apache.flink.ml.clustering

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.ml.classification.Classification
import org.apache.flink.ml.math.clustering.KMeans
import org.apache.flink.ml.util.FlinkTestBase
import org.scalactic.TripleEquals
import org.scalatest.{FlatSpec, Matchers}

class KMeansSuite extends FlatSpec with Matchers with FlinkTestBase {

  behavior of "KMeans implementation"

  it should "train KMeans" in {
    //    val env = ExecutionEnvironment.getExecutionEnvironment

    val kmeans = KMeans()

    //    val trainingDataSet = env.fromCollection(KMeansTestData.trainingData)

    KMeans.fit(KMeansTestData.trainingData)


    KMeans.centroides.zip(KMeansTestData.expectedCentroids).foreach {
      case (c, expectedC) =>
        c.valueIterator.zip(expectedC.valueIterator).foreach {
          case (v, expectedV) =>
            v should be(expectedV +- 1)
        }

    }

  }
}
