package es.udc.fic.acs.infmsb01.atm.common.model.processor.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoSessionException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.SessionOpenedException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.session.Session;

public abstract class PersistenceAction {

	private Session session;
	private Message message;
	private boolean transactional;
	
	public PersistenceAction(boolean transactional) {
		this.transactional = transactional;
	}

	public abstract Object execute(Connection connection) throws InstanceNotFoundException, SQLException, IOException, InvalidStateException, InstantiationException, IllegalAccessException, BadCreditCardException, BadAccountException, SessionOpenedException, ClassNotFoundException, NoSessionException, BusyChannelException;
	
	public boolean isTransactional() {
		return transactional;
	}

	public void setTransactional(boolean transactional) {
		this.transactional = transactional;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
}
