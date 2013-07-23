package es.udc.fic.acs.infmsb01.atm.common.model.message;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;

public abstract class DataMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte channelNumber;
	private short messageNumber;
	private boolean bankOnline;

	public DataMessage(RecipientInfo from, RecipientInfo to, byte channelNumber,
			short messageNumber, boolean bankOnline) {
		super(from, to);
		this.bankOnline = bankOnline;
		this.channelNumber = channelNumber;
		this.messageNumber = messageNumber;
	}
	
	public DataMessage() {}

	public byte getChannelNumber() {
		return channelNumber;
	}

	public short getMessageNumber() {
		return messageNumber;
	}

	public boolean isBankOnline() {
		return bankOnline;
	}

	public void setChannelNumber(byte channelNumber) {
		this.channelNumber = channelNumber;
	}

	public void setMessageNumber(short messageNumber) {
		this.messageNumber = messageNumber;
	}

	public void setBankOnline(boolean bankOnline) {
		this.bankOnline = bankOnline;
	}
	
	@Override
	public String toString() {
		return super.toString() + " online:" + bankOnline + " cn:" + channelNumber + " mn:" + messageNumber;
	}

}
