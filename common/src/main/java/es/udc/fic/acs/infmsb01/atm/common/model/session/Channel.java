package es.udc.fic.acs.infmsb01.atm.common.model.session;

import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;

class Channel {

	private ArrayList<DataRequestMessage> requests;
	private ArrayList<DataResponseMessage> responses;
	private short currentRequestNumber;
	private short currentResponseNumber;
	private boolean available;
	private RecipientInfo atm;

	Channel() {
		this.requests = new ArrayList<DataRequestMessage>();
		this.responses = new ArrayList<DataResponseMessage>();
		this.currentRequestNumber = 0;
		this.currentResponseNumber = 0;
		this.available = true;
		atm = null;
	}

	short getCurrentRequestNumber() {
		return currentRequestNumber;
	}

	short getCurrentResponseNumber() {
		return currentResponseNumber;
	}

	boolean isAvailable() {
		return available;
	}

	void appendMessage(DataRequestMessage message) throws BusyChannelException {
		if (!available) {
			throw new BusyChannelException();
		}
		requests.add(message);
		currentRequestNumber = message.getMessageNumber();
		available = false;

	}

	boolean appendMessage(DataResponseMessage message) {
		if (!available) {
			responses.add(message);
			currentResponseNumber = message.getMessageNumber();
			available = true;
			return true;
		}
		return false;
	}

	DataRequestMessage getCurrentRequestMessage() {
		if(currentRequestNumber > 0) {
			return requests.get(requests.size() - 1);
		}
		return null;
	}

	DataResponseMessage getCurrentResponseMessage() {
		if(currentResponseNumber > 0) {
			return responses.get(responses.size() - 1);
		}
		return null;
	}

	RecipientInfo getATM() {
		return atm;
	}

	void setATM(RecipientInfo atm) {
		this.atm = atm;
	}

}
