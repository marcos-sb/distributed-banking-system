package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.BankAccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.DataRequestBankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadDestinationAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadSourceAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoFundsInSourceAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;

public final class RequestAccountsTransferBankAction extends DataRequestBankPersistenceAction {

	public RequestAccountsTransferBankAction() {
		super(true);
	}

	@Override
	protected Object doExecute(Connection connection) throws SQLException, BadCreditCardException, IOException, InstantiationException, IllegalAccessException {

		RequestAccountsTransfer message = (RequestAccountsTransfer) getMessage();
		ResponseAccountsTransfer response = new ResponseAccountsTransfer();
		DataResponseCode drc = DataResponseCode.DENIED;
		BankDAO dao = new BankDAO();
		BankSession session = (BankSession) getSession();
		ArrayList<BankAccountTO> affectedAccounts;
		AccountMovementsCTO movSource;
		AccountMovementsCTO movDestination;
		Calendar now;

		try {

			session.appendMessage(message);
			session.addTransfer(message.getAmmount());
			dao.insertDataRequestMessage(session.getConsortium().getId(),
					message, connection);

			affectedAccounts = (ArrayList<BankAccountTO>) dao.accountsTransfer(
					new CreditCardTO(message.getCardNumber()),
					message.getSourceAccountNumber(),
					message.getDestinationAccountNumber(),
					message.getAmmount(), connection);

			movSource = new AccountMovementsCTO(affectedAccounts.get(0)); // sourceAccount
																			// @
																			// 0
			movDestination = new AccountMovementsCTO(affectedAccounts.get(1)); // destinationAccount
																				// @
																				// 1

			now = Calendar.getInstance();

			movSource.addMovement(new AccountMovementTO(
					MovementType.TRANSFERSENT.getCode(), message.getAmmount()
							* -1, now));

			movDestination.addMovement(new AccountMovementTO(
					MovementType.TRANSFERRECEIVED.getCode(), message
							.getAmmount(), now));

			dao.insertAccountMovements(movSource, connection);
			dao.insertAccountMovements(movDestination, connection);

			response.setSourceAccountBalance(movSource.getAccount()
					.getBalance());
			response.setDestinationAccountBalance(movDestination.getAccount()
					.getBalance());

			drc = DataResponseCode.ACCEPTED;

		} catch (BadCreditCardException e) {
			drc = DataResponseCode.DENIEDBADCARD;
		} catch (BadSourceAccountException e) {
			drc = DataResponseCode.DENIEDBADSOURCE;
		} catch (BadDestinationAccountException e) {
			drc = DataResponseCode.DENIEDBADDESTINATION;
		} catch (NoFundsInSourceAccountException e) {
			drc = DataResponseCode.DENIEDNOFUNDSSOURCE;
		} catch (Exception e) {
			drc = DataResponseCode.DENIED;
		} finally {

			response = (ResponseAccountsTransfer) fillResponseMessage(response,
					drc);

			session.appendMessage(response);
			dao.insertDataResponseMessage(session.getConsortium().getId(),
					response, connection);
			dao.updateSessionTotals(session.getConsortium().getId(),
					session.getSumDeposits(), session.getSumTransfers(),
					session.getSumWithdrawals(), connection);

			getTransport().send(response);

		}

		return response;

	}

	private DataResponseMessage fillResponseMessage(DataResponseMessage drm,
			DataResponseCode rc) {

		drm.setBankOnline(true);
		drm.setChannelNumber(((DataRequestMessage) getMessage())
				.getChannelNumber());
		drm.setFrom(((BankSession) getSession()).getBank());
		drm.setMessageNumber(((DataRequestMessage) getMessage())
				.getMessageNumber());
		drm.setResponseCode(rc);
		drm.setTo(((BankSession) getSession()).getConsortium());

		return drm;
	}

}
