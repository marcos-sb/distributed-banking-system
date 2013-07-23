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
import es.udc.fic.acs.infmsb01.atm.consortium.model.dao.ConsortiumDAO;
import es.udc.fic.acs.infmsb01.atm.consortium.model.processor.action.ConsortiumPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;

public class CreatePartialSessionConsortiumPersistenceAction extends
		ConsortiumPersistenceAction {

	public CreatePartialSessionConsortiumPersistenceAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection)
			throws InstanceNotFoundException, SQLException, IOException,
			InvalidStateException, InstantiationException,
			IllegalAccessException, BadCreditCardException,
			BadAccountException, SessionOpenedException, NoSessionException {
		
		ConsortiumDAO dao = new ConsortiumDAO();
		ConsortiumSession cs = (ConsortiumSession) getSession();
		
		dao.createPartialSession(cs, getTransportProcessor().getConsortiumInfo(), connection);
		
		return cs;
		
	}

}
