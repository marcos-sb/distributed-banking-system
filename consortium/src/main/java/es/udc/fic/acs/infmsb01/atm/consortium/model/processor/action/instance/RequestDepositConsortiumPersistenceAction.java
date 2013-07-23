package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.PendingDataRequestMessage;

public final class RequestDepositConsortiumPersistenceAction extends
		DataRequestConsortiumPersistenceAction {

	public RequestDepositConsortiumPersistenceAction() {
		super();
	}
	
	@Override
	protected final Object serveOffline(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException {
		
		ResponseDeposit response = new ResponseDeposit();
		
		ConsortiumDAO dao = new ConsortiumDAO();
		ConsortiumSession session = (ConsortiumSession) getSession();
		DataRequestMessage message = (DataRequestMessage) getMessage();
		
		message.setTo(session.getBank());
		
		PendingDataRequestMessage pendingMessage = session.appendPendingMessage(message);
		dao.insertPendingMessage(session.getBank().getId(), pendingMessage, connection);
		
		response.setAccountBalance(0d);
		response.setBankOnline(false);
		response.setChannelNumber((byte)0);
		response.setFrom(session.getConsortium());
		response.setMessageNumber((byte)0);
		response.setResponseCode(DataResponseCode.ACCEPTED);
		response.setTo(getMessage().getFrom());
		
		getTransportProcessor().send2ATM(response);
		
		return getMessage();
	}

}
