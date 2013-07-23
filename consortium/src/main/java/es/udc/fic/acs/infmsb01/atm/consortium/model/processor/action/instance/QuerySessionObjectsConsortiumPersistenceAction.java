package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public final class QuerySessionObjectsConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public QuerySessionObjectsConsortiumPersistenceAction() {
		super(false);
	}

	@Override
	public Object execute(Connection connection) throws SQLException, IOException, ClassNotFoundException, BusyChannelException {
			
		ConsortiumDAO dao = new ConsortiumDAO();
		ArrayList<ConsortiumSession> tempList = (ArrayList<ConsortiumSession>) dao.querySessions(connection);
		
		return tempList;
		
	}

}
