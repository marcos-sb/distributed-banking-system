package es.udc.fic.acs.infmsb01.atm.common.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.session.Session;

public abstract class MessageAction extends ContextAction {

	private Message message;
	
	public MessageAction(Session session, Message message) {
		super(session);
		this.message = message;
	}
	
	public MessageAction() {
		super();
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}

}
