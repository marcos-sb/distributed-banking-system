package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;

public final class ResponseAccountMovementsATMAction extends ATMGatewayMessageAction {

	public ResponseAccountMovementsATMAction() {
		super();
	}

	@Override
	public Object execute() {
		getGateway().updateView((ResponseAccountMovements) getMessage());
		return getMessage();
	}

}
