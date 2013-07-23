package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;

public final class ResponseCheckBalanceATMAction extends ATMGatewayMessageAction {

	public ResponseCheckBalanceATMAction() {
		super();
	}

	@Override
	public Object execute() {
		getGateway().updateView((ResponseCheckBalance)getMessage());
		return getMessage();
	}

}
