package hu.bme.mit.incqueryd.csp.util.heuristics;

public class HeuristicsHelper {
	
	public static int getEstimatedMemoryUsage(long size) {
		double memorySize = 0.0003 * size + 52.969;
		
		return ((int) Math.ceil(memorySize * 1.3));
	}

}