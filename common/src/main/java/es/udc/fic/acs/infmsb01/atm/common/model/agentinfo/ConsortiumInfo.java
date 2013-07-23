package es.udc.fic.acs.infmsb01.atm.common.model.agentinfo;

public final class ConsortiumInfo extends AgentInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer bankPort;

	public ConsortiumInfo(String id, String ip, int atmPort, int bankPort) {
		super(id, ip, atmPort);
		this.bankPort = bankPort;
	}
	
	public ConsortiumInfo(ConsortiumInfo ci) {
		super(ci.getId(), ci.getIp(), ci.getPort());
		this.bankPort = ci.bankPort;
	}
	
	public ConsortiumInfo() {
		super();
	}
	
	public final Integer getBankPort() {
		return this.bankPort;
	}
	
	public final void setBankPort(int bankPort) {
		this.bankPort = bankPort;
	}

}
