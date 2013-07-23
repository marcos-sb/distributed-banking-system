package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseStopTraffic;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestStopTrafficConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public RequestStopTrafficConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws IOException,
			InstantiationException, IllegalAccessException, SQLException {

		ConsortiumDAO dao = new ConsortiumDAO();
		ConsortiumSession cs = (ConsortiumSession) getSession();

		ResponseStopTraffic response = new ResponseStopTraffic();

		ErrorCode ec = ErrorCode.OTHER;
		ControlResponseCode crc = ControlResponseCode.DENIED;

		try {
			
			cs.requestStopTraffic();
			crc = ControlResponseCode.ACCEPTED;
			ec = ErrorCode.CORRECT;
			cs.stopTrafficAccepted();
			
			dao.updateSessionState(cs.getBank().getId(), cs.getContextState(),
					connection);
			
		} catch (InvalidStateException e) {

			crc = ControlResponseCode.DENIED;
			
			switch(e.getCurrentContext().getStateTO().getStateName()) {
			case OFFLINE:
				ec = ErrorCode.NOSESSION;
				break;
				
			default:
				ec = ErrorCode.OTHER;
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
