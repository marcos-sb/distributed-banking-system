package es.udc.fic.acs.infmsb01.atm.common.model.message;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;

public abstract class DataRequestMessage extends DataMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardNumber;

	public DataRequestMessage(RecipientInfo from, RecipientInfo to, byte channelNumber,
			short messageNumber, boolean bankOnline, String cardNumber) {

		super(from, to, channelNumber, messageNumber, bankOnline);
		this.cardNumber = cardNumber;

	}
	
	public DataRequestMessage() {}

	public String getCardNumber() {
		return cardNumber;
	}
	
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Override
	public String toString() {
		return super.toString() + " card:" + cardNumber;
	}

}
