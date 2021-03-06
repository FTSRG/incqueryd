package hu.bme.mit.incqueryd.actorservice.server

import hu.bme.mit.incqueryd.actorservice.ActorId
import hu.bme.mit.incqueryd.actorservice.AkkaUtils
import hu.bme.mit.incqueryd.yarn.IncQueryDZooKeeper
import upickle._
import eu.mondo.utils.NetworkUtils
import hu.bme.mit.incqueryd.actorservice.YarnActorService
import hu.bme.mit.incqueryd.actorservice.ActorId
import akka.actor.Actor
import scala.concurrent.Await

object ActorApplication {

  def main(args: Array[String]) {
    val zkActorPath = args(0)
    val name = args(1)
    val actorClassName = args(2)
    val actorClass = Class.forName(actorClassName).asSubclass(classOf[Actor])
    val ip = NetworkUtils.getLocalIpAddress
    val port = YarnActorService.port
    val actorRef = AkkaUtils.startActor(ActorId(YarnActorService.actorSystemName, ip, port, name), actorClass)
    IncQueryDZooKeeper.setData(zkActorPath + IncQueryDZooKeeper.addressPath, s"$ip:$port".getBytes)
    IncQueryDZooKeeper.setData(zkActorPath, actorRef.path.toSerializationFormat.getBytes)
    AkkaUtils.teminateClientActorSystem()
  }

}