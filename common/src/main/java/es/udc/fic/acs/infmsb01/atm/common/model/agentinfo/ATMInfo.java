package es.udc.fic.acs.infmsb01.atm.common.model.agentinfo;

public final class ATMInfo extends AgentInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ATMInfo(String id, String ip, int port) {
		super(id, ip, port);
	}
	
	public ATMInfo(ATMInfo atmInfo) {
		super(atmInfo.getId(), atmInfo.getIp(), atmInfo.getPort());
	}
	
	public ATMInfo() {
		super();
	}

}
