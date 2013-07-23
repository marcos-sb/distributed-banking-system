package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;

public final class ResponseDepositATMAction extends ATMGatewayMessageAction {

	public ResponseDepositATMAction() {
		super();
	}

	@Override
	public Object execute() {
		getGateway().updateView((ResponseDeposit) getMessage());
		return getMessage();
	}

}
