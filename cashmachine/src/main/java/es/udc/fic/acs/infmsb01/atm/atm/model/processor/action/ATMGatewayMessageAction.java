package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action;

import es.udc.fic.acs.infmsb01.atm.atm.model.gateway.ATMControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.MessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.session.Session;

public abstract class ATMGatewayMessageAction extends MessageAction {

	private ATMControllerModelGateway gateway;
	
	public ATMGatewayMessageAction(Session session, Message message, ATMControllerModelGateway gateway) {
		super(session, message);
		this.gateway = gateway;
	}

	public ATMGatewayMessageAction() {
	}

	public ATMControllerModelGateway getGateway() {
		return gateway;
	}

	public void setGateway(ATMControllerModelGateway gateway) {
		this.gateway = gateway;
	}
	
}
