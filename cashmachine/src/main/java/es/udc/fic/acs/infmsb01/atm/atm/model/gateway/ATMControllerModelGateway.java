package es.udc.fic.acs.infmsb01.atm.atm.model.gateway;

import java.io.IOException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.atm.controller.ATMViewController;
import es.udc.fic.acs.infmsb01.atm.atm.model.message.processor.ATMMessageProcessor;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.RequestWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountMovements;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseAccountsTransfer;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseCheckBalance;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseDeposit;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.ResponseWithdraw;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class ATMControllerModelGateway {

	private ATMMessageProcessor messageProcessor;
	private ATMViewController controller;

	public ATMControllerModelGateway(ATMMessageProcessor mp,
			ATMViewController controller) {
		this.messageProcessor = mp;
		this.controller = controller;

		messageProcessor.setGateway(this);
		controller.setGateway(this);
	}

	public final void updateView(DataResponseMessage drm) {
		// ////////////////////////////// CRAP_CODE¨
		if (drm instanceof ResponseCheckBalance) {
			updateView((ResponseCheckBalance) drm);
		} else if (drm instanceof ResponseWithdraw) {
			updateView((ResponseWithdraw) drm);
		} else if (drm instanceof ResponseDeposit) {
			updateView((ResponseDeposit) drm);
		} else if (drm instanceof ResponseAccountMovements) {
			updateView((ResponseAccountMovements) drm);
		} else if (drm instanceof ResponseAccountsTransfer) {
			updateView((ResponseAccountsTransfer) drm);
		}
		// //////////////////////////////////
	}

	public final void updateView(ResponseCheckBalance response) {
		String balanceString;
		balanceString = response.isBankOnline() ? response.getAccountBalance()
				.toString() : StateName.OFFLINE.toString();
		controller.updateViewResponseCheckBalance(response.getResponseCode()
				.toString(), balanceString);

	}

	public final void updateView(ResponseWithdraw response) {
		String balanceString;
		balanceString = response.isBankOnline() ? response.getAccountBalance()
				.toString() : StateName.OFFLINE.toString();
		controller.updateViewResponseCheckBalance(response.getResponseCode()
				.toString(), balanceString);

	}

	public final void updateView(ResponseDeposit response) {
		String balanceString;
		balanceString = response.isBankOnline() ? response.getAccountBalance()
				.toString() : StateName.OFFLINE.toString();
		controller.updateViewResponseCheckBalance(response.getResponseCode()
				.toString(), balanceString);
	}

	public final void updateView(ResponseAccountMovements response) {
		controller.updateViewResponseAccountMovements(response
				.getResponseCode().toString(), response.getAccountMovements());
	}

	public final void updateView(ResponseAccountsTransfer response) {
		String sourceBalanceString, destinationBalanceString;
		if (response.isBankOnline()) {
			sourceBalanceString = response.getSourceAccountBalance().toString();
			destinationBalanceString = response.getDestinationAccountBalance()
					.toString();
		} else {
			sourceBalanceString = StateName.OFFLINE.toString();
			destinationBalanceString = StateName.OFFLINE.toString();
		}
		
		controller.updateViewResponseAccountsTransfer(response
				.getResponseCode().toString(), sourceBalanceString,
				destinationBalanceString);
	}

	public final void requestCheckBalance(String creditCardNumber,
			byte associatedAccountNumber) {

		RequestCheckBalance request = new RequestCheckBalance();

		request.setCardNumber(creditCardNumber);
		request.setAssociatedAccountNumber(associatedAccountNumber);

		try {
			// process request
			messageProcessor.process(request);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public final void requestAccountMovements(String creditCardNumber,
			byte associatedAccountNumber) {

		RequestAccountMovements request = new RequestAccountMovements();

		request.setCardNumber(creditCardNumber);
		request.setAssociatedAccountNumber(associatedAccountNumber);

		try {
			messageProcessor.process(request);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public final void requestWithdraw(String creditCardNumber,
			byte associatedAccountNumber, short ammount) {

		RequestWithdraw request = new RequestWithdraw();

		request.setCardNumber(creditCardNumber);
		request.setAssociatedAccountNumber(associatedAccountNumber);
		request.setAmmount(ammount);

		try {
			messageProcessor.process(request);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public final void requestDeposit(String creditCardNumber,
			byte associatedAccountNumber, short ammount) {

		RequestDeposit request = new RequestDeposit();

		request.setCardNumber(creditCardNumber);
		request.setAssociatedAccountNumber(associatedAccountNumber);
		request.setAmmount(ammount);

		try {
			messageProcessor.process(request);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	public final void requestAccountsTransfer(String creditCardNumber,
			byte sourceAccountNumber, byte destinationAccountNumber,
			short ammount) {

		RequestAccountsTransfer request = new RequestAccountsTransfer();

		request.setCardNumber(creditCardNumber);
		request.setSourceAccountNumber(sourceAccountNumber);
		request.setDestinationAccountNumber(destinationAccountNumber);
		request.setAmmount(ammount);

		try {
			messageProcessor.process(request);

		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();			
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public final void setTransportProcessorLinkState(TransportProcessorLinkState linkState) {
		messageProcessor.setTransportProcessorLinkState(linkState);
	}

}
