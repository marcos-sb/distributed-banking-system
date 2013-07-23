package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;

public final class GetCreditCardAccountsBankAction extends BankPersistenceAction {

	private CreditCardTO creditCard;
	
	public GetCreditCardAccountsBankAction(CreditCardTO creditCard) {
		super(false);
		this.creditCard = creditCard;
	}

	@Override
	public Object execute(Connection connection) throws SQLException, BadCreditCardException {
		
		BankDAO dao = new BankDAO();
		
		return dao.queryCreditCardAccounts(creditCard, connection);
		
	}

}
