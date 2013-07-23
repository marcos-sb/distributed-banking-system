package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;

public final class RequestAccountMovements extends DataRequestMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte associatedAccountNumber;

	public RequestAccountMovements(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			String cardNumber, byte associatedAccountNumber) {

		super(from, to, channelNumber, messageNumber, bankOnline, cardNumber);
		this.associatedAccountNumber = associatedAccountNumber;
	}

	public RequestAccountMovements() {
		super();
	}

	public byte getAssociatedAccountNumber() {
		return associatedAccountNumber;
	}

	public void setAssociatedAccountNumber(byte associatedAccountNumber) {
		this.associatedAccountNumber = associatedAccountNumber;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " aan:" + associatedAccountNumber;
	}

}
