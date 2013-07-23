package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

public final class CreditCardTO {

	String cardNumber;
	
	public CreditCardTO(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public CreditCardTO() {}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
}
