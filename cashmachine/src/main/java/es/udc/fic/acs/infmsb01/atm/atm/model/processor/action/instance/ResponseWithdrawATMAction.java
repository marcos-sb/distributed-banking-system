package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;

public final class ResponseWithdrawATMAction extends ATMGatewayMessageAction {

	public ResponseWithdrawATMAction() {
		super();
	}

	@Override
	public Object execute() {
		getGateway().updateView((ResponseWithdraw) getMessage());
		return getMessage();
	}

}
