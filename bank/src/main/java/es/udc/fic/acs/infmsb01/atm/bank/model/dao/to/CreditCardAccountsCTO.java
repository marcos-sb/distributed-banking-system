package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

import java.util.ArrayList;

public final class CreditCardAccountsCTO {

	CreditCardTO creditCard;
	ArrayList<BankAccountTO> accounts;
	
	public CreditCardAccountsCTO(CreditCardTO creditCard) {
		this.creditCard = creditCard;
		this.accounts = new ArrayList<BankAccountTO>();
	}

	public CreditCardTO getCreditCard() {
		return creditCard;
	}

	public final void setCreditCard(CreditCardTO creditCard) {
		this.creditCard = creditCard;
	}

	public final ArrayList<BankAccountTO> getAccounts() {
		return accounts;
	}
	
	public final void addAccount(BankAccountTO ba) {
		accounts.add(ba);
	}

}
