package es.udc.fic.acs.infmsb01.atm.consortium.model.message.processor;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidRecipientException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.ControlMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.ActionProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.util.ConfigurationParametersManager;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;
import es.udc.fic.acs.infmsb01.atm.consortium.model.gateway.ConsortiumControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.CreatePartialSessionConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.DataRequestConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.DataResponseConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.QueryBanksInfoConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.QuerySessionObjectsConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestAccountMovementsConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestAccountsTransferConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestCheckBalanceConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestCloseSessionConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestDepositConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestEndRecoveryConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestOpenSessionConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestRecoveryConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestRestartTrafficConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestStopTrafficConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.RequestWithdrawConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.ResponseAccountsTransferConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.ResponseDepositConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.ResponseEndRecoveryConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.ResponseRecoveryConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance.ResponseWithdrawConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.PendingDataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.consortium.net.transport.ConsortiumTransportProcessor;

public final class ConsortiumMessageProcessor {

	private static final String JDBC_USER_NAME_CONFIGURATION_PARAMETER = "ConsortiumMessageProcessor/JDBCUserName";
	private static final String JDBC_USER_PASS_CONFIGURATION_PARAMETER = "ConsortiumMessageProcessor/JDBCUserPass";
	private static final String JDBC_DB_URL_CONFIGURATION_PARAMETER = "ConsortiumMessageProcessor/JDBCDBUrl";

	private String userName;
	private String userPass;
	private String url;

	private static HashMap<Class<? extends Message>, Class<? extends ConsortiumPersistenceAction>> message2PersistenceAction;

	private static HashMap<String, ConsortiumSession> bankID2ConsortiumSession;

	private RecipientInfo consortiumID;
	private ConsortiumTransportProcessor transportProcessor;
	private ConsortiumControllerModelGateway gateway;

	public ConsortiumMessageProcessor(ConsortiumInfo ci) throws Exception {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());

		userName = getParameter(JDBC_USER_NAME_CONFIGURATION_PARAMETER);
		userPass = getParameter(JDBC_USER_PASS_CONFIGURATION_PARAMETER);
		url = getParameter(JDBC_DB_URL_CONFIGURATION_PARAMETER);

		bankID2ConsortiumSession = rebuildSessionObjects();

		consortiumID = new RecipientInfo(ci.getId());
		transportProcessor = new ConsortiumTransportProcessor(ci,
				rebuildBankInfoObjects());

