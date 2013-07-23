package es.udc.fic.acs.infmsb01.atm.common.model.message;

import java.io.Serializable;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;

public abstract class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RecipientInfo from;
	private RecipientInfo to;

	public Message(RecipientInfo from, RecipientInfo to) {
		this.from = from;
		this.to = to;
	}
	
	public Message() {}

	public RecipientInfo getFrom() {
		return from;
	}

	public RecipientInfo getTo() {
		return to;
	}

	public void setFrom(RecipientInfo from) {
		this.from = from;
	}

	public void setTo(RecipientInfo to) {
		this.to = to;
	}
	
	@Override
	public String toString() {
		return from.getId() + " -> " + to.getId();
	}

}
