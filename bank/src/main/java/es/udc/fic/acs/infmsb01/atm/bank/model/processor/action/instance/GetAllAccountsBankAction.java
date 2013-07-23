package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;

public class GetAllAccountsBankAction extends BankPersistenceAction {

	public GetAllAccountsBankAction() {
		super(false);
	}

	@Override
	public Object execute(Connection connection) throws SQLException, BadCreditCardException {
		
		BankDAO dao = new BankDAO();
		return dao.queryAllAccounts(connection);
		
	}

}