		gateway = null;

	}

	@SuppressWarnings("unchecked")
	private final HashMap<String, ConsortiumSession> rebuildSessionObjects()
			throws SQLException, Exception {

		ConsortiumPersistenceAction action = new QuerySessionObjectsConsortiumPersistenceAction();

		ArrayList<ConsortiumSession> tempList = (ArrayList<ConsortiumSession>) ActionProcessor
				.process(DriverManager.getConnection(url, userName, userPass),
						action);
		HashMap<String, ConsortiumSession> tempMap = new HashMap<String, ConsortiumSession>();

		for (ConsortiumSession cs : tempList) {
			tempMap.put(cs.getBank().getId(), cs);
		}

		return tempMap;

	}

	@SuppressWarnings("unchecked")
	private final ArrayList<BankInfo> rebuildBankInfoObjects()
			throws SQLException, Exception {
		ConsortiumPersistenceAction action = new QueryBanksInfoConsortiumPersistenceAction();

		return (ArrayList<BankInfo>) ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

	}

	public final ConsortiumTransportProcessor getTransport() {
		return this.transportProcessor;
	}

	public final void setGateway(ConsortiumControllerModelGateway gateway) {
		this.gateway = gateway;

		if (bankID2ConsortiumSession != null
				&& bankID2ConsortiumSession.size() > 0) {
			gateway.updateView(getAllSessions());
		}
	}

	private final void registerSession(ConsortiumSession cs) {
		bankID2ConsortiumSession.put(cs.getBank().getId(), cs);
	}

	private final RecipientInfo getBankRecipientInfo(String creditCardNumber) {
		return new RecipientInfo(creditCardNumber.substring(0, 8));
	}

	public final void processUI(ControlMessage message) throws SQLException,
			Exception {

		ConsortiumPersistenceAction action;

		ConsortiumSession cs = bankID2ConsortiumSession.get(message.getTo()
				.getId());

		if (cs != null) {
			action = getAction(message);
			action.setMessage(message);
			action.setSession(cs);
			action.setTransportProcessor(transportProcessor);
			action.setGateway(gateway);

			ActionProcessor.process(
					DriverManager.getConnection(url, userName, userPass),
					action);
		}
		gateway.updateView(cs);
	}

	public final void processBank(Message message) throws SQLException,
			Exception {

		ConsortiumPersistenceAction action;
		Object processResult;

		// is the message for me?
		if (message.getTo() != null) {
			if (!message.getTo().equals(consortiumID)) {
				throw new InvalidRecipientException(consortiumID.toString());
			}
		}

		RecipientInfo bankID = message.getFrom();
		ConsortiumSession cs = bankID2ConsortiumSession.get(bankID.getId());

		action = getAction(message);
		action.setMessage(message);
		action.setSession(cs);
		action.setTransportProcessor(transportProcessor);
		action.setGateway(gateway);
		processResult = ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

		if (cs == null) { // no session

			if (processResult == null) {
				throw new NoSessionException();

			} else { // new full session
				ConsortiumSession newCS = (ConsortiumSession) processResult;
				registerSession(newCS);
				processPendingMessages(newCS);

				cs = newCS;
			}

		} else {

			if (processResult == null && message instanceof RequestCloseSession) {
				unRegisterSession(bankID);

			} else if (message instanceof RequestOpenSession
					|| message instanceof RequestRestartTraffic) { // new full
																	// session

				processPendingMessages(cs);

			}

		}

		gateway.updateView(cs);

	}

	private final void processPendingMessages(ConsortiumSession cs)
			throws SQLException, Exception {

		ConsortiumPersistenceAction actionRequest;
		ArrayList<PendingDataRequestMessage> pendingMessages = cs
				.getPendingMessages();

		synchronized (pendingMessages) {

			for (PendingDataRequestMessage pendingMessage : pendingMessages) {

				actionRequest = (DataRequestConsortiumPersistenceAction) getAction(pendingMessage
						.getDataRequestMessage());
				actionRequest
						.setMessage(pendingMessage.getDataRequestMessage());
				actionRequest.setSession(cs);
				actionRequest.setTransportProcessor(transportProcessor);
				actionRequest.setGateway(gateway);
				ActionProcessor.process(
						DriverManager.getConnection(url, userName, userPass),
						actionRequest);
			}

			pendingMessages.notify();
		}

	}

	private void unRegisterSession(RecipientInfo bankID) {
		bankID2ConsortiumSession.remove(bankID.getId());
	}

	public final ArrayList<ConsortiumSession> getAllSessions() {
		return new ArrayList<ConsortiumSession>(
				bankID2ConsortiumSession.values());
	}

	public final void processATM(DataRequestMessage message)
			throws SQLException, Exception {
		DataRequestConsortiumPersistenceAction actionRequest;

		// is the message for me?
		if (message.getTo() != null) {
			if (!message.getTo().equals(consortiumID)) {
				throw new InvalidRecipientException(consortiumID.toString());
			}
		}

		RecipientInfo bankID = getBankRecipientInfo(message.getCardNumber());
		ConsortiumSession cs = bankID2ConsortiumSession.get(bankID.getId());

		message.setBankOnline(cs != null && cs.isBankOnline());

		if (cs == null) {
			cs = new ConsortiumSession(consortiumID, bankID);
			ConsortiumPersistenceAction action = new CreatePartialSessionConsortiumPersistenceAction();
			action.setTransportProcessor(transportProcessor);
			action.setSession(cs);
			ActionProcessor.process(
					DriverManager.getConnection(url, userName, userPass),
					action);
			bankID2ConsortiumSession.put(bankID.getId(), cs);
		}

		actionRequest = (DataRequestConsortiumPersistenceAction) getAction(message);
		actionRequest.setMessage(message);
		actionRequest.setSession(cs);
		actionRequest.setTransportProcessor(transportProcessor);
		actionRequest.setGateway(gateway);
		ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass),
				actionRequest);

		gateway.updateView(cs);

	}

	public final ConsortiumSession getConsortiumSession(String BankId) {
		return bankID2ConsortiumSession.get(BankId);
	}

	private static final String getParameter(String param) throws IOException {
		return ConfigurationParametersManager.getParameter(param);
	}

	private static ConsortiumPersistenceAction getAction(Message message)
			throws InstantiationException, IllegalAccessException {
		if (message2PersistenceAction == null) {
			fillMessage2PersistenceAction();
		}

		return message2PersistenceAction.get(message.getClass()).newInstance();
	}

	private static void fillMessage2PersistenceAction() {

		message2PersistenceAction = new HashMap<Class<? extends Message>, Class<? extends ConsortiumPersistenceAction>>();

		message2PersistenceAction.put(RequestWithdraw.class,
				RequestWithdrawConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestAccountsTransfer.class,
				RequestAccountsTransferConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestAccountMovements.class,
				RequestAccountMovementsConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestCheckBalance.class,
				RequestCheckBalanceConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestDeposit.class,
				RequestDepositConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseWithdraw.class,
				ResponseWithdrawConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseAccountsTransfer.class,
				ResponseAccountsTransferConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseAccountMovements.class,
				DataResponseConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseCheckBalance.class,
				DataResponseConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseDeposit.class,
				ResponseDepositConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestOpenSession.class,
				RequestOpenSessionConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestCloseSession.class,
				RequestCloseSessionConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestStopTraffic.class,
				RequestStopTrafficConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestRestartTraffic.class,
				RequestRestartTrafficConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestRecovery.class,
				RequestRecoveryConsortiumPersistenceAction.class);
		message2PersistenceAction.put(RequestEndRecovery.class,
				RequestEndRecoveryConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseRecovery.class,
				ResponseRecoveryConsortiumPersistenceAction.class);
		message2PersistenceAction.put(ResponseEndRecovery.class,
				ResponseEndRecoveryConsortiumPersistenceAction.class);
	}

	public void setTransportProcessorBankLinkState(
			TransportProcessorLinkState linkState) {
		transportProcessor.setBankLinkState(linkState);
	}

	public void setTransportProcessorATMLinkState(
			TransportProcessorLinkState linkState) {
		transportProcessor.setATMLinkState(linkState);
	}

}
