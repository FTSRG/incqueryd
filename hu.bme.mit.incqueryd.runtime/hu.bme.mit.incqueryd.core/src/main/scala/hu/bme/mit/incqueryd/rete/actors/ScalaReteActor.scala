package hu.bme.mit.incqueryd.rete.actors

import org.eclipse.incquery.runtime.rete.recipes.ReteNodeRecipe
import hu.bme.mit.incqueryd.rete.nodes.ReteNode
import java.util.HashMap
import hu.bme.mit.incqueryd.rete.dataunits.ReteNodeSlot
import hu.bme.mit.incqueryd.util.ReteNodeConfiguration
import hu.bme.mit.incqueryd.rete.nodes.ReteNodeFactory
import akka.actor.Actor
import hu.bme.mit.incqueryd.rete.messages.ActorReply
import hu.bme.mit.incqueryd.rete.messages.YellowPages
import hu.bme.mit.incqueryd.arch.ArchUtil
import org.eclipse.incquery.runtime.rete.recipes.AlphaRecipe
import org.eclipse.incquery.runtime.rete.recipes.BetaRecipe
import org.eclipse.incquery.runtime.rete.recipes.ProductionRecipe
import scala.collection.JavaConversions._
import akka.actor.ActorRef
import hu.bme.mit.incqueryd.rete.messages.UpdateMessage
import hu.bme.mit.incqueryd.rete.dataunits.ChangeSet
import hu.bme.mit.incqueryd.rete.nodes.AlphaNode
import org.apache.commons.lang.NotImplementedException
import hu.bme.mit.incqueryd.rete.nodes.BetaNode
import scala.collection.immutable.Stack
import hu.bme.mit.incqueryd.rete.nodes.InputNode
import hu.bme.mit.incqueryd.rete.nodes.InitializableReteNode
import hu.bme.mit.incqueryd.rete.messages.Transformation
import hu.bme.mit.incqueryd.rete.messages.TerminationMessage
import hu.bme.mit.incqueryd.rete.messages.CoordinatorMessage
import hu.bme.mit.incqueryd.rete.messages.SubscriptionMessage
import hu.bme.mit.incqueryd.rete.nodes.ProductionNode

class ScalaReteActor extends Actor {

  protected var recipe: ReteNodeRecipe = null
  protected var reteNode: ReteNode = null
  protected var subscribers = new HashMap[ActorRef, ReteNodeSlot]
  protected var pendingTerminationMessages = 0
  protected var coordinatorRef: ActorRef = null

  System.err.println("[ReteActor] Rete actor instantiated.")

  private def configure(conf: ReteNodeConfiguration) = {
    recipe = conf.getReteNodeRecipe()
    reteNode = ReteNodeFactory.createNode(conf)

    System.err.println("[ReteActor] " + reteNode.getClass().getName() + " configuration received.")

    sender ! ActorReply.CONFIGURATION_RECEIVED
  }

  private def subscribe(yellowPages: YellowPages) = {
    
    val emfUriToActorRef = yellowPages.getEmfUriToActorRef()

    System.err.println();
    System.err.println("[ReteActor] " + self + ", " + reteNode.getClass().getName() + ": "
      + ArchUtil.oneLiner(recipe.toString()))

    recipe match {
      case alphaRecipe: AlphaRecipe => {
        val parent = alphaRecipe.getParent
        val parentUri = ArchUtil.getJsonEObjectUri(parent)
        val parentActorRef = emfUriToActorRef.get(parentUri)

        System.err.println("[ReteActor] - parent: " + parentUri + " -> " + parentActorRef)

        subscribeToActor(parentActorRef, ReteNodeSlot.SINGLE)
      }
      case betaRecipe: BetaRecipe => {
        val primaryParent = betaRecipe.getLeftParent.getParent
        val secondaryParent = betaRecipe.getRightParent.getParent

        val primaryParentUri = ArchUtil.getJsonEObjectUri(primaryParent)
        val primaryParentActorRef = emfUriToActorRef.get(primaryParentUri)

        System.err.println("[ReteActor] - primary parent URI: " + primaryParentUri + " -> " + primaryParentActorRef)

        val secondaryParentUri = ArchUtil.getJsonEObjectUri(secondaryParent)
        val secondaryParentActorRef = emfUriToActorRef.get(secondaryParentUri)

        System.err.println("[ReteActor] - secondary parent URI: " + secondaryParentUri + " -> "
          + secondaryParentActorRef)

        subscribeToActor(primaryParentActorRef, ReteNodeSlot.PRIMARY)
        subscribeToActor(secondaryParentActorRef, ReteNodeSlot.SECONDARY)
      }
      case productionRecipe: ProductionRecipe => {
        val parents = productionRecipe.getParents

        parents.foreach(parent => {
          val parentUri = ArchUtil.getJsonEObjectUri(parent)
          val parentActorRef = emfUriToActorRef.get(parentUri)
          
          System.err.println("[ReteActor] - parent URI: " + parentUri + " -> " + parentActorRef)
          
          subscribeToActor(parentActorRef, ReteNodeSlot.SINGLE)
        })
        
      }
    }
    
  }
  
  protected def subscribeToActor(actorRef: ActorRef, slot: ReteNodeSlot) = {
    val message = ArchUtil.slotToMessage(slot)
    
    actorRef ! message
    
    try {
      Thread.sleep(200)
    } catch {
      case e:InterruptedException => e.printStackTrace()
    }
  }
  
