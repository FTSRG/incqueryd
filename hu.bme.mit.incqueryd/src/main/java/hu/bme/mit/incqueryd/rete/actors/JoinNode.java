//package hu.bme.mit.incqueryd.rete.actors;
//
//import incqueryd.retenodes.messages.UpdateMessage;
//import incqueryd.retenodes.messages.UpdateType;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import distributed.rete.datastructure.JoinSide;
//import distributed.rete.datastructure.Tuple;
//import distributed.rete.datastructure.TupleImpl;
//
///**
// * The first kind of beta node is the JoinNode, that basically calculates the natural join of the contents of its
// * parents. This is probably the single most important element of the RETE network; most related work contains a node
// * with similar functionality, sometimes referred to as an AND node (as it enforces that two conditions must be met). As
// * with the rules of natural join, the contents of this node are tuples that combine two tuples (one from each input
// * slot) whose signature matches (each signature generated by the mask of the appropriate slot). The combined tuple
// * contains all elements of the tuples it was created from; but includes only one instance of those elements that were
// * selected by the pattern masks and matched to be equal on both sides. Whenever an update arrives from one of the input
// * slots, tuples with the same signature are retrieved from the other Indexer. Then each of them is combined with the
// * incoming tuple and the result is propagated to the children of the JoinNode. [Bergmann's MSc thesis, p.38]
// * 
// * @author szarnyasg
// * 
// */
//public class JoinNode extends AbstractJoinNode {
//
//	public JoinNode() {
//		super();
//	}
//
//	/**
//	 * Join the newly arrived tuples to the old ones in the opposite side's indexer.
//	 * 
//	 * @return
//	 */
//	@Override
//	protected UpdateMessage joinNewTuples(Set<Tuple> newTuples, JoinSide joinSide, UpdateType updateType) {
//		// int n = 0;
//
//		Indexer newTuplesIndexer = joinSide == JoinSide.PRIMARY ? leftIndexer : rightIndexer;
//		Indexer existingTuplesIndexer = joinSide == JoinSide.PRIMARY ? rightIndexer : leftIndexer;
//
//		// save the new tuples to the indexer's memory
//		newTuplesIndexer.add(newTuples);
//
//		// TODO: investigate why using a HashSet introduces an ugly heisenbug in
//		// the code
//		// Set<Tuple> result = new HashSet<>();
//		List<Tuple> result = new ArrayList<>();
//		List<Integer> rightTupleMask = rightIndexer.getJoinMask().getMask();
//
//		// logger.info("[" + getSelf().path() + "] Join side is: " +
//		// joinSide);
//		// logger.info("[" + getSelf().path() + "] Tuples added: " +
//		// newTuples.size());
//		// logger.info("[" + getSelf().path() +
//		// "] NewTuplesIndexer size is " + newTuplesIndexer.getSize() +
//		// " mask is: "
//		// + newTuplesIndexer.getJoinMask().getMask() + ", hashCode is: " +
//		// newTuplesIndexer);
//		// logger.info("[" + getSelf().path() +
//		// "] ExistingTuplesIndexer size is " + existingTuplesIndexer.getSize()
//		// + " mask is: "
//		// + existingTuplesIndexer.getJoinMask().getMask() + ", hashCode is: " +
//		// existingTuplesIndexer);
//		// logger.info(existingTuplesIndexer.getMap());
//
//		for (Tuple newTuple : newTuples) {
//			Tuple extractedTuple = newTuplesIndexer.getJoinMask().extract(newTuple);
//			Set<Tuple> matchingTuples = existingTuplesIndexer.get(extractedTuple);
//
//			// logger.info(extractedTuple);
//
//			// for each matching tuple, create a result tuple
//			for (Tuple matchingTuple : matchingTuples) {
//				int size = newTuple.size() - extractedTuple.size() + matchingTuple.size();
//				Object[] resultTuple = new Object[size];
//
//				// assemble the result tuple
//				Tuple leftTuple = joinSide == JoinSide.PRIMARY ? newTuple : matchingTuple;
//				Tuple rightTuple = joinSide == JoinSide.PRIMARY ? matchingTuple : newTuple;
//
//				// copy from the left tuple
//				for (int i = 0; i < leftTuple.size(); i++) {
//					resultTuple[i] = leftTuple.get(i);
//				}
//
//				// copy from the right tuple -- skip the duplicate attributes
//				int j = 0;
//				for (int i = 0; i < rightTuple.size(); i++) {
//					if (!rightTupleMask.contains(i)) {
//						resultTuple[leftTuple.size() + j] = rightTuple.get(i);
//						j++;
//					}
//				}
//
//				Tuple tuple = new TupleImpl(resultTuple);
//				// logger.info(tuple);
//				result.add(tuple);
//				// n++;
//				// logger.info("n = " + n + ", result size = " +
//				// result.size());
//			}
//
//		}
//
//		UpdateMessage propagatedUpdateMessage = null;
//		if (!result.isEmpty() && targetActor != null) {
//			propagatedUpdateMessage = new UpdateMessage(updateType, nextJoinSide, result);
//		}
//
//		return propagatedUpdateMessage;
//	}
//}
