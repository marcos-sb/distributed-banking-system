package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseOpenSession;

public final class ResponseOpenSessionBankAction extends BankPersistenceAction {

	public ResponseOpenSessionBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws InvalidStateException,
			SQLException {
		
		BankSession session = (BankSession) getSession();
		
		session.getSmartTimer().cancelTask();
		
		ResponseOpenSession message = (ResponseOpenSession) getMessage();
		if (!message.getErrorCode().equals(ErrorCode.CORRECT)
				|| message.getResponseCode().equals(ControlResponseCode.DENIED)) {

			session.deniedOpenSession();
			
			return message;

		}

		BankDAO dao = new BankDAO();
				
		session.openSessionAccepted();

		dao.createSession(session, (ConsortiumInfo) getTransport()
				.getDestinationInfo(), (BankInfo) getTransport()
				.getOriginInfo(), connection);

		return message;
	}

}
