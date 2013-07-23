package es.udc.fic.acs.infmsb01.atm.consortium.model.session;

import java.util.ArrayList;
import java.util.ListIterator;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.session.FAPSession;

public final class ConsortiumSession extends FAPSession {

	private RecipientInfo consortium;
	private RecipientInfo bank;
	private ArrayList<PendingDataRequestMessage> pendingMessages;
	private short pendingNumber;

	public ConsortiumSession(RecipientInfo consortiumID, RecipientInfo bankID,
			byte numberOfChannels) {
		
		super(numberOfChannels);
		this.consortium = consortiumID;
		this.bank = bankID;
		this.pendingMessages = new ArrayList<PendingDataRequestMessage>();
		this.pendingNumber = 0;
	}

	public ConsortiumSession(RecipientInfo consortiumID, RecipientInfo bankID) {
		super();
		this.consortium = consortiumID;
		this.bank = bankID;
		this.pendingMessages = new ArrayList<PendingDataRequestMessage>();
		this.pendingNumber = 0;
	}

	public final boolean isBankOnline() {
		StateName sessionState = getContextState().getStateName();
		return isDataPipelineOnline()
				&& !sessionState.equals(StateName.OFFLINE)
				&& !sessionState.equals(StateName.TRAFFICSTOPPED);
	}

	public final void setNumberOfChannels(byte numberOfChannels) {
		super.setNumberOfChannels(numberOfChannels);
	}

	public RecipientInfo getConsortium() {
		return consortium;
	}

	public RecipientInfo getBank() {
		return bank;
	}

	public void setBank(RecipientInfo bankID, byte numberOfChannels) {
		this.bank = bankID;
		super.setNumberOfChannels(numberOfChannels);
	}

	public PendingDataRequestMessage appendPendingMessage(
			DataRequestMessage message) {

		PendingDataRequestMessage pendingMessage = new PendingDataRequestMessage(
				message, pendingNumber++);
		pendingMessages.add(pendingMessage);

		return pendingMessage;
	}

	public void appendPendingMessage(PendingDataRequestMessage pendingMessage) {
		pendingMessages.add(pendingMessage);
	}

	public PendingDataRequestMessage removePendingMessage(DataRequestMessage message) {
		PendingDataRequestMessage pending = null;
		synchronized (pendingMessages) {
			ListIterator<PendingDataRequestMessage> it = pendingMessages
					.listIterator();
			
			while(it.hasNext()) {
				pending = it.next();
				DataRequestMessage pendingPayload = pending.getDataRequestMessage();
				
				if(pendingPayload.equals(message)) {
					it.remove();
					return pending;
				}
			}
			
			pendingMessages.notify();
		}
		
		return pending;
	}

	public ArrayList<PendingDataRequestMessage> getPendingMessages() {
		return this.pendingMessages;
	}

	public final int getPendingSize() {
		return this.pendingMessages.size();
	}

	@Override
	public void appendMessage(DataRequestMessage message)
			throws BusyChannelException {

		byte channelNumber = getAvailableChannelNumber();
		message.setChannelNumber(channelNumber);
		message.setMessageNumber(getNewRequestNumber(channelNumber));

		super.appendMessage(message);

	}

	@Override
	public boolean equals(Object obj) {
		return ((ConsortiumSession) obj).bank.equals(this.bank);
	}

}
