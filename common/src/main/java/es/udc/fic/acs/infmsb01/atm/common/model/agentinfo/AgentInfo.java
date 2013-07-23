package es.udc.fic.acs.infmsb01.atm.common.model.agentinfo;

import java.io.Serializable;

public abstract class AgentInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer port;
	private String ip;
	
	public AgentInfo(String id, String ip, int port) {
		this.id = id;
		this.port = port;
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AgentInfo() {}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		return id + "@" + ip + ":" + port;
	}

}
