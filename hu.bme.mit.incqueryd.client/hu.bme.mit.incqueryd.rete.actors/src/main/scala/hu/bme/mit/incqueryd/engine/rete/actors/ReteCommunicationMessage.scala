package hu.bme.mit.incqueryd.engine.rete.actors

import akka.actor.ActorPath
import akka.actor.ActorRef
import hu.bme.mit.incqueryd.engine.rete.dataunits.ChangeSet
import hu.bme.mit.incqueryd.engine.rete.dataunits.ReteNodeSlot
import hu.bme.mit.incqueryd.engine.rete.dataunits.Tuple
import hu.bme.mit.incqueryd.engine.util.ReteNodeConfiguration

sealed trait ReteCommunicationMessage
case class Configure(configuration: ReteNodeConfiguration) extends ReteCommunicationMessage
case object EstablishSubscriptions extends ReteCommunicationMessage
case class RegisterSubscriber(slot: ReteNodeSlot) extends ReteCommunicationMessage
case class UpdateMessage(changeSet: ChangeSet, slot: ReteNodeSlot, route: List[ActorPath]) extends ReteCommunicationMessage
case class TerminationMessage(route: List[ActorPath]) extends ReteCommunicationMessage
case object PropagateState extends ReteCommunicationMessage 
case class PropagateInputChange(changeSet : ChangeSet) extends ReteCommunicationMessage
case class FilterOutAndPropagate(subjectId: Long) extends ReteCommunicationMessage // XXX only for demo?
case class SubscribeReceiver(receiver : ActorRef) extends ReteCommunicationMessage
case class UnsubscribeReceiver(receiver : ActorRef) extends ReteCommunicationMessage
case object GetQueryResults extends ReteCommunicationMessage
