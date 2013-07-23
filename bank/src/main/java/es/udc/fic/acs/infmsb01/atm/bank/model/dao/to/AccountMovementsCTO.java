package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

import java.util.ArrayList;

public final class AccountMovementsCTO {

	BankAccountTO account;
	ArrayList<AccountMovementTO> movements;
	
	public AccountMovementsCTO(BankAccountTO account) {
		this.account = account;
		this.movements = new ArrayList<AccountMovementTO>();
	}

	public BankAccountTO getAccount() {
		return account;
	}

	public void setAccount(BankAccountTO account) {
		this.account = account;
	}

	public ArrayList<AccountMovementTO> getMovements() {
		return movements;
	}
	
	public void addMovement(AccountMovementTO movement) {
		movements.add(movement);
	}
	
}
