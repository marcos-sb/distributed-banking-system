package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.SessionOpenedException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.PendingDataRequestMessage;

public class DataResponseConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public DataResponseConsortiumPersistenceAction(boolean transactional) {
		super(transactional);
	}

	public DataResponseConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public final Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException, BadCreditCardException,
			BadAccountException, SessionOpenedException, NoSessionException {

		ConsortiumDAO dao = new ConsortiumDAO();

		DataResponseMessage response = (DataResponseMessage) getMessage();
		ConsortiumSession cs = (ConsortiumSession) getSession();

		if (cs == null) {
			return response;
		}
		
		cs.acceptDataMessage();
		boolean inserted = cs.appendMessage(response);
		
		if(!inserted) {
			return response;
		}

		DataRequestMessage associatedRequest = cs.getRequest(response);

		response.setFrom(cs.getConsortium());
		response.setTo(cs.getATM(response.getChannelNumber()));

		dao.insertDataResponseMessage(cs.getBank().getId(), response,
				connection);

		if (!associatedRequest.isBankOnline()) { // aR was a pendingmessage
			PendingDataRequestMessage pendingMessage = cs.removePendingMessage(associatedRequest);
			
			if(pendingMessage != null) {
			
				dao.removePendingMessage(cs.getBank().getId(), pendingMessage.getPendingNumber(),
					connection);
			}
			
		} else {
			getTransportProcessor().send2ATM(response);
		}

		// ////////////////////////////hook to subclass
		alterSession(connection);
		// ///////////////////////////

		dao.updateSessionTotals(cs.getBank().getId(), cs.getSumDeposits(),
				cs.getSumTransfers(), cs.getSumWithdrawals(), connection);

		return response;

	}

	/*
	 * actions which change session contents should override alterSession;
	 * persistence storage is handled by this class ;)
	 */
	protected void alterSession(Connection connection) {
	}

}
