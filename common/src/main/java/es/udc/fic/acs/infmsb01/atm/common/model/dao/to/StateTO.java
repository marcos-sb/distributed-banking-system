package es.udc.fic.acs.infmsb01.atm.common.model.dao.to;

import java.io.Serializable;

public final class StateTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6453552619228291303L;
	private StateName name;
	
	public StateTO(StateName name) {
		this.name = name;
	}
	
	public StateName getStateName() {
		return name;
	}

}
