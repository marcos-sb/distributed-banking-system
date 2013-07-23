package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

public final class BankAccountTO {

	String accountNumber;
	double balance;
	
	public BankAccountTO(String accountNumber, double balance) {
		this.accountNumber = accountNumber;
		this.balance = balance;
	}
	
	public BankAccountTO() {}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
}
