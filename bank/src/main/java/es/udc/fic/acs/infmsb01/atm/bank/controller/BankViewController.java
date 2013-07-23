package es.udc.fic.acs.infmsb01.atm.bank.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.BankAccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.Card2AccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardAccountsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.gateway.BankControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.bank.view.BankView;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class BankViewController {

	private BankView view;
	private BankControllerModelGateway gateway;

	private Vector<String> sessionColNames, pipelineColNames;

	public BankViewController(String windowTitle) {
		this.gateway = null;
		this.view = new BankView(this, windowTitle);

		// ////////////////////////////////
		sessionColNames = new Vector<String>();
		sessionColNames.add("ConsortiumID");
		sessionColNames.add("State");
		sessionColNames.add("SumW");
		sessionColNames.add("SumD");
		sessionColNames.add("SumT");

		pipelineColNames = new Vector<String>();
		pipelineColNames.add("Channel #");
		pipelineColNames.add("Message #");
		pipelineColNames.add("From");
		pipelineColNames.add("To");
		pipelineColNames.add("Message");
		pipelineColNames.add("Payload");
		// ////////////////////////////////
	}

	public void setGateway(BankControllerModelGateway gateway) {
		this.gateway = gateway;
	}

	public final void requestOpenSession(String numberOfChannels) {
		gateway.requestOpenSession(Byte.parseByte(numberOfChannels));
	}

	public final void requestCloseSession() {
		gateway.requestCloseSession();
	}

	public final void requestStopTraffic() {
		gateway.requestStopTraffic();
	}

	public final void requestRestartTraffic() {
		gateway.requestRestartTraffic();
	}

	public final void updateViewSession(BankSession modifiedSession) {
		Vector<Vector<String>> sessionTableData = new Vector<Vector<String>>();
		Vector<Vector<String>> pipelineTableData = new Vector<Vector<String>>();

		Vector<String> sessionTableRow = new Vector<String>();
		sessionTableRow.add(modifiedSession.getConsortium().toString());
		sessionTableRow.add(modifiedSession.getContextState().getStateName()
				.toString());
		sessionTableRow
				.add(String.valueOf(modifiedSession.getSumWithdrawals()));
		sessionTableRow.add(String.valueOf(modifiedSession.getSumDeposits()));
		sessionTableRow.add(String.valueOf(modifiedSession.getSumTransfers()));

		sessionTableData.add(sessionTableRow);

		ArrayList<DataMessage> pipelineMessages = modifiedSession
				.getAllCurrentMessages();
		for (DataMessage dm : pipelineMessages) {
			Vector<String> pipelineTableRow = new Vector<String>();
			pipelineTableRow.add(String.valueOf(dm.getChannelNumber()));
			pipelineTableRow.add(String.valueOf(dm.getMessageNumber()));
			pipelineTableRow.add(dm.getFrom().getId());
			pipelineTableRow.add(dm.getTo().toString());
			pipelineTableRow.add(dm.getClass().getSimpleName());
			pipelineTableRow.add(dm.toString());

			pipelineTableData.add(pipelineTableRow);
		}

		view.updateSessionTable(sessionTableData, sessionColNames);
		view.updatePipelineTable(pipelineTableData, pipelineColNames);
	}

	public final void getAllAccounts() {
		List<Card2AccountTO> allCards2Accounts = null;
		
		try {
			allCards2Accounts = gateway.getAllAccounts();
		} catch(Exception e) {
			e.printStackTrace();
		}

		if (allCards2Accounts != null && allCards2Accounts.size() > 0) {
			Vector<String> card2AccountHeader = new Vector<String>();
			Vector<Vector<String>> allCards2AccountsData = new Vector<Vector<String>>();
			Vector<String> card2AccountData;

			card2AccountHeader.add("CreditCard #");
			card2AccountHeader.add("CreditCard Account #");
			card2AccountHeader.add("Account #");

			for (Card2AccountTO c2ba : allCards2Accounts) {
				card2AccountData = new Vector<String>();

				card2AccountData.add(c2ba.getCardNumber());
				card2AccountData.add(String.valueOf(c2ba.getAssociatedAccountNumber()));
				card2AccountData.add(c2ba.getRealAccountNumber());

				allCards2AccountsData.add(card2AccountData);
			}

			view.updateDBQueryTable(allCards2AccountsData, card2AccountHeader);
		}

	}

	public final void getAccountMovements(String creditcard,
			String associatedAccountNumber) {
		AccountMovementsCTO accountMovementsCTO = null;
		
		try {
			accountMovementsCTO = gateway.getAccountMovements(new CreditCardTO(
				creditcard), Byte.parseByte(associatedAccountNumber));
		} catch(Exception e) {
			e.printStackTrace();
		}

		Vector<String> accountMovementsHeader = new Vector<String>();
		Vector<Vector<String>> allMovementsData = new Vector<Vector<String>>();
		Vector<String> accountMovementData;

		accountMovementsHeader.add("Account #");
		accountMovementsHeader.add("Type");
		accountMovementsHeader.add("Ammount");
		accountMovementsHeader.add("Time");

		if (accountMovementsCTO != null) {

			SimpleDateFormat sdfDateTime = new SimpleDateFormat(
					"dd/MM/yyyyHH:mm:ss");

			List<AccountMovementTO> movements = accountMovementsCTO
					.getMovements();
			for (AccountMovementTO am : movements) {
				accountMovementData = new Vector<String>();
				accountMovementData.add(accountMovementsCTO.getAccount()
						.getAccountNumber());
				accountMovementData.add(MovementType.getMovementType(
						am.getType()).toString());
				accountMovementData.add(String.valueOf(am.getAmmount()));
				accountMovementData.add(sdfDateTime.format(am.getDate().getTime()));

				allMovementsData.add(accountMovementData);
			}

			view.updateDBQueryTable(allMovementsData, accountMovementsHeader);
		}

	}

	public final void getCreditcardAccounts(String creditcard) {
		CreditCardAccountsCTO creditCardAccountsCTO = null;
		
		try {
			creditCardAccountsCTO = gateway.getCreditCardAccounts(new CreditCardTO(
				creditcard));
		} catch(Exception e) {
			e.printStackTrace();
		}

		Vector<String> creditcardAccountsHeader = new Vector<String>();
		Vector<Vector<String>> allAccountsData = new Vector<Vector<String>>();
		Vector<String> accountData;

		creditcardAccountsHeader.add("CreditCard #");
		creditcardAccountsHeader.add("Account #");
		creditcardAccountsHeader.add("Balance");

		if (creditCardAccountsCTO != null) {
			List<BankAccountTO> accounts = creditCardAccountsCTO.getAccounts();
			for (BankAccountTO ba : accounts) {
				accountData = new Vector<String>();
				accountData.add(creditCardAccountsCTO.getCreditCard()
						.getCardNumber());
				accountData.add(ba.getAccountNumber());
				accountData.add(String.valueOf(ba.getBalance()));

				allAccountsData.add(accountData);
			}

			view.updateDBQueryTable(allAccountsData, creditcardAccountsHeader);
		}

	}

	public void setTransportProcessorLinkState(
			TransportProcessorLinkState linkState) {
		
		gateway.setTransportProcessorLinkState(linkState);
		
	}
	
	
}
