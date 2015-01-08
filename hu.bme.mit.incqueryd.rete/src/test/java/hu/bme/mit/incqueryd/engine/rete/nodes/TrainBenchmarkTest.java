package hu.bme.mit.incqueryd.engine.rete.nodes;

import static org.junit.Assert.assertEquals;
import hu.bme.mit.incqueryd.engine.rete.dataunits.ChangeSet;
import hu.bme.mit.incqueryd.engine.rete.dataunits.ReteNodeSlot;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.incquery.runtime.rete.recipes.AntiJoinRecipe;
import org.eclipse.incquery.runtime.rete.recipes.BinaryInputRecipe;
import org.eclipse.incquery.runtime.rete.recipes.JoinRecipe;
import org.eclipse.incquery.runtime.rete.recipes.Mask;
import org.eclipse.incquery.runtime.rete.recipes.ProjectionIndexerRecipe;
import org.eclipse.incquery.runtime.rete.recipes.RecipesFactory;
import org.eclipse.incquery.runtime.rete.recipes.TrimmerRecipe;
import org.eclipse.incquery.runtime.rete.recipes.UnaryInputRecipe;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TrainBenchmarkTest {

	protected ProjectionIndexerRecipe createProjectionIndexer(final Collection<? extends Integer> mask) {
		final Mask eMask = RecipesFactory.eINSTANCE.createMask();
		eMask.setSourceArity(mask.size());
		eMask.getSourceIndices().addAll(mask);
		final ProjectionIndexerRecipe indexer = RecipesFactory.eINSTANCE.createProjectionIndexerRecipe();
		indexer.setMask(eMask);
		return indexer;
	}

	@Test
	public void posLengthTest() throws IOException {
		// input nodes
		TypeInputNode segmentLengthInputNode = createAttributeInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.SEGMENT_LENGTH);
		segmentLengthInputNode.load();
		ChangeSet segmentLengthChangeSet = segmentLengthInputNode.getChangeSet();
		
		assertEquals(4835, segmentLengthChangeSet.size());
	}
	
	private TypeInputNode createAttributeInputNode(String type) {
		BinaryInputRecipe recipe = RecipesFactory.eINSTANCE.createBinaryInputRecipe();
		recipe.setTypeName(type);
		recipe.setTraceInfo("attribute");
		TypeInputNode node = new TypeInputNode(recipe);
		return node;
	}

	@Test
	public void routeSensorTest() throws IOException {
		// input nodes
		TypeInputNode switchPositionNode = createEdgeInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.SWITCHPOSITION_SWITCH);
		TypeInputNode routeSwitchPositionNode = createEdgeInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.ROUTE_SWITCHPOSITION);
		TypeInputNode trackElementSensorNode = createEdgeInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.TRACKELEMENT_SENSOR);
		TypeInputNode routeRouteDefinitionNode = createEdgeInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.ROUTE_ROUTEDEFINITION);

		// join node: <SwP, Sw, R>
		JoinNode joinNode1 = createJoinNode(ImmutableList.of(0), ImmutableList.of(1));

		// antijoin node: <SwP, Sw, R, Sen>
		JoinNode joinNode2 = createJoinNode(ImmutableList.of(1), ImmutableList.of(0));

		// antijoin node: <SwP, Sw, R, Sen>
		AntiJoinNode antiJoinNode = createAntiJoinNode(ImmutableList.of(2, 3), ImmutableList.of(0, 1));

		// trimmer node: <R>
		TrimmerNode trimmerNode = createTrimmerNode(ImmutableList.of(3));
		
		// sending the changesets
		switchPositionNode.load();
		routeSwitchPositionNode.load();
		trackElementSensorNode.load();
		routeRouteDefinitionNode.load();
		
		ChangeSet switchPositionChangeSet = switchPositionNode.getChangeSet();
		ChangeSet routeSwitchPositionChangeSet = routeSwitchPositionNode.getChangeSet();
		ChangeSet trackElementSensorChangeSet = trackElementSensorNode.getChangeSet();
		ChangeSet routeRouteDefinitionChangeSet = routeRouteDefinitionNode.getChangeSet();
		
		// joinNode1
		ChangeSet cs1 = joinNode1.update(switchPositionChangeSet, ReteNodeSlot.PRIMARY); // empty
		ChangeSet cs2 = joinNode1.update(routeSwitchPositionChangeSet, ReteNodeSlot.SECONDARY);
		
		// joinNode2
		ChangeSet cs3 = joinNode2.update(cs2, ReteNodeSlot.PRIMARY); // empty
		ChangeSet cs4 = joinNode2.update(trackElementSensorChangeSet, ReteNodeSlot.SECONDARY);

		// antiJoin
		ChangeSet cs5 = antiJoinNode.update(routeRouteDefinitionChangeSet, ReteNodeSlot.SECONDARY); // empty
		ChangeSet cs6 = antiJoinNode.update(cs4, ReteNodeSlot.PRIMARY);
		
		// trimmer
		ChangeSet cs7 = trimmerNode.update(cs6);
		
		assertEquals(94, cs7.size());
	}

	@Test
	public void switchSensorTest() throws IOException {
		
		// input node for switch vertices
		TypeInputNode switchInputNode = createVertexInputNode(TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.SWITCH);
		
		// input node for trackElement_sensor edges
		String type = TrainBenchmarkConstants.TRAINBENCHMARK_BASE + TrainBenchmarkConstants.TRACKELEMENT_SENSOR;
		TypeInputNode trackElementSensorNode = createEdgeInputNode(type);

		// antijoin node
		AntiJoinRecipe antiJoinRecipe = RecipesFactory.eINSTANCE.createAntiJoinRecipe();
		ProjectionIndexerRecipe leftProjectionIndexer = createProjectionIndexer(ImmutableList.of(0));
		ProjectionIndexerRecipe rightProjectionIndexer = createProjectionIndexer(ImmutableList.of(0));
		antiJoinRecipe.setLeftParent(leftProjectionIndexer);
		antiJoinRecipe.setRightParent(rightProjectionIndexer);
		AntiJoinNode antiJoinNode = new AntiJoinNode(antiJoinRecipe);

		// sending the changesets
		switchInputNode.load();
		ChangeSet switchChangeSet = switchInputNode.getChangeSet();
		trackElementSensorNode.load();
		ChangeSet trackElementSensorChangeSet = trackElementSensorNode.getChangeSet();
		
		// secondary changeset
		ChangeSet cs1 = antiJoinNode.update(trackElementSensorChangeSet, ReteNodeSlot.SECONDARY);

		// primary changeset
		ChangeSet cs2 = antiJoinNode.update(switchChangeSet, ReteNodeSlot.PRIMARY);

		assertEquals(19, cs2.size());
	}

	private TypeInputNode createVertexInputNode(String typeName) {
		UnaryInputRecipe switchRecipe = RecipesFactory.eINSTANCE.createUnaryInputRecipe();
		switchRecipe.setTypeName(typeName);
		TypeInputNode switchInputNode = new TypeInputNode(switchRecipe);
		return switchInputNode;
	}

	private TrimmerNode createTrimmerNode(ImmutableList<Integer> mask) {
		TrimmerRecipe trimmerRecipe = RecipesFactory.eINSTANCE.createTrimmerRecipe();
		Mask eMask = RecipesFactory.eINSTANCE.createMask();
		eMask.getSourceIndices().addAll(mask);
		trimmerRecipe.setMask(eMask);
		TrimmerNode trimmerNode = new TrimmerNode(trimmerRecipe);
		return trimmerNode;
	}

	private AntiJoinNode createAntiJoinNode(ImmutableList<Integer> primaryMask, ImmutableList<Integer> secondaryMask) {
		AntiJoinRecipe antiJoinRecipe = RecipesFactory.eINSTANCE.createAntiJoinRecipe();
		antiJoinRecipe.setLeftParent(createProjectionIndexer(primaryMask));
		antiJoinRecipe.setRightParent(createProjectionIndexer(secondaryMask));
		AntiJoinNode antiJoinNode = new AntiJoinNode(antiJoinRecipe);
		return antiJoinNode;
	}

	private JoinNode createJoinNode(ImmutableList<Integer> primaryMask, ImmutableList<Integer> secondaryMask) {
		JoinRecipe joinRecipe1 = RecipesFactory.eINSTANCE.createJoinRecipe();
		joinRecipe1.setLeftParent(createProjectionIndexer(primaryMask));
		joinRecipe1.setRightParent(createProjectionIndexer(secondaryMask));
		JoinNode joinNode1 = new JoinNode(joinRecipe1);
		return joinNode1;
	}

	private TypeInputNode createEdgeInputNode(String type) throws IOException {
		BinaryInputRecipe recipe = RecipesFactory.eINSTANCE.createBinaryInputRecipe();
		recipe.setTypeName(type);
		recipe.setTraceInfo("edge");
		TypeInputNode node = new TypeInputNode(recipe);
		return node;
	}

}