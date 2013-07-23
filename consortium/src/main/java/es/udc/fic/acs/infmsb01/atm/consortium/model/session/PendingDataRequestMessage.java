package es.udc.fic.acs.infmsb01.atm.consortium.model.session;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;

public final class PendingDataRequestMessage {

	private short pendingNumber;
	private DataRequestMessage message;

	public PendingDataRequestMessage(DataRequestMessage message,
			short pendingNumber) {
		
		this.pendingNumber = pendingNumber;
		this.message = message;
		
	}

	public short getPendingNumber() {
		return this.pendingNumber;
	}
	
	public DataRequestMessage getDataRequestMessage() {
		return this.message;
	}
	
	@Override
	public String toString() {
		return message.toString();
	}
	
}
