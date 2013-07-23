package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseEndRecovery;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ResponseEndRecoveryConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public ResponseEndRecoveryConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException, NoSessionException {

		ResponseEndRecovery message = (ResponseEndRecovery) getMessage();
		
		ConsortiumSession cs = (ConsortiumSession) getSession();
		
		if(cs == null) {
			throw new NoSessionException();
		}
		
		cs.getSmartTimer().cancelTask();
		
		if(!message.getErrorCode().equals(ErrorCode.CORRECT)
			|| message.getResponseCode().equals(ControlResponseCode.DENIED)) {
			
			cs.deniedEndRecovery();
			
			return message;
			
		}
		
		ConsortiumDAO dao = new ConsortiumDAO();
		
		cs.endRecoveryAccepted();
		dao.updateSessionState(cs.getBank().getId(), cs.getContextState(), connection);
		
		return message;
		
	}

}
