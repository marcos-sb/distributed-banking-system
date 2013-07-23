package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.ATMGatewayMessageAction;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;

public final class ResponseAccountsTransferATMAction extends ATMGatewayMessageAction {

	public ResponseAccountsTransferATMAction() {
		super();
	}

	@Override
	public Object execute() {
		getGateway().updateView((ResponseAccountsTransfer) getMessage());
		return getMessage();
	}

}
