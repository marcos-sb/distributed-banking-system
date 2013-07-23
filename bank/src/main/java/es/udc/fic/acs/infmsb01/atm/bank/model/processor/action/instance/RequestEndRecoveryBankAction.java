package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseEndRecovery;

public final class RequestEndRecoveryBankAction extends BankPersistenceAction {

	public RequestEndRecoveryBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws SQLException,
			IOException, InstantiationException, IllegalAccessException {

		BankDAO dao = new BankDAO();
		BankSession session = (BankSession) getSession();
		ResponseEndRecovery response = new ResponseEndRecovery();

		ErrorCode ec = ErrorCode.OTHER;
		ControlResponseCode crc = ControlResponseCode.DENIED;

		try {

			session.requestEndRecovery();
			crc = ControlResponseCode.ACCEPTED;
			ec = ErrorCode.CORRECT;
			session.endRecoveryAccepted();
			
			dao.updateSessionState(session.getConsortium().getId(),
					session.getContextState(),
					connection);

		} catch (InvalidStateException e) {

			crc = ControlResponseCode.DENIED;

			switch(e.getCurrentContext().getStateTO().getStateName()) {
			case OFFLINE:
				ec = ErrorCode.NOSESSION;
				break;
				
			default:
				ec = ErrorCode.RECOVERYNOTSET;
			}

		} finally {

			response.setFrom(session.getBank());
			response.setTo(session.getConsortium());
			response.setErrorCode(ec);
			response.setResponseCode(crc);

			getTransport().send(response);
		}

		return response;

	}

}
