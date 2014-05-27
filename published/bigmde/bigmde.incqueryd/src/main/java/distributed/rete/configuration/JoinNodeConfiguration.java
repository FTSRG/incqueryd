package distributed.rete.configuration;

import distributed.rete.datastructure.JoinSide;
import distributed.rete.datastructure.TupleMask;
import akka.actor.ActorRef;

public class JoinNodeConfiguration extends ReteNodeConfiguration {

	private static final long serialVersionUID = 1L;

	public JoinNodeConfiguration(ActorRef coordinator, TupleMask leftMask, TupleMask rightMask, String targetActorPath,
			JoinSide targetJoinSide) {
		super(coordinator, targetActorPath);
		this.leftMask = leftMask;
		this.rightMask = rightMask;
		this.targetJoinSide = targetJoinSide;
	}

	public TupleMask leftMask;
	public TupleMask rightMask;
	public JoinSide targetJoinSide;

}