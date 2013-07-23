package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.common.model.message.code.DataResponseCode;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class RequestAccountMovementsConsortiumPersistenceAction extends
		DataRequestConsortiumPersistenceAction {

	public RequestAccountMovementsConsortiumPersistenceAction() {
		super();
	}
	
	@Override
	protected final Object serveOffline(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException {
		ResponseAccountMovements response = new ResponseAccountMovements();
		
		ConsortiumSession session = (ConsortiumSession) getSession();
		
		response.setBankOnline(false);
		response.setChannelNumber((byte)0);
		response.setFrom(session.getConsortium());
		response.setMessageNumber((byte)0);
		response.setResponseCode(DataResponseCode.DENIED);
		response.setTo(getMessage().getFrom());
		
		getTransportProcessor().send2ATM(response);
		
		return getMessage();
	}

}
