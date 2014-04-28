package hu.bme.mit.incqueryd.cache;

import hu.bme.mit.incqueryd.cache.hazelcast.HazelcastCache;
import hu.bme.mit.incqueryd.cache.local.LocalCache;
import hu.bme.mit.incqueryd.rete.dataunits.Tuple;

import java.util.List;
import java.util.Set;

public class TupleCache {

	private final boolean distributed;
	private final DistributedCache cache;

	public TupleCache(final List<String> cacheMachineIps) {
		distributed = !cacheMachineIps.isEmpty();
		if (distributed) {
			cache = new HazelcastCache(cacheMachineIps);
		} else {
			cache = new LocalCache();
		}
	}

	public boolean isDistributed() {
		return distributed;
	}
	
	public Set<Tuple> getSet(String typename) {
		return cache.getSet(typename);
	}
	
	public DistributedMultiMap<Tuple, Tuple> getMultiMap(String nodeId) {
		return cache.getMultiMap(nodeId);
	}

}