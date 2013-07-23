package es.udc.fic.acs.infmsb01.atm.common.model.session;

import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateTO;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.session.DataPipeline;
import es.udc.fic.acs.infmsb01.atm.common.model.session.state.Context;

public abstract class FAPSession implements Session {

	private DataPipeline dataPipeline;
	private Context context;
	private SmartTimer smartTimer;

	public FAPSession(byte numberOfChannels) {
		this.context = new Context();
		this.dataPipeline = new DataPipeline(numberOfChannels);
		this.smartTimer = new SmartTimer(5000);
	}

	public FAPSession() {
		this.context = new Context();
		this.dataPipeline = new DataPipeline((byte) 0);
		this.smartTimer = new SmartTimer(5000);
	}

	public void resetSession() {
		context = new Context();
		this.dataPipeline = new DataPipeline((byte) 0);
		this.smartTimer = new SmartTimer(5000);
	}

	public SmartTimer getSmartTimer() {
		return this.smartTimer;
	}

	public ArrayList<DataMessage> getAllCurrentMessages() {
		return dataPipeline.getAllCurrentMessages();
	}

	public ArrayList<DataRequestMessage> getAllCurrentRequests() {
		return dataPipeline.getAllCurrentRequests();
	}

	protected void setNumberOfChannels(byte numberOfChannels) {
		if (this.dataPipeline == null) {
			this.dataPipeline = new DataPipeline(numberOfChannels);
		} else {
			dataPipeline.setNumberOfChannels(numberOfChannels);
		}
	}

	public short getCurrentRequestNumber(byte channelNumber) {
		return dataPipeline.getCurrentRequest(channelNumber);
	}

	public DataRequestMessage getCurrentRequest(byte channelNumber) {
		return dataPipeline.getCurrentRequestMessage(channelNumber);
	}

	public DataResponseMessage getCurrentResponse(byte channelNumber) {
		return dataPipeline.getCurrentResponseMessage(channelNumber);
	}

	public DataRequestMessage getRequest(DataResponseMessage message) {
		short currentRequestNumber = dataPipeline.getCurrentRequest(message
				.getChannelNumber());
		short currentResponseNumber = dataPipeline.getCurrentResponse(message
				.getChannelNumber());

		if (currentRequestNumber == currentResponseNumber
				&& message.getMessageNumber() == currentResponseNumber) {
			
			return dataPipeline.getCurrentRequestMessage(message
					.getChannelNumber());
		}

		return null;
	}

	public DataResponseMessage getResponse(DataRequestMessage message) {
		short currentRequestNumber = dataPipeline.getCurrentRequest(message
				.getChannelNumber());
		short currentResponseNumber = dataPipeline.getCurrentResponse(message
				.getChannelNumber());

		if (currentRequestNumber == currentResponseNumber
				&& message.getMessageNumber() == currentRequestNumber) {
			
			return dataPipeline.getCurrentResponseMessage(message
					.getChannelNumber());
		}

		return null;
	}

	protected boolean isDataPipelineOnline() {
		return dataPipeline.isDataPipelineOnline();
	}

	public RecipientInfo getATM(byte channelNumber) {
		return dataPipeline.getATM(channelNumber);
	}

	public void setATM(byte channelNumber, RecipientInfo atm) {
		dataPipeline.setATM(channelNumber, atm);
	}

	public void setContextState(StateTO state) {
		context.setState(state);
	}

	public StateTO getContextState() {
		return context.getStateTO();
	}

	public void acceptDataMessage() throws InvalidStateException {
		context.acceptDataMessage();
	}

	public void requestOpenSession() throws InvalidStateException {
		context.requestOpenSession();
	}

	public void deniedOpenSession() throws InvalidStateException {
		context.deniedOpenSession();
	}

	public void openSessionAccepted() throws InvalidStateException {
		context.openSessionAccepted();
	}

	public void requestStopTraffic() throws InvalidStateException {
		context.requestStopTraffic();
	}

	public void deniedStopTraffic() throws InvalidStateException {
		context.deniedStopTraffic();
	}

	public void stopTrafficAccepted() throws InvalidStateException {
		context.stopTrafficAccepted();
	}

	public void requestRestartTraffic() throws InvalidStateException {
		context.requestRestartTraffic();
	}

