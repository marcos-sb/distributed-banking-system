package es.udc.fic.acs.infmsb01.atm.bank.model.session;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.session.FAPSession;

public final class BankSession extends FAPSession {
	
	private RecipientInfo consortium;
	private RecipientInfo bank;
	
	public BankSession(RecipientInfo consortiumID, RecipientInfo bankID, byte numberOfChannels) {
		super(numberOfChannels);
		this.consortium = consortiumID;
		this.bank = bankID;
	}

	public RecipientInfo getConsortium() {
		return consortium;
	}

	public RecipientInfo getBank() {
		return bank;
	}
	
	public void setNumberOfChannels(byte numberOfChannels) {
		super.setNumberOfChannels(numberOfChannels);
	}
	
}
