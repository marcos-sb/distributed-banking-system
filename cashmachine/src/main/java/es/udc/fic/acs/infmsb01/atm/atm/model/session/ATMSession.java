package es.udc.fic.acs.infmsb01.atm.atm.model.session;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.session.Session;

public final class ATMSession implements Session {

	private RecipientInfo consortiumID;
	private RecipientInfo atmID;
	
	public ATMSession(RecipientInfo consortiumID, RecipientInfo atmID) {
		this.consortiumID = consortiumID;
		this.atmID = atmID;
	}

	public RecipientInfo getConsortiumID() {
		return consortiumID;
	}

	public RecipientInfo getAtmID() {
		return atmID;
	}
	
}
