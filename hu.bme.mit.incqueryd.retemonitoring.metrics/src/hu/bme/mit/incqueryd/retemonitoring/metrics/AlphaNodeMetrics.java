package hu.bme.mit.incqueryd.retemonitoring.metrics;


public class AlphaNodeMetrics extends ReteNodeMetrics {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AlphaNodeMetrics(String reteNode, String hostName,
			int updateMessagesSent, int changesCount) {
		super(reteNode, hostName, updateMessagesSent, changesCount);
	}

}
