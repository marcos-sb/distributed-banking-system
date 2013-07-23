package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.BankAccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.DataRequestBankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;

public final class RequestWithdrawBankAction extends DataRequestBankPersistenceAction {

	public RequestWithdrawBankAction() {
		super(true);
	}

	@Override
	protected Object doExecute(Connection connection) throws SQLException, BadCreditCardException, IOException, InstantiationException, IllegalAccessException {

		RequestWithdraw message = (RequestWithdraw) getMessage();
		ResponseWithdraw response = new ResponseWithdraw();
		DataResponseCode drc = DataResponseCode.DENIED;
		BankDAO dao = new BankDAO();
		BankSession session = (BankSession) getSession();
		Calendar now;
		AccountMovementsCTO mov;
		BankAccountTO account;
		double newBalance;

		try {

			session.appendMessage(message);
			session.addWithdrawal(message.getAmmount());
			dao.insertDataRequestMessage(session.getConsortium().getId(),
					message, connection);

			account = dao.queryCreditCardAccount(
					new CreditCardTO(message.getCardNumber()),
					message.getAssociatedAccountNumber(), connection);
			
			newBalance = account.getBalance() - message.getAmmount();
			account.setBalance(newBalance);

			dao.updateAccount(account, connection);

			mov = new AccountMovementsCTO(account);
			now = Calendar.getInstance();

			mov.addMovement(new AccountMovementTO(
					MovementType.WITHDRAWAL.getCode(), message.getAmmount(), now));

			dao.insertAccountMovements(mov, connection);

			response.setAccountBalance(newBalance);
			
			drc = DataResponseCode.ACCEPTED;

		} catch (BadCreditCardException e) {
			drc = DataResponseCode.DENIEDBADCARD;
		} catch (BadAccountException e) {
			drc = DataResponseCode.DENIEDBADACCOUNT;
		} catch (Exception e) {
			drc = DataResponseCode.DENIED;
		} finally {

			response = (ResponseWithdraw) fillResponseMessage(response,
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
