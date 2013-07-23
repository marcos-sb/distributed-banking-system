package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.InvalidStateException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;

public abstract class DataRequestBankPersistenceAction extends
		BankPersistenceAction {

	public DataRequestBankPersistenceAction(boolean transactional) {
		super(transactional);
	}

	@Override
	public final Object execute(Connection connection)
			throws InvalidStateException, SQLException, BadCreditCardException,
			InstanceNotFoundException, BadAccountException, IOException,
			InstantiationException, IllegalAccessException {

		DataRequestMessage message = (DataRequestMessage) getMessage();
		BankSession bs = (BankSession) getSession();

		if (bs != null) {
			bs.acceptDataMessage();

			if (bs.getContextState().getStateName()
					.equals(StateName.RECOVERINGMESSAGES)) {

				try {
					// check whether response is already prepared
					DataResponseMessage drm = bs.getResponse(message);

					if (drm != null) {
						getTransport().send(drm);
						return message;
					}

				} catch (ClassCastException e) {

				}
			}

			return doExecute(connection);
		}

		return insertLogMessage(connection);

	}

	private Object insertLogMessage(Connection connection) throws SQLException,
			IOException {

		BankDAO dao = new BankDAO();

		dao.insertLogMessage(getMessage(), connection);

		return getMessage();

	}

	protected abstract Object doExecute(Connection connection)
			throws SQLException, BadCreditCardException,
			InstanceNotFoundException, BadAccountException, IOException,
			InstantiationException, IllegalAccessException;

}
