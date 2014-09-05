package hu.bme.mit.incqueryd.rete.messages;

import hu.bme.mit.incqueryd.rete.dataunits.ChangeSet;
import hu.bme.mit.incqueryd.rete.dataunits.ReteNodeSlot;
import scala.collection.immutable.Stack;
import akka.actor.ActorRef;

/**
 * 
 * @author szarnyasg
 * 
 */
public class UpdateMessage extends ReteCommunicationMessage {

	private static final long serialVersionUID = 1L;

	protected ChangeSet changeSet;
	protected ReteNodeSlot nodeSlot;

	public UpdateMessage(final ChangeSet changeSet, final ReteNodeSlot nodeSlot, final Stack<ActorRef> route) {
		super(route);
		
		this.changeSet = changeSet;
		this.nodeSlot = nodeSlot;
	}

	public ChangeSet getChangeSet() {
		return changeSet;
	}

	public ReteNodeSlot getNodeSlot() {
		return nodeSlot;
	}

	@Override
	public String toString() {
		return "UpdateMessage [changeSet=" + changeSet.getChangeType() + ", " + changeSet.getTuples() + ", nodeSlot="
				+ nodeSlot + ", route=" + route + "]";
	}

	@Override
    public boolean equals(final Object o) {
        if (!(o instanceof UpdateMessage))
            return false;
        final UpdateMessage updateMessage = (UpdateMessage) o;

        // comparing fields
        return getChangeSet().equals(updateMessage.getChangeSet()) //
        		&& getNodeSlot().equals(updateMessage.getNodeSlot()) //
        		&& getRoute().equals(updateMessage.getRoute());
    }
}
