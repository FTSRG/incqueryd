package hu.bme.mit.incqueryd.coordinator.client

import akka.pattern.ask
import akka.util.Timeout
import hu.bme.mit.incqueryd.engine._
import hu.bme.mit.incqueryd.inventory.{Inventory, MachineInstance}
import org.eclipse.incquery.runtime.rete.recipes.ReteRecipe
import org.openrdf.model.Model

import scala.concurrent.Await
import scala.concurrent.duration._

object Coordinator {
  final val port = 9090
  final val actorSystemName = "coordinator"
  final val actorName = "coordinator"
}

class Coordinator(instance: MachineInstance) {

  def loadData(databaseUrl: String, vocabulary: Model, inventory: Inventory): Index = {
    println(s"Loading data")
    askCoordinator[Index](LoadData(databaseUrl, vocabulary, inventory))
  }

  def startQuery(recipe: ReteRecipe, index: Index): ReteNetwork = {
    println(s"Starting query")
    askCoordinator[ReteNetwork](StartQuery(recipe))
  }

  def checkResults(pattern: PatternDescriptor): List[ChangeSet] = {
    askCoordinator[List[ChangeSet]](CheckResults(pattern))
  }

  def stopQuery(network: ReteNetwork) {
    println(s"Stopping query")
    askCoordinator[String](StopQuery(network))
  }

  private def askCoordinator[T](message: CoordinatorCommand, timeout: Timeout = Timeout(AkkaUtils.defaultTimeout)): T = {
    val coordinatorActor = AkkaUtils.findActor(Coordinator.actorSystemName, instance.ip, Coordinator.port, Coordinator.actorName)
    val future = coordinatorActor.ask(message)(timeout)
    Await.result(future, timeout.duration).asInstanceOf[T]
  }

}