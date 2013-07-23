package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimerTask;

import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;

public final class RequestOpenSessionBankAction extends BankPersistenceAction {

	public RequestOpenSessionBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws SQLException,
			IOException, InstantiationException, IllegalAccessException, InvalidStateException {

		final BankSession session = (BankSession) getSession();
		Calendar now = Calendar.getInstance();
		
		RequestOpenSession message = (RequestOpenSession) getMessage();
		session.requestOpenSession();
		
		message.setFrom(session.getBank());
		message.setTo(session.getConsortium());
		message.setDate(now);
		//messagenumberofchannels set in facade
		
		session.setNumberOfChannels(message.getNumberOfChannels());
		message.setBankInfo((BankInfo) getTransport().getOriginInfo());
		
		session.getSmartTimer().setTask(new TimerTask() {
			
			@Override
			public void run() {
				
				this.cancel();
				
				session.deniedOpenSession();
				getGateway().updateView(session);
				
			}
			
		});
		
		getTransport().send(message);

		return message;

	}

}
