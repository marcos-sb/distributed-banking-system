package es.udc.fic.acs.infmsb01.atm.atm.model.processor.action.instance;

import java.io.IOException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.NetAction;

public final class RequestAccountsTransferATMAction extends NetAction {

	public RequestAccountsTransferATMAction() {
		super();
	}

	@Override
	public Object execute() throws IOException, InstantiationException, IllegalAccessException, ParseException {
		this.getTransportProcessor().send(getMessage());
		
		return getMessage();
	}

}
