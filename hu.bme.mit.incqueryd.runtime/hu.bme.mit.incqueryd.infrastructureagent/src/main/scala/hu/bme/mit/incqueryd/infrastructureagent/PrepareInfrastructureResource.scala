package hu.bme.mit.incqueryd.infrastructureagent

import javax.ws.rs.core.MediaType
import javax.ws.rs.{GET, Path, Produces, QueryParam}
import com.codahale.metrics.annotation.Timed
import hu.bme.mit.incqueryd.coordinator.client.Coordinator
import hu.bme.mit.incqueryd.engine.{AkkaUtils, CoordinatorActor, IsAlive}
import hu.bme.mit.incqueryd.infrastructureagent.client.InfrastructureAgent.PrepareInfrastructure._
import hu.bme.mit.incqueryd.infrastructureagent.client.{InfrastructureAgent, PrepareInfrastructureResponse}
import hu.bme.mit.incqueryd.inventory.Inventory
import upickle._

@Path(InfrastructureAgent.PrepareInfrastructure.path)
@Produces(Array(MediaType.APPLICATION_JSON))
class PrepareInfrastructureResource {

  @GET
  @Timed
  def execute(@QueryParam(inventoryParameter) inventoryJson: String, @QueryParam(currentIpParameter) currentIp: String): PrepareInfrastructureResponse = {
    val inventory = read[Inventory](inventoryJson)
    val isMaster = inventory.master.ip == currentIp
    if (isMaster) {
      startCoordinator(inventory)
      startMonitoring
    }
    startOsAgent(inventory)
    new PrepareInfrastructureResponse(isMaster)
  }

  private def startCoordinator(inventory: Inventory) {
    val masterIp = inventory.master.ip
    val coordinatorActor = AkkaUtils.createActor(Coordinator.actorSystemName, masterIp, Coordinator.port, Coordinator.actorName, classOf[CoordinatorActor])
    AkkaUtils.retry(AkkaUtils.defaultRetryCount)(AkkaUtils.defaultDelayMillis) {
      coordinatorActor ! IsAlive
    }
  }

  private def startMonitoring {
    // TODO
  }

  private def startOsAgent(inventory: Inventory) {
    // TODO
  }

}
