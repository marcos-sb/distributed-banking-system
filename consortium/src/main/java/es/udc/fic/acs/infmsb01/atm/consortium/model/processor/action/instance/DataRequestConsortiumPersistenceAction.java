package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public abstract class DataRequestConsortiumPersistenceAction extends ConsortiumPersistenceAction {
	
	public DataRequestConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public final Object execute(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException, InvalidStateException, BusyChannelException {
		
		ConsortiumSession cs = (ConsortiumSession) getSession();
		
		if(cs != null && cs.isBankOnline()) {
			return doExecute(connection);
		}
	
		return serveOffline(connection);
		
	}
	
	/* 
	* to be overrided by subclasses which actions can be processed offline
	**/
	protected abstract Object serveOffline(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException;
	
	public final Object doExecute(Connection connection) throws SQLException, IOException, InstantiationException, IllegalAccessException, InvalidStateException, BusyChannelException {

		ConsortiumDAO dao = new ConsortiumDAO();
		
		ConsortiumSession session = (ConsortiumSession) getSession();
		DataRequestMessage message = (DataRequestMessage) getMessage();
		
		session.acceptDataMessage();
		
		session.appendMessage(message);
		session.setATM(message.getChannelNumber(), message.getFrom());
		message.setFrom(session.getConsortium());
		message.setTo(session.getBank());
		dao.insertDataRequestMessage(session.getBank().getId(), message, connection);
		
		getTransportProcessor().send2Bank(message);
		
		return message;
		
	}

}
