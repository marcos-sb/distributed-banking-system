package es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;

public final class QueryBanksInfoConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public QueryBanksInfoConsortiumPersistenceAction() {
		super(false);
	}

	@Override
	public Object execute(Connection connection) throws SQLException {
			
		ConsortiumDAO dao = new ConsortiumDAO();
		ArrayList<BankInfo> tempList = (ArrayList<BankInfo>) dao.queryBanksInfo(connection);
		
		return tempList;
		
	}

}
