package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;

public final class ResponseAccountMovements extends DataResponseMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<AccountMovement> accountMovements;

	public ResponseAccountMovements(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			DataResponseCode responseCode,
			ArrayList<AccountMovement> accountMovements) {

		super(from, to, channelNumber, messageNumber, bankOnline, responseCode);
		this.accountMovements = accountMovements;

	}

	public ResponseAccountMovements() {
		super();
		accountMovements = new ArrayList<AccountMovement>();
	}

	public ArrayList<AccountMovement> getAccountMovements() {
		return accountMovements;
	}

	public void addAccountMovement(AccountMovement am) {
		accountMovements.add(am);
	}

	public int getNumberOfMovements() {
		if (accountMovements != null) {
			return accountMovements.size();
		}
		return 0;
	}

	@Override
	public String toString() {
		
		StringBuilder movementsString = new StringBuilder();
		
		for(AccountMovement am : accountMovements) {
			movementsString.append(am.toString()).append(" | ");
		}
		
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " ams:[" + movementsString.toString() + "]";
	}

}
