package hu.bme.mit.incqueryd.spark.client

import org.apache.spark.launcher.SparkLauncher
import org.apache.spark.SparkConf
import hu.bme.mit.incqueryd.yarn.IncQueryDZooKeeper
import hu.bme.mit.incqueryd.spark.ProcessingMethod
import hu.bme.mit.incqueryd.spark.utils.IQDSparkUtils._
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import hu.bme.mit.incqueryd.engine.util.DatabaseConnection
import hu.bme.mit.incqueryd.engine.util.DatabaseConnection.Backend

/**
 * @author pappi
 */
object IQDSparkClient {
  
  lazy val pool: ExecutorService = Executors.newCachedThreadPool()
  
  def getSparkLauncher(): SparkLauncher = {
    new SparkLauncher()
      .setSparkHome(SPARK_HOME)
      .setAppResource(HDFS_JAR_PATH)
      .setMaster("yarn-cluster")
      .setDeployMode("cluster")
      .setMainClass(MAIN_CLASS)
      .setConf(SparkLauncher.DRIVER_MEMORY, "512m")
      .setConf(SparkLauncher.EXECUTOR_MEMORY, "512m")
      }

  def loadData(databaseConnection: DatabaseConnection) {
    val method = databaseConnection.getBackend match {
      case Backend.FILE => ProcessingMethod.HDFS_LOAD
      case Backend.FOURSTORE => ProcessingMethod.FOURSTORE_LOAD
    }
    val exit_code = getSparkLauncher()
      .setAppName(method.toString())
      .addAppArgs(s"-$OPTION_PROCESSING_METHOD", method.toString())
      .addAppArgs(s"-$OPTION_DURATION", DEFAULT_DURATION.toString())
      .addAppArgs(s"-$OPTION_DATASOURCE_URL", databaseConnection.getConnectionString)
      .addAppArgs(s"-$OPTION_SINGLE_RUN")
      .addAppArgs(s"-$OPTION_NO_DATA_TIMEOUT_MS", 30000.toString())
      .launch().waitFor()
  }

  lazy val wikistreamPool: ExecutorService = Executors.newCachedThreadPool()

  def startWikidataStreaming(databaseConnection: DatabaseConnection)  = {
    val thread = new Thread {
      override def run() {
        val process = getSparkLauncher()
          .setAppName(s"WIKISTREAM")
          .addAppArgs(s"-$OPTION_PROCESSING_METHOD", ProcessingMethod.WIKISTREAM.toString())
          .addAppArgs(s"-$OPTION_DURATION", DEFAULT_DURATION.toString())
          .addAppArgs(s"-$OPTION_DATASOURCE_URL", databaseConnection.getConnectionString)
          .addAppArgs(s"-$OPTION_SINGLE_RUN")
          .addAppArgs(s"-$OPTION_NO_DATA_TIMEOUT_MS", 60000.toString())
          .launch()
      }
    }
    wikistreamPool.execute(thread)
  }

  def stopWikidataStreaming() = {
    wikistreamPool.shutdownNow()
  }

  def startOutputStream(patternName : String, productionNodePath: String) = {
   
    val thread = new Thread {
      override def run() {
        val process = getSparkLauncher()
          .setAppName(s"$patternName query results stream")
          .addAppArgs(s"-$OPTION_PROCESSING_METHOD", ProcessingMethod.PRODUCTIONSTREAM.toString())
          .addAppArgs(s"-$OPTION_DURATION", DEFAULT_DURATION.toString())
          .addAppArgs(s"-$OPTION_DATASOURCE_URL", productionNodePath)
          .addAppArgs(s"-$OPTION_QUERY_ID", patternName)
          .launch()
      }
    }
    
    pool.execute(thread);
    
  }

  def stopOutputStreams() = {
    pool.shutdownNow()
  }

}