	public void deniedRestartTraffic() throws InvalidStateException {
		context.deniedRestartTraffic();
	}

	public void restartTrafficAccepted() throws InvalidStateException {
		context.restartTrafficAccepted();
	}

	public void requestCloseSession() throws InvalidStateException {
		context.requestCloseSession();
	}

	public void deniedCloseSession() throws InvalidStateException {
		context.deniedCloseSession();
	}

	public void closeSessionAccepted() throws InvalidStateException {
		context.closeSessionAccepted();
	}

	public void requestRecovery() throws InvalidStateException {
		context.requestRecovery();
	}

	public void deniedRecovery() throws InvalidStateException {
		context.deniedRecovery();
	}

	public void requestEndRecovery() throws InvalidStateException {
		context.requestEndRecovery();
	}

	public void deniedEndRecovery() throws InvalidStateException {
		context.deniedEndRecovery();
	}

	public void recoveryAccepted() throws InvalidStateException {
		context.recoveryAccepted();
	}

	public void endRecoveryAccepted() throws InvalidStateException {
		context.endRecoveryAccepted();
	}

	public void setSumTransfers(long sumTransfers) {
		dataPipeline.setSumTransfers(sumTransfers);
	}

	public void setSumDeposits(long sumDeposits) {
		dataPipeline.setSumDeposits(sumDeposits);
	}

	public void setSumWithdrawals(long sumWithdrawals) {
		dataPipeline.setSumWithdrawals(sumWithdrawals);
	}

	public long addTransfer(long transferAmmount) {
		long newSum = dataPipeline.getSumTransfers() + transferAmmount;
		dataPipeline.setSumTransfers(newSum);
		return newSum;
	}

	public long addDeposit(long depositAmmount) {
		long newSum = dataPipeline.getSumDeposits() + depositAmmount;
		dataPipeline.setSumDeposits(newSum);
		return newSum;
	}

	public long addWithdrawal(long withdrawalAmmount) {
		long newSum = dataPipeline.getSumWithdrawals() + withdrawalAmmount;
		dataPipeline.setSumWithdrawals(newSum);
		return newSum;
	}

	public long getSumTransfers() {
		return dataPipeline.getSumTransfers();
	}

	public long getSumDeposits() {
		return dataPipeline.getSumDeposits();
	}

	public long getSumWithdrawals() {
		return dataPipeline.getSumWithdrawals();
	}

	public byte getNumberOfChannels() {
		return dataPipeline.getNumberOfChannels();
	}

	public byte getAvailableChannelNumber() {
		return dataPipeline.getAvailableChannelNumber();
	}

	public short getNewRequestNumber(byte channelNumber) {
		return (short) (dataPipeline.getCurrentRequest(channelNumber) + 1);
	}

	public short getNewResponseNumber(byte channelNumber) {
		return (short) (dataPipeline.getCurrentResponse(channelNumber) + 1);
	}

	public void appendMessageFromDAO(DataRequestMessage message)
			throws BusyChannelException {
		dataPipeline.appendMessage(message);
	}

	public void appendMessageFromDAO(DataResponseMessage message) {
		dataPipeline.appendMessage(message);
	}

	public void appendMessage(DataRequestMessage message)
			throws BusyChannelException {
		byte channelNumber = message.getChannelNumber();
		short requestNumber = message.getMessageNumber();

		if (dataPipeline.getNumberOfChannels() < channelNumber) {
			// TODO throw EX
		}

		if (!dataPipeline.isAvailable(channelNumber)) {
			throw new BusyChannelException();
		}

		if (dataPipeline.getCurrentRequest(channelNumber) + 1 != requestNumber) {
			// throw EX
		}

		dataPipeline.appendMessage(message);
	}

	public boolean appendMessage(DataResponseMessage message) {
		byte channelNumber = message.getChannelNumber();
		short responseNumber = message.getMessageNumber();

		if (dataPipeline.getNumberOfChannels() < channelNumber) {
			// TODO throw EX
		}

		if (dataPipeline.isAvailable(channelNumber)) {
			// throw EX
		}

		if (dataPipeline.getCurrentResponse(channelNumber) + 1 != responseNumber) {
			// throw EX
		}

		return dataPipeline.appendMessage(message);
	}

}
