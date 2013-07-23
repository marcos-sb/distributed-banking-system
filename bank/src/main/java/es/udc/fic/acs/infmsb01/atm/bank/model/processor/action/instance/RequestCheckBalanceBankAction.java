package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.BankAccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.DataRequestBankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;

public final class RequestCheckBalanceBankAction extends DataRequestBankPersistenceAction {

	public RequestCheckBalanceBankAction() {
		super(true);
	}

	@Override
	protected Object doExecute(Connection connection) throws SQLException, BadCreditCardException, IOException, InstantiationException, IllegalAccessException {

		ResponseCheckBalance response = new ResponseCheckBalance();
		DataResponseCode drc = DataResponseCode.DENIED;
		BankDAO dao = new BankDAO();
		BankSession session = (BankSession) getSession();
		BankAccountTO ba;

		try {

			RequestCheckBalance message = (RequestCheckBalance) getMessage();

			session.appendMessage(message);
			dao.insertDataRequestMessage(session.getConsortium().getId(),
					message, connection);

			ba = dao.queryCreditCardAccount(
					new CreditCardTO(message.getCardNumber()),
					message.getAssociatedAccountNumber(), connection);

			response.setAccountBalance(ba.getBalance());
			
			drc = DataResponseCode.ACCEPTED;

		} catch (BadCreditCardException e) {
			drc = DataResponseCode.DENIEDBADCARD;
		} catch (BadAccountException e) {
			drc = DataResponseCode.DENIEDBADACCOUNT;
		} catch (Exception e) {
			drc = DataResponseCode.DENIED;
		} finally {

			response = (ResponseCheckBalance) fillResponseMessage(response, drc);

			session.appendMessage(response);
			dao.insertDataResponseMessage(session.getConsortium().getId(),
					response, connection);

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
