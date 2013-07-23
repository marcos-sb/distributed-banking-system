package es.udc.fic.acs.infmsb01.atm.bank.model.dao.to;

public final class Card2AccountTO {

	private final String cardNumber;
	private final byte associatedAccountNumber;
	private final String realAccountNumber;
	
	public Card2AccountTO(String cardNumber, byte associatedAccountNumber, String realAccountNumber) {
		this.cardNumber = cardNumber;
		this.associatedAccountNumber = associatedAccountNumber;
		this.realAccountNumber = realAccountNumber;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public byte getAssociatedAccountNumber() {
		return associatedAccountNumber;
	}
	public String getRealAccountNumber() {
		return realAccountNumber;
	}
	
}
