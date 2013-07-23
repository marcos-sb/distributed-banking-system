package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.SessionOpenedException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseOpenSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestOpenSessionConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public RequestOpenSessionConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws IOException,
			InstantiationException, IllegalAccessException, SQLException {

		ResponseOpenSession response = new ResponseOpenSession();
		ConsortiumSession cs = (ConsortiumSession) getSession();
		RequestOpenSession message = (RequestOpenSession) getMessage();

		response.setErrorCode(ErrorCode.OTHER);
		response.setResponseCode(ControlResponseCode.DENIED);

		try {

			if (cs == null) { // no partial session (from pending atm requests)
								// existed

				cs = new ConsortiumSession(message.getTo(), message.getFrom());

			} else if (cs.isBankOnline()) { // full session existed
				throw new SessionOpenedException();
			}

			cs.requestOpenSession();
			ConsortiumDAO dao = new ConsortiumDAO();

			ConsortiumInfo ci = getTransportProcessor().getConsortiumInfo();
			BankInfo bi = getTransportProcessor().getRegisteredBank(
					message.getFrom());

			cs.openSessionAccepted();
			cs.setNumberOfChannels(message.getNumberOfChannels());
			dao.createSession(cs, ci, bi, connection);

			response.setErrorCode(ErrorCode.CORRECT);
			response.setResponseCode(ControlResponseCode.ACCEPTED);

		} catch (SessionOpenedException e) {

			response.setErrorCode(ErrorCode.SESSIONOPENED);
			response.setResponseCode(ControlResponseCode.DENIED);

		} catch (InvalidStateException e) {

			response.setErrorCode(ErrorCode.OTHER);
			response.setResponseCode(ControlResponseCode.DENIED);

		} finally {
			response.setFrom(cs.getConsortium());
			response.setTo(cs.getBank());

			getTransportProcessor().send2Bank(response);
		}

		return cs;

	}

}