  protected def subscribeSender(slot: ReteNodeSlot) = {
    
    subscribers.put(sender, slot)
    
    sender ! ActorReply.SUBSCRIBED
    
    System.err.println("[ReteActor] " + self + ": Subscribed: " + sender + " on slot " + slot)
    
  }
  
  private def update(updateMessage: UpdateMessage) = {
    System.err.println("[ReteActor] " + self + ", " + reteNode.getClass().getName()
				+ ": update message received, " + updateMessage.getChangeSet().getChangeType() + " "
				+ updateMessage.getNodeSlot())
	
	var changeSet:ChangeSet = null
				
	updateMessage.getNodeSlot match {
      case ReteNodeSlot.SINGLE => {
        changeSet = reteNode.asInstanceOf[AlphaNode].update(updateMessage.getChangeSet)
      }
      case ReteNodeSlot.PRIMARY | ReteNodeSlot.SECONDARY => {
        changeSet = reteNode.asInstanceOf[BetaNode].update(updateMessage.getChangeSet, updateMessage.getNodeSlot)
      }
      case _ => {
        throw new NotImplementedException(updateMessage.getNodeSlot() + " slot is not supported.")
      }
    }
  }
  
  protected def sendToSubscribers(changeSet: ChangeSet, senderStack: Stack[ActorRef]) = {
    reteNode match{
      case node:InputNode => {
        pendingTerminationMessages = subscribers.entrySet.size
      }
    }
     
    subscribers.entrySet.foreach(entry => {
        val subscriber = entry.getKey
        val slot = entry.getValue
        
        val propagatedSenderStack = senderStack.push(self)
        val updateMessage = new UpdateMessage(changeSet, slot, propagatedSenderStack)
        
        // @formatter:off
			System.err.println("[ReteActor] " + self + ", " + reteNode.getClass().getName() + ", "
					+ ArchUtil.oneLiner(recipe.getTraceInfo()) + ": Sending to " + subscriber + "\n" 
					+ "            - " + changeSet.getChangeType() + " changeset, " + changeSet.getTuples().size() + " tuples\n"
					+ "            - " + "with sender stack: " + propagatedSenderStack + "\n" 
					+ "            - " + pendingTerminationMessages + " pending")
		// @formatter:on
					
		subscriber ! updateMessage
    })
  }
  
  private def initialize = {
    coordinatorRef = sender
    
    System.err.println("[ReteActor] " + self + ": INITIALIZE received")
    
    val node = reteNode.asInstanceOf[InitializableReteNode]
    val changeSet = node.initialize
    
    val emptyStack = Stack.empty[ActorRef]
    sendToSubscribers(changeSet, emptyStack)
  }
  
  private def doTransformation(transformation: Transformation) = {
    coordinatorRef = sender
    System.err.println("[ReteActor] " + self + ": PosLength transformation")
    
    val inputNode = reteNode.asInstanceOf[InputNode]
    val changeSets = inputNode.transform(transformation)
    val emptyStack = Stack.empty[ActorRef]
    
    changeSets.foreach(changeSet => {
      sendToSubscribers(changeSet, emptyStack)
    })
  }
  
  private def terminationProtocol (readyMessage:TerminationMessage):Unit = {
    val route = readyMessage.getRoute
    
    reteNode match{
      case node:InputNode => {
        if (route.isEmpty()) {
		  pendingTerminationMessages -= 1
		}
        
        if (pendingTerminationMessages == 0) {
		  coordinatorRef ! CoordinatorMessage.TERMINATED

		  System.err.println(coordinatorRef)

		  System.err.println("+======================================+")
		  System.err.println("|          you're terminated           |")
		  System.err.println("+======================================+")
		}
        
        return
      }
    }
    
    val pair = route.pop2
    val readyMessageTarget = pair._1
    val readyMessageSenderStack = pair._2
    
    val propagatedReadyMessage = new TerminationMessage(readyMessageSenderStack)
    readyMessageTarget ! propagatedReadyMessage
    
    System.err.println("[ReteActor] Termination protocol sending: " + readyMessageSenderStack + " to "
				+ readyMessageTarget)
				
	return
  }

  def receive = {
    case conf: ReteNodeConfiguration => configure(conf)
    case updateMessage: UpdateMessage => update(updateMessage)
    case yellowPages: YellowPages => {
      subscribe(yellowPages)
      sender ! ActorReply.YELLOWPAGES_RECEIVED
    }
    case terminationMessage: TerminationMessage => terminationProtocol(terminationMessage)
    case transformation: Transformation => doTransformation(transformation)
    case SubscriptionMessage.SUBSCRIBE_SINGLE => subscribeSender(ReteNodeSlot.SINGLE)
    case SubscriptionMessage.SUBSCRIBE_PRIMARY => subscribeSender(ReteNodeSlot.PRIMARY)
    case SubscriptionMessage.SUBSCRIBE_SECONDARY => subscribeSender(ReteNodeSlot.SECONDARY)
    case CoordinatorMessage.INITIALIZE => initialize
    case CoordinatorMessage.GETQUERYRESULTS => {
      val productionNode = reteNode.asInstanceOf[ProductionNode]
      val memory = productionNode.getMemory()
      sender ! memory
    }
  }

}