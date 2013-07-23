package es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.instance;

import java.sql.Connection;
import java.sql.SQLException;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.BankDAO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.processor.action.BankPersistenceAction;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;

public final class GetAccountMovementsBankAction extends BankPersistenceAction {

	private CreditCardTO creditCard;
	private byte associatedAccountNumber;

	public GetAccountMovementsBankAction(CreditCardTO creditCard,
			byte associatedAccountNumber) {

		super(false); // Not transactional
		this.creditCard = creditCard;
		this.associatedAccountNumber = associatedAccountNumber;
	}

	@Override
	public Object execute(Connection connection) throws SQLException, BadCreditCardException, InstanceNotFoundException, BadAccountException {

		BankDAO dao = new BankDAO();
		return dao.queryAccountMovements(creditCard, associatedAccountNumber,
				connection);

	}

}
