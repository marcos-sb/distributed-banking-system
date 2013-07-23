package es.udc.fic.acs.infmsb01.atm.common.model.agentinfo;

public final class BankInfo extends AgentInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankInfo(String id, String ip, int port) {
		super(id, ip, port);
	}
	
	public BankInfo(BankInfo bi) {
		super(bi.getId(), bi.getIp(), bi.getPort());
	}
	
	public BankInfo() {
		super();
	}
	
}
