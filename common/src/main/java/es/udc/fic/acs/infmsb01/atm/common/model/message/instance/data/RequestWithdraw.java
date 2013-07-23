package es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;

public final class RequestWithdraw extends DataRequestMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte associatedAccountNumber;
	private short ammount;

	public RequestWithdraw(RecipientInfo from, RecipientInfo to,
			byte channelNumber, short messageNumber, boolean bankOnline,
			String cardNumber, byte associatedAccountNumber, short ammount) {

		super(from, to, channelNumber, messageNumber, bankOnline, cardNumber);
		this.associatedAccountNumber = associatedAccountNumber;
		this.ammount = ammount;

	}

	public RequestWithdraw() {
		super();
	}

	public void setAssociatedAccountNumber(byte associatedAccountNumber) {
		this.associatedAccountNumber = associatedAccountNumber;
	}

	public void setAmmount(short ammount) {
		this.ammount = ammount;
	}

	public byte getAssociatedAccountNumber() {
		return associatedAccountNumber;
	}

	public short getAmmount() {
		return ammount;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + "] " + super.toString()
				+ " aan:" + associatedAccountNumber + " a:" + ammount;
	}

}
