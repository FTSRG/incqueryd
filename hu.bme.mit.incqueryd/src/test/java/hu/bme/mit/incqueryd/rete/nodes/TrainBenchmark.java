package hu.bme.mit.incqueryd.rete.nodes;

import static org.junit.Assert.assertEquals;
import hu.bme.mit.incqueryd.io.GraphSonFormat;
import hu.bme.mit.incqueryd.rete.dataunits.ChangeSet;
import hu.bme.mit.incqueryd.rete.dataunits.ChangeType;
import hu.bme.mit.incqueryd.rete.dataunits.Tuple;
import hu.bme.mit.incqueryd.rete.dataunits.TupleImpl;
import hu.bme.mit.incqueryd.rete.dataunits.TupleMask;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

/**
 * Test cases for the TrainBenchmark queries. The queries are evaluated on an instance model serialized in Faunus GraphSON format. For details, see the
 * TrainBenchmark website: http://incquery.net/publications/trainbenchmark
 * 
 * See the private wiki for reference output values: https://trac.inf.mit.bme.hu/Ontology/wiki/TrainBenchmark/TBResultFormat
 * 
 * @author szarnyasg
 * 
 */
public class TrainBenchmark {

	// vertex types
	protected final String Switch = "Switch";
	protected final String Segment = "Segment";

	// edge labels
	protected final String Route_routeDefinition = "Route_routeDefinition";
	protected final String Route_switchPosition = "Route_switchPosition";
	protected final String SwitchPosition_switch = "SwitchPosition_switch";
	protected final String TrackElement_sensor = "TrackElement_sensor";

	protected final int size = 1;
	protected final String pathName = "src/test/resources/testBig_User_" + size + ".faunus-graphson";

	protected final boolean logResults = false;
	protected final boolean logMessages = false;

	final Map<String, Set<Tuple>> vertexTuplesMap = new HashMap<>();
	final Map<String, Set<Tuple>> edgeTuplesMap = new HashMap<>();

	private void load(final Collection<String> vertexTypes, final Collection<String> edgeLabels) throws IOException {
		final Multimap<String, Object> vertexTypeVertexIdsMap = ArrayListMultimap.create();
		final Map<Object, Map<String, Object>> vertexIdVertexPropertiesMap = new HashMap<>();
		final Map<String, Multimap<Object, Object>> edgeLabelVertexPairsMap = new HashMap<>();

		logMessage("Loading...");

		// collect the edges from the Faunus GraphSON file in one run
		startTimer();
		GraphSonFormat.indexGraph(pathName, vertexTypes, vertexTypeVertexIdsMap, vertexIdVertexPropertiesMap, edgeLabels, edgeLabelVertexPairsMap);

		// converting the vertices to tuples
		for (final String vertexType : vertexTypeVertexIdsMap.keySet()) {
			final Collection<Object> verticesId = vertexTypeVertexIdsMap.get(vertexType);
			final Set<Tuple> tuples = new HashSet<>();

			for (final Object vertexId : verticesId) {
				final Tuple tuple = new TupleImpl(vertexId);
				tuples.add(tuple);
			}
			vertexTuplesMap.put(vertexType, tuples);
		}

		// converting the edges to tuples
		for (final Entry<String, Multimap<Object, Object>> entry : edgeLabelVertexPairsMap.entrySet()) {
			final String edgeLabel = entry.getKey();
			final Multimap<Object, Object> edges = entry.getValue();

			final Set<Tuple> tuples = new HashSet<>();
			edgeTuplesMap.put(edgeLabel, tuples);

			for (final Object v1 : edges.keySet()) {
				final Collection<Object> v2s = edges.get(v1);

				logResult(v1 + ": " + v2s);
				for (final Object v2 : v2s) {
					final Tuple tuple = new TupleImpl(v1, v2);
					tuples.add(tuple);
					logResult(tuple.toString());
				}
			}
		}

		System.out.print("loaded, ");
		restartTimer();
	}

	@Test
	public void posLength() throws IOException {
		System.out.println("PosLength query");
	}

