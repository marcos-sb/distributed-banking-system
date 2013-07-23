package es.udc.fic.acs.infmsb01.atm.common.model.session;

import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;

class DataPipeline {

	private ArrayList<Channel> channels;
	private byte numberOfChannels;
	private long sumDeposits;
	private long sumWithdrawals;
	private long sumTransfers;

	DataPipeline(byte numberOfChannels) {
		this.numberOfChannels = numberOfChannels;
		this.channels = new ArrayList<Channel>(numberOfChannels);

		for (int i = 0; i < numberOfChannels; i++) {
			Channel c = new Channel();
			channels.add(c);
		}

		sumDeposits = 0;
		sumWithdrawals = 0;
		sumTransfers = 0;
	}

	ArrayList<DataMessage> getAllCurrentMessages() {
		ArrayList<DataMessage> drms = new ArrayList<DataMessage>();
		for (Channel c : channels) {
			if (c.getCurrentRequestNumber() > 0) {
				drms.add(c.getCurrentRequestMessage());
			}
			if (c.getCurrentResponseNumber() > 0) {
				drms.add(c.getCurrentResponseMessage());
			}
		}
		return drms;
	}

	public ArrayList<DataRequestMessage> getAllCurrentRequests() {
		ArrayList<DataRequestMessage> drms = new ArrayList<DataRequestMessage>();
		for (Channel c : channels) {
			if (c.getCurrentRequestNumber() > 0) {
				drms.add(c.getCurrentRequestMessage());
			}
		}
		return drms;
	}

	DataRequestMessage getCurrentRequestMessage(byte channelNumber) {
		return channels.get(channelNumber-1).getCurrentRequestMessage();
	}

	DataResponseMessage getCurrentResponseMessage(byte channelNumber) {
		return channels.get(channelNumber-1).getCurrentResponseMessage();
	}

	boolean isDataPipelineOnline() {
		return numberOfChannels > 0;
	}

	void setNumberOfChannels(byte numberOfChannels) {
		int newChannels = numberOfChannels - this.numberOfChannels;
		int length = channels.size();
		if (newChannels > 0) {
			for (int i = 0; i < newChannels; i++) {
				Channel c = new Channel();
				channels.add(c);
			}
		} else {
			for (int i = 0; i > newChannels; i--) {
				channels.remove(length - 1 + i);
			}
		}
		this.numberOfChannels = (byte) newChannels;
	}

	long getSumDeposits() {
		return sumDeposits;
	}

	void setSumDeposits(long sumDeposits) {
		this.sumDeposits = sumDeposits;
	}

	long getSumWithdrawals() {
		return sumWithdrawals;
	}

	void setSumWithdrawals(long sumWithdrawals) {
		this.sumWithdrawals = sumWithdrawals;
	}

	long getSumTransfers() {
		return sumTransfers;
	}

	void setSumTransfers(long sumTransfers) {
		this.sumTransfers = sumTransfers;
	}

	byte getNumberOfChannels() {
		return numberOfChannels;
	}

	RecipientInfo getATM(byte channelNumber) {
		return channels.get(channelNumber-1).getATM();
	}

	void setATM(byte channelNumber, RecipientInfo atm) {
		channels.get(channelNumber-1).setATM(atm);
	}

	short getCurrentRequest(byte channelNumber) {
		return channels.get(channelNumber-1).getCurrentRequestNumber();
	}

	short getCurrentResponse(byte channelNumber) {
		return channels.get(channelNumber-1).getCurrentResponseNumber();
	}

	boolean isAvailable(byte channelNumber) {
		return channels.get(channelNumber-1).isAvailable();
	}

	void appendMessage(DataRequestMessage message) throws BusyChannelException {
		channels.get(message.getChannelNumber()-1).appendMessage(message);
	}

	boolean appendMessage(DataResponseMessage message) {
		synchronized (this) {
			boolean inserted = channels.get(message.getChannelNumber()-1).appendMessage(message);
			this.notify();
			return inserted;
		}
	}

	byte getAvailableChannelNumber() {
		while (true) {
			synchronized (this) {
				byte i = 1;
				for (Channel c : channels) {
					if (c.isAvailable()) {
						return i;
					}
					i++;
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
