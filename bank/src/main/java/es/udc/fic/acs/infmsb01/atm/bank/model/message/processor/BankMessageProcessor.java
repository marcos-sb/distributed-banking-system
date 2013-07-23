package es.udc.fic.acs.infmsb01.atm.bank.model.message.processor;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.Card2AccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardAccountsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.gateway.BankControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.GetAccountMovementsBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.GetAllAccountsBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.GetCreditCardAccountsBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.QuerySessionObjectBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestAccountMovementsBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestAccountsTransferBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestCheckBalanceBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestCloseSessionBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestDepositBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestEndRecoveryBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestOpenSessionBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestRecoveryBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestRestartTrafficBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestStopTrafficBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.RequestWithdrawBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.ResponseCloseSessionBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.ResponseOpenSessionBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.ResponseRestartTrafficBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance.ResponseStopTrafficBankAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidRecipientException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestEndRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRecovery;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.ActionProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.PersistenceAction;
import es.udc.fic.acs.infmsb01.atm.common.model.util.ConfigurationParametersManager;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessor;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class BankMessageProcessor {

	private static final String JDBC_USER_NAME_CONFIGURATION_PARAMETER = "BankMessageProcessor/JDBCUserName";
	private static final String JDBC_USER_PASS_CONFIGURATION_PARAMETER = "BankMessageProcessor/JDBCUserPass";
	private static final String JDBC_DB_URL_CONFIGURATION_PARAMETER = "BankMessageProcessor/JDBCDBUrl";

	private String userName;
	private String userPass;
	private String url;

	private static HashMap<Class<? extends Message>, Class<? extends BankPersistenceAction>> message2PersistenceAction;

	private BankSession bankSession;

	private RecipientInfo consortiumID;
	private RecipientInfo bankID;
	private TransportProcessor transportProcessor;
	private BankControllerModelGateway gateway;

	public BankMessageProcessor(RecipientInfo bankID,
			RecipientInfo consortiumID, TransportProcessor tp)
			throws InstantiationException, IllegalAccessException,
			ParseException, Exception {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());

		userName = getParameter(JDBC_USER_NAME_CONFIGURATION_PARAMETER);
		userPass = getParameter(JDBC_USER_PASS_CONFIGURATION_PARAMETER);
		url = getParameter(JDBC_DB_URL_CONFIGURATION_PARAMETER);

		transportProcessor = tp;

		this.bankID = bankID;
		this.consortiumID = consortiumID;
		// this.bankSession = new BankSession(consortiumID, bankID,
		// (byte) 0);

		this.bankSession = rebuildSessionObject();
		gateway = null;

	}

	private BankSession rebuildSessionObject() throws SQLException, Exception {

		PersistenceAction action = new QuerySessionObjectBankAction();

		BankSession bs = (BankSession) ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

		if(bs == null) {
			bs = new BankSession(consortiumID, bankID, (byte)0);
		}
		
		return bs;

	}

	public final void setGateway(BankControllerModelGateway gateway) {
		this.gateway = gateway;

		if (bankSession != null) {
			gateway.updateView(bankSession);
		}
	}

	public final TransportProcessor getTransport() {
		return this.transportProcessor;
	}

	@SuppressWarnings("unchecked")
	public final List<Card2AccountTO> getAllAccounts() throws Exception {

		GetAllAccountsBankAction action = new GetAllAccountsBankAction();

		return (List<Card2AccountTO>) ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

	}

	public final AccountMovementsCTO getAccountMovements(
			CreditCardTO creditCard, byte associatedAccountNumber)
			throws Exception {

		GetAccountMovementsBankAction action = new GetAccountMovementsBankAction(
				creditCard, associatedAccountNumber);

		return (AccountMovementsCTO) ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

	}

	public final CreditCardAccountsCTO getCreditCardAccounts(
			CreditCardTO creditCard) throws Exception {

		GetCreditCardAccountsBankAction action = new GetCreditCardAccountsBankAction(
				creditCard);

		return (CreditCardAccountsCTO) ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);

	}

	public final void process(Message message) throws Exception {

		BankPersistenceAction action;

		// is the message for me?
		if (message.getTo() != null) {
			if (!message.getTo().equals(bankID)) {
				throw new InvalidRecipientException(bankID.toString());
			}
		}

		// do i know the sender?
		if (message.getFrom() != null) {
			if (!message.getFrom().equals(consortiumID)) {
				throw new InvalidRecipientException(consortiumID.toString());
			}
		}

		action = getAction(message);
		action.setMessage(message);
		action.setTransport(transportProcessor);
		action.setSession(bankSession);
		action.setGateway(gateway);

		ActionProcessor.process(
				DriverManager.getConnection(url, userName, userPass), action);
		
		gateway.updateView(bankSession);
	}

	private static final BankPersistenceAction getAction(Message message)
			throws InstantiationException, IllegalAccessException {
		if (message2PersistenceAction == null) {
			fillMessage2PersistenceAction();
		}

		return message2PersistenceAction.get(message.getClass()).newInstance();
	}

	private static final void fillMessage2PersistenceAction() {

		message2PersistenceAction = new HashMap<Class<? extends Message>, Class<? extends BankPersistenceAction>>();

		message2PersistenceAction.put(RequestAccountMovements.class,
				RequestAccountMovementsBankAction.class);
		message2PersistenceAction.put(RequestAccountsTransfer.class,
				RequestAccountsTransferBankAction.class);
		message2PersistenceAction.put(RequestCheckBalance.class,
				RequestCheckBalanceBankAction.class);
		message2PersistenceAction.put(RequestCloseSession.class,
				RequestCloseSessionBankAction.class);
		message2PersistenceAction.put(RequestDeposit.class,
				RequestDepositBankAction.class);
		message2PersistenceAction.put(RequestEndRecovery.class,
				RequestEndRecoveryBankAction.class);
		message2PersistenceAction.put(RequestOpenSession.class,
				RequestOpenSessionBankAction.class);
		message2PersistenceAction.put(RequestRecovery.class,
				RequestRecoveryBankAction.class);
		message2PersistenceAction.put(RequestRestartTraffic.class,
				RequestRestartTrafficBankAction.class);
		message2PersistenceAction.put(RequestStopTraffic.class,
				RequestStopTrafficBankAction.class);
		message2PersistenceAction.put(RequestWithdraw.class,
				RequestWithdrawBankAction.class);
		message2PersistenceAction.put(ResponseCloseSession.class,
				ResponseCloseSessionBankAction.class);
		message2PersistenceAction.put(ResponseOpenSession.class,
				ResponseOpenSessionBankAction.class);
		message2PersistenceAction.put(ResponseRestartTraffic.class,
				ResponseRestartTrafficBankAction.class);
		message2PersistenceAction.put(ResponseStopTraffic.class,
				ResponseStopTrafficBankAction.class);

	}

	private static final String getParameter(String param) throws IOException {

		return ConfigurationParametersManager.getParameter(param);

	}
	
	public void setTransportProcessorLinkState(
			TransportProcessorLinkState linkState) {
		
		transportProcessor.setLinkState(linkState);
		
	}

}
