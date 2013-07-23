package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestRestartTrafficConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public RequestRestartTrafficConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException, NoSessionException {

		ConsortiumDAO dao = new ConsortiumDAO();
		ConsortiumSession cs = (ConsortiumSession) getSession();

		if (cs == null) {
			throw new NoSessionException();
		}

		ResponseRestartTraffic response = new ResponseRestartTraffic();

		ErrorCode ec = ErrorCode.OTHER;
		ControlResponseCode crc = ControlResponseCode.DENIED;

		try {

			cs.requestRestartTraffic();
			crc = ControlResponseCode.ACCEPTED;
			ec = ErrorCode.CORRECT;
			cs.restartTrafficAccepted();
			
			dao.updateSessionState(cs.getBank().getId(), cs.getContextState(),
					connection);

		} catch (InvalidStateException e) {

			crc = ControlResponseCode.DENIED;
			
			switch(e.getCurrentContext().getStateTO().getStateName()) {
			case OFFLINE:
				ec = ErrorCode.NOSESSION;
				break;
				
			default:
				ec = ErrorCode.UNSTOPPEDTRAFFIC;
			}

		} finally {
			response.setErrorCode(ec);
			response.setResponseCode(crc);
			response.setFrom(cs.getConsortium());
			response.setTo(cs.getBank());

			getTransportProcessor().send2Bank(response);
		}

		return response;

	}

}
