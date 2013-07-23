package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;

public final class RequestAccountsTransfer extends DataRequestMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte sourceAccountNumber;
	private byte destinationAccountNumber;
	private short ammount;

	public RequestAccountsTransfer(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			String cardNumber, byte sourceAccountNumber,
			byte destinationAccountTransfer, short ammount) {

		super(from, to, channelNumber, messageNumber, bankOnline, cardNumber);
		this.sourceAccountNumber = sourceAccountNumber;
		this.destinationAccountNumber = destinationAccountTransfer;
		this.ammount = ammount;

	}

	public RequestAccountsTransfer() {
		super();
	}

	public void setSourceAccountNumber(byte sourceAccountNumber) {
		this.sourceAccountNumber = sourceAccountNumber;
	}

	public void setDestinationAccountNumber(byte destinationAccountNumber) {
		this.destinationAccountNumber = destinationAccountNumber;
	}

	public void setAmmount(short ammount) {
		this.ammount = ammount;
	}

	public byte getSourceAccountNumber() {
		return sourceAccountNumber;
	}

	public byte getDestinationAccountNumber() {
		return destinationAccountNumber;
	}

	public short getAmmount() {
		return ammount;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " an:" + sourceAccountNumber + " -[" + ammount + "]-> an:"
				+ destinationAccountNumber;
	}

}
