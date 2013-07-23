package es.udc.fic.acs.infmsb01.atm.atm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import es.udc.fic.acs.infmsb01.atm.atm.model.gateway.ATMControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.atm.view.ATMView;
import es.udc.fic.acs.infmsb01.atm.common.model.message.instance.data.AccountMovement;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class ATMViewController {

	private ATMView view;
	private ATMControllerModelGateway gateway;

	public ATMViewController(String windowTitle) {
		this.gateway = null;
		view = new ATMView(this, windowTitle);
	}

	public void setGateway(ATMControllerModelGateway gateway) {
		this.gateway = gateway;
	}

	public final void requestCheckBalance(String creditCardNumber,
			String associatedAccountNumber) {
		// TODO Capture format exception (parse problem)
		gateway.requestCheckBalance(creditCardNumber,
				Byte.parseByte(associatedAccountNumber));

	}

	public final void requestAccountMovements(String creditCardNumber,
			String associatedAccountNumber) {

		gateway.requestAccountMovements(creditCardNumber,
				Byte.parseByte(associatedAccountNumber));

	}

	public final void requestWithdraw(String creditCardNumber,
			String associatedAccountNumber, String ammount) {

		gateway.requestWithdraw(creditCardNumber,
				Byte.parseByte(associatedAccountNumber),
				Short.parseShort(ammount));

	}

	public final void requestDeposit(String creditCardNumber,
			String associatedAccountNumber, String ammount) {

		gateway.requestDeposit(creditCardNumber,
				Byte.parseByte(associatedAccountNumber),
				Short.parseShort(ammount));

	}

	public final void requestAccountsTransfer(String creditCardNumber,
			String sourceAccountNumber, String destinationAccountNumber,
			String ammount) {

		gateway.requestAccountsTransfer(creditCardNumber,
				Byte.parseByte(sourceAccountNumber),
				Byte.parseByte(destinationAccountNumber),
				Short.parseShort(ammount));

	}

	public final void updateViewResponseCheckBalance(String message, String balance) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		Vector<String> header = new Vector<String>();

		data.add(row);

		row.add(message);
		row.add(balance);

		header.add("Result");
		header.add("Balance");

		view.paintResult(data, header);
	}

	public final void updateViewResponseAccountMovements(String message,
			ArrayList<AccountMovement> movs) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row;
		Vector<String> header = new Vector<String>();
		SimpleDateFormat sdfDateTime = new SimpleDateFormat(
				"dd/MM/yyyyHH:mm:ss");

		for (AccountMovement mov : movs) {
			row = new Vector<String>();
			row.add(message);
			row.add(mov.getMovementType().toString());
			row.add(String.valueOf(mov.getMovementAmmount()));
			row.add(sdfDateTime.format(mov.getDate().getTime()));
			data.add(row);
		}

		if(movs.size() == 0) {
			row = new Vector<String>();
			row.add(message);
			data.add(row);
		}
		
		header.add("Result");
		header.add("Type");
		header.add("Ammount");
		header.add("Time");

		view.paintResult(data, header);
	}

	public final void updateViewResponseWithdraw(String message, String balance) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		Vector<String> header = new Vector<String>();

		data.add(row);

		row.add(message);
		row.add(balance);

		header.add("Result");
		header.add("Balance");

		view.paintResult(data, header);
	}

	public final void updateViewResponseDeposit(String message, String balance) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		Vector<String> header = new Vector<String>();

		data.add(row);

		row.add(message);
		row.add(balance);

		header.add("Result");
		header.add("Balance");

		view.paintResult(data, header);
	}

	public final void updateViewResponseAccountsTransfer(String message,
			String sourceBalance, String destinationBalance) {
		
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row = new Vector<String>();
		Vector<String> header = new Vector<String>();

		data.add(row);

		row.add(message);
		row.add(sourceBalance);
		row.add(destinationBalance);

		header.add("Result");
		header.add("Source Balance");
		header.add("Destination Balance");

		view.paintResult(data, header);
	}
	
	public final void setTransportProcessorLinkState(TransportProcessorLinkState linkState) {
		gateway.setTransportProcessorLinkState(linkState);
	}

}
