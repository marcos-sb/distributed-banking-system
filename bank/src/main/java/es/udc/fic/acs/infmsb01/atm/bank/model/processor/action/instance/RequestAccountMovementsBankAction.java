package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.DataRequestBankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.MovementType;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.AccountMovement;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;

public final class RequestAccountMovementsBankAction extends DataRequestBankPersistenceAction {

	public RequestAccountMovementsBankAction() {
		super(true);
	}

	@Override
	protected Object doExecute(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException {

		ArrayList<AccountMovementTO> movements;
		ResponseAccountMovements response = new ResponseAccountMovements();
		DataResponseCode drc = DataResponseCode.DENIED;
		BankDAO dao = new BankDAO();
		BankSession session = (BankSession) getSession();

		try {

			RequestAccountMovements message = (RequestAccountMovements) getMessage();
			
			session.appendMessage(message);
			dao.insertDataRequestMessage(session.getConsortium().getId(),
					message, connection);

			movements = dao.queryAccountMovements(
					new CreditCardTO(message.getCardNumber()),
					message.getAssociatedAccountNumber(), connection)
					.getMovements();
			
			for (AccountMovementTO movement : movements) {
				AccountMovement am = new AccountMovement(movement.getAmmount(),
						movement.getDate(),
						MovementType.getMovementType(movement.getType()));
			
				response.addAccountMovement(am);
			}

			drc = DataResponseCode.ACCEPTED;

		} catch (BadCreditCardException e) {
			drc = DataResponseCode.DENIEDBADCARD;
		} catch (BadAccountException e) {
			drc = DataResponseCode.DENIEDBADACCOUNT;
		} catch (Exception e) {
			drc = DataResponseCode.DENIED;
		} finally {

			response = (ResponseAccountMovements) fillResponseMessage(response,
					drc);

			session.appendMessage(response);
			dao.insertDataResponseMessage(session.getConsortium().getId(), response, connection);
			
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
