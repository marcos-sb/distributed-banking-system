package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.IncoherentSessionTotalsException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ControlResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.ErrorCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.ResponseCloseSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestCloseSessionConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public RequestCloseSessionConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InstantiationException, IllegalAccessException {

		RequestCloseSession message = (RequestCloseSession) getMessage();
		ResponseCloseSession response = new ResponseCloseSession();
		ConsortiumSession cs = (ConsortiumSession) getSession();

		ErrorCode ec = ErrorCode.OTHER;
		ControlResponseCode crc = ControlResponseCode.DENIED;
		response.setSumDeposits(0l);
		response.setSumTransfers(0l);
		response.setSumWidrawals(0l);
		
		try {

			if (cs == null) { // no session with bank
				throw new NoSessionException();
			}

			ConsortiumDAO dao = new ConsortiumDAO();
			cs.requestCloseSession();

			response.setSumDeposits(cs.getSumDeposits());
			response.setSumTransfers(cs.getSumTransfers());
			response.setSumWidrawals(cs.getSumWithdrawals());
			
			if (message.getSumDeposits() != cs.getSumDeposits()
					|| message.getSumTransfers() != cs.getSumTransfers()
					|| message.getSumWidrawals() != cs.getSumWithdrawals()) {
				
				cs.deniedCloseSession();
				throw new IncoherentSessionTotalsException();
				
			}

			cs.closeSessionAccepted();

			dao.closeSession(cs, connection);

			cs.resetSession();

			cs = null;

			crc = ControlResponseCode.ACCEPTED;
			ec = ErrorCode.CORRECT;

		} catch (NoSessionException e) {

			ec = ErrorCode.NOSESSION;
			crc = ControlResponseCode.DENIED;

		} catch (InvalidStateException e) {

			crc = ControlResponseCode.DENIED;

			if (!e.getCurrentContext().getStateTO().getStateName()
					.equals(StateName.TRAFFICSTOPPED)) {
				ec = ErrorCode.UNSTOPPEDTRAFFIC;
			} else {
				ec = ErrorCode.OTHER;
			}

		} catch (IncoherentSessionTotalsException e) {
			
			ec = ErrorCode.OTHER;
			crc = ControlResponseCode.DENIED;
			
		} finally {

			response.setFrom(message.getTo());
			response.setTo(message.getFrom());
			response.setErrorCode(ec);
			response.setResponseCode(crc);

			getTransportProcessor().send2Bank(response);
		}

		return cs;

	}

}
