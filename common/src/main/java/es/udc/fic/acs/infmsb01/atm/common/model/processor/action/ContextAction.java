package es.udc.fic.acs.infmsb01.atm.common.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.common.model.session.Session;

public abstract class ContextAction extends Action {

	private Session session;
	
	public ContextAction(Session session) {
		this.session = session;
	}
	
	public ContextAction() {
		super();
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
}
