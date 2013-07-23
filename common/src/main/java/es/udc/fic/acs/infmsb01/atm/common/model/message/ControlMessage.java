package es.udc.fic.acs.infmsb01.atm.common.model.message;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;

public abstract class ControlMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ControlMessage(RecipientInfo from, RecipientInfo to) {
		super(from, to);
	}
	
	public ControlMessage() {}

}