	@Test
	public void routeSensor() throws IOException {		
		System.out.println("RouteSensor query");
		final Collection<String> vertexTypes = ImmutableList.of();
		final Collection<String> edgeLabels = ImmutableList.of(Route_routeDefinition, Route_switchPosition, SwitchPosition_switch, TrackElement_sensor);
		load(vertexTypes, edgeLabels);

		final Set<Tuple> route_routeDefinitionTuples = edgeTuplesMap.get(Route_routeDefinition); // Route, Sensor
		final Set<Tuple> route_switchPositionTuples = edgeTuplesMap.get(Route_switchPosition); // Route, SwitchPosition
		final Set<Tuple> switchPosition_switchTuples = edgeTuplesMap.get(SwitchPosition_switch); // SwitchPosition, Switch
		final Set<Tuple> trackElement_sensorTuples = edgeTuplesMap.get(TrackElement_sensor); // Switch, Sensor
		final ChangeSet route_routeDefinitionChangeSet = new ChangeSet(route_routeDefinitionTuples, ChangeType.POSITIVE);
		final ChangeSet route_switchPositionChangeSet = new ChangeSet(route_switchPositionTuples, ChangeType.POSITIVE);
		final ChangeSet switchPosition_switchChangeSet = new ChangeSet(switchPosition_switchTuples, ChangeType.POSITIVE);
		final ChangeSet trackElement_sensorChangeSet = new ChangeSet(trackElement_sensorTuples, ChangeType.POSITIVE);
		
		
		logMessage("Route_switchPosition JOIN SwitchPosition_switch");
		logMessage("<Route, SwitchPosition, Switch>");
		final TupleMask leftMask1 = new TupleMask(ImmutableList.of(1));
		final TupleMask rightMask1 = new TupleMask(ImmutableList.of(0));
		final JoinNode joinNode1 = new JoinNode(leftMask1, rightMask1);
		final ChangeSet resultChangeSet1 = Algorithms.join(joinNode1, route_switchPositionChangeSet, switchPosition_switchChangeSet);
		logResult(resultChangeSet1.getTuples().toString());
		logMessage(resultChangeSet1.getTuples().size() + " tuples");

		logMessage("Route_switchPosition JOIN SwitchPosition_switch JOIN TrackElement_sensor");
		logMessage("<Route, SwitchPosition, Switch, Sensor>");
		final TupleMask leftMask2 = new TupleMask(ImmutableList.of(2));
		final TupleMask rightMask2 = new TupleMask(ImmutableList.of(0));
		final JoinNode joinNode2 = new JoinNode(leftMask2, rightMask2);
		final ChangeSet resultChangeSet2 = Algorithms.join(joinNode2, resultChangeSet1, trackElement_sensorChangeSet);
		logResult(resultChangeSet2.getTuples().toString());
		logMessage(resultChangeSet2.getTuples().size() + " tuples");

		logMessage("Route_switchPosition JOIN SwitchPosition_switch JOIN TrackElement_sensor JOIN Route_routeDefinition");
		logMessage("<Route, SwitchPosition, Switch, Sensor, Route>");
		final TupleMask leftMask3 = new TupleMask(ImmutableList.of(3));
		final TupleMask rightMask3 = new TupleMask(ImmutableList.of(1));
		final AntiJoinNode joinNode3 = new AntiJoinNode(leftMask3, rightMask3);
		final ChangeSet resultChangeSet3 = Algorithms.join(joinNode3, resultChangeSet2, route_routeDefinitionChangeSet);
		logResult(resultChangeSet3.getTuples().toString());
		logMessage(resultChangeSet3.getTuples().size() + " tuples");
		logBenchmark(resultChangeSet3.getTuples().size() + " tuples");

		assertEquals(resultChangeSet3.getTuples().size(), 19);
	}

	@Test
	public void signalNeighbor() throws IOException {
		System.out.println("SignalNeighbor query");
	}
	
	@Test
	public void switchSensor() throws IOException {
		System.out.println("SwitchSensor query");
		final Collection<String> vertexTypes = ImmutableList.of(Switch);
		final Collection<String> edgeLabels = ImmutableList.of(TrackElement_sensor);
		load(vertexTypes, edgeLabels);

		final Set<Tuple> switchTuples = vertexTuplesMap.get(Switch);
		final Set<Tuple> trackElement_sensorTuples = edgeTuplesMap.get(TrackElement_sensor); // Switch, Sensor
		final ChangeSet switchChangeSet = new ChangeSet(vertexTuplesMap.get(Switch), ChangeType.POSITIVE);
		final ChangeSet trackElement_sensorChangeSet = new ChangeSet(trackElement_sensorTuples, ChangeType.POSITIVE); // Switch, Sensor

		
		logMessage("Route_switchPosition JOIN SwitchPosition_switch");
		logMessage("<Route, SwitchPosition, Switch>");
		final TupleMask leftMask = new TupleMask(ImmutableList.of(0));
		final TupleMask rightMask = new TupleMask(ImmutableList.of(0));
		final AntiJoinNode anitJoinNode = new AntiJoinNode(leftMask, rightMask);
		final ChangeSet resultChangeSet = Algorithms.join(anitJoinNode, switchChangeSet, trackElement_sensorChangeSet);
		logResult(resultChangeSet.getTuples().toString());
		logBenchmark(resultChangeSet.getTuples().size() + " tuples");

		assertEquals(resultChangeSet.getTuples().size(), 26);
	}

	private long startTime;

	private void startTimer() {
		logMessage("(Re)starting timer");
		startTime = System.nanoTime();
	}

	private void restartTimer() {
		final long stopTime = System.nanoTime();
		final long deltaTime = stopTime - startTime;
		final long deltaTimeMs = deltaTime / 1000000;
		// logBenchmark("Time elapsed: ");
		logBenchmark("time [ms]: " + deltaTimeMs);
		startTimer();
	}

	private void logMessage() {
		logMessage("");
	}

	private void logMessage(final String message) {
		if (logMessages) {
			System.out.println(message);
		}
	}

	private void logResult(final String message) {
		if (logResults) {
			System.out.println(message);
		}
	}

	private void logBenchmark(final String message) {
		System.out.println(message);
	}

}