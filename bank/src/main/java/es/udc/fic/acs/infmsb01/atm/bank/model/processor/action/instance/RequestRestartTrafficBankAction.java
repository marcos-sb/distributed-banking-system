package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;

public final class RequestRestartTrafficBankAction extends BankPersistenceAction {

	public RequestRestartTrafficBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws SQLException,
			IOException, InstantiationException, IllegalAccessException, InvalidStateException {

		final BankSession session = (BankSession) getSession();
		
		RequestRestartTraffic message = (RequestRestartTraffic) getMessage();
		session.requestRestartTraffic();
		
		message.setFrom(session.getBank());
		message.setTo(session.getConsortium());
		
		session.getSmartTimer().setTask(new TimerTask() {
			
			@Override
			public void run() {
				
				this.cancel();
				
				session.deniedRestartTraffic();
				getGateway().updateView(session);
				
			}
			
		});
		
		getTransport().send(message);

		return message;

	}

}
