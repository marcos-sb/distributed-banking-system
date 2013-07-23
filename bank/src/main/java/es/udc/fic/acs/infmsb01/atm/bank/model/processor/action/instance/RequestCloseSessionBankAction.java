package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimerTask;

import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;

public final class RequestCloseSessionBankAction extends BankPersistenceAction {

	public RequestCloseSessionBankAction() {
		super(true);
	}

	@Override
	public Object execute(Connection connection) throws SQLException,
			IOException, InstantiationException, IllegalAccessException, InvalidStateException {

		final BankSession bs = (BankSession) getSession();
		bs.requestCloseSession();
		
		bs.getSmartTimer().setTask(new TimerTask() {
			
			@Override
			public void run() {
				
				this.cancel();
				
				bs.deniedRecovery();
				getGateway().updateView(bs);
				
			}
			
		});
		
		RequestCloseSession message = (RequestCloseSession) getMessage();
		message.setFrom(bs.getBank());
		message.setTo(bs.getConsortium());
		message.setSumDeposits(bs.getSumDeposits());
		message.setSumTransfers(bs.getSumTransfers());
		message.setSumWidrawals(bs.getSumWithdrawals());
		
		getTransport().send(message);

		return message;

	}

}
