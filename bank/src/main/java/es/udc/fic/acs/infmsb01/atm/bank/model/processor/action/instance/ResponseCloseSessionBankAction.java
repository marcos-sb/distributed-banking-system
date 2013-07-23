package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseCloseSession;

public class ResponseCloseSessionBankAction extends BankPersistenceAction {

	public ResponseCloseSessionBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws InvalidStateException, SQLException {
		ResponseCloseSession message = (ResponseCloseSession) getMessage();
		
		BankSession bs = (BankSession) getSession();
		
		bs.getSmartTimer().cancelTask();
		
		if (!message.getErrorCode().equals(ErrorCode.CORRECT)
				|| message.getResponseCode().equals(ControlResponseCode.DENIED)) {

			bs.deniedCloseSession();
			
			return message;
			
		}
		
		BankDAO dao = new BankDAO();
		
		bs.closeSessionAccepted();
		
		dao.closeSession(bs, connection);
		
		bs.resetSession();
		
		return message;
	}

}
