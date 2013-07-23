package es.udc.fic.acs.infmsb01.atm.bank.model.gateway;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import es.udc.fic.acs.infmsb01.atm.bank.controller.BankViewController;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.Card2AccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardAccountsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.message.processor.BankMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestCloseSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestOpenSession;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestRestartTraffic;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.control.RequestStopTraffic;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class BankControllerModelGateway {

	private BankMessageProcessor messageProcessor;
	private BankViewController controller;
	
	public BankControllerModelGateway(BankMessageProcessor messageProcessor, BankViewController controller) {
		this.messageProcessor = messageProcessor;
		this.controller = controller;
		
		messageProcessor.setGateway(this);
		controller.setGateway(this);
	}
	
	public final void updateView(BankSession bs) {
		controller.updateViewSession(bs);
	}
	
	public final void requestOpenSession(byte numberOfChannels) {
		
		RequestOpenSession request = new RequestOpenSession();
		
		request.setNumberOfChannels(numberOfChannels);
		
		try {
			
			messageProcessor.process(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public final void requestCloseSession() {
		
		RequestCloseSession request = new RequestCloseSession();
		
		try {
			
			messageProcessor.process(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public final void requestStopTraffic() {

		RequestStopTraffic request = new RequestStopTraffic();
		
		
		try {
			
			messageProcessor.process(request);
		
			
		} catch (IOException e) {			
			e.printStackTrace();			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final void requestRestartTraffic() {
		
		RequestRestartTraffic request = new RequestRestartTraffic();
		
		try {
			
			messageProcessor.process(request);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public final List<Card2AccountTO> getAllAccounts() throws Exception {
		try {
			return messageProcessor.getAllAccounts();
		} catch (SQLException e) {
			throw new Exception(e);
		}
	}
	
	public final AccountMovementsCTO getAccountMovements(
			CreditCardTO creditCard, byte associatedAccountNumber) throws Exception {
		
		try {
			return messageProcessor.getAccountMovements(creditCard, associatedAccountNumber);
		} catch (SQLException e) {
			throw new Exception(e);
		} catch(Exception e) {
			throw new Exception(e);
		}
		
	}
	
	public final CreditCardAccountsCTO getCreditCardAccounts(
			CreditCardTO creditCard) throws Exception {
		
		try {
			return messageProcessor.getCreditCardAccounts(creditCard);
		} catch(SQLException e) {
			throw new Exception(e);
		}

	}
	
	public void setTransportProcessorLinkState(
			TransportProcessorLinkState linkState) {
		
		messageProcessor.setTransportProcessorLinkState(linkState);
		
	}
	
}
