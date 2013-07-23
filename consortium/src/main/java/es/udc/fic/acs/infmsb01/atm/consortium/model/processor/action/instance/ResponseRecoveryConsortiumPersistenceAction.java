package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.SessionOpenedException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRecovery;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class ResponseRecoveryConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public ResponseRecoveryConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException, BadCreditCardException,
			BadAccountException, SessionOpenedException, NoSessionException {
		
		ResponseRecovery message = (ResponseRecovery) getMessage();
		
		ConsortiumSession cs = (ConsortiumSession) getSession();
		
		if(cs == null) {
			throw new NoSessionException();
		}

		cs.getSmartTimer().cancelTask();
		
		if(!message.getErrorCode().equals(ErrorCode.CORRECT)
			|| message.getResponseCode().equals(ControlResponseCode.DENIED)) {
		
			cs.deniedRecovery();
			
			return message;
			
		}
		
		ConsortiumDAO dao = new ConsortiumDAO();
		
		cs.recoveryAccepted();
		dao.updateSessionState(cs.getBank().getId(), cs.getContextState(), connection);
		
		//////////////////////////// recovery /////////////////
		ArrayList<DataRequestMessage> requests = cs.getAllCurrentRequests(); 
		for(DataRequestMessage drm : requests) {
			getTransportProcessor().send2Bank(drm);
		}
		/////////////////////////////
		
		return message;
		
	}

}
