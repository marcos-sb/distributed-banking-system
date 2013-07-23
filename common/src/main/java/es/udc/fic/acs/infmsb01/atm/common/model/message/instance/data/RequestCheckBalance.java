package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;

public final class RequestCheckBalance extends DataRequestMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte associatedAccountNumber;

	public RequestCheckBalance(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			String cardNumber, byte associatedAccountNumber) {

		super(from, to, channelNumber, messageNumber, bankOnline, cardNumber);
		this.associatedAccountNumber = associatedAccountNumber;

	}

	public RequestCheckBalance() {
		super();
	}

	public void setAssociatedAccountNumber(byte associatedAccountNumber) {
		this.associatedAccountNumber = associatedAccountNumber;
	}

	public byte getAssociatedAccountNumber() {
		return associatedAccountNumber;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " aan:" + associatedAccountNumber;
	}

}
