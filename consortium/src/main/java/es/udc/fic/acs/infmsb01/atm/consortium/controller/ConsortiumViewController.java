package es.udc.fic.acs.infmsb01.atm.consortium.controller;

import java.util.ArrayList;
import java.util.Vector;

import es.udc.fic.acs.infmsb01.atm.common.model.message.DataMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;
import es.udc.fic.acs.infmsb01.atm.consortium.model.gateway.ConsortiumControllerModelGateway;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.PendingDataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.consortium.view.ConsortiumView;

public final class ConsortiumViewController {

	private ConsortiumControllerModelGateway gateway;
	private ConsortiumView view;

	private Vector<String> sessionColNames, pendingColNames, pipelineColNames;

	public ConsortiumViewController(String windowTitle) {
		this.gateway = null;
		this.view = new ConsortiumView(this, windowTitle);

		// ////////////////////////////////
		sessionColNames = new Vector<String>();
		sessionColNames.add("BankID");
		sessionColNames.add("State");
		sessionColNames.add("SumW");
		sessionColNames.add("SumD");
		sessionColNames.add("SumT");
		sessionColNames.add("# Pending");

		pendingColNames = new Vector<String>();
		pendingColNames.add("From");
		pendingColNames.add("To");
		pendingColNames.add("Message");
		pendingColNames.add("Payload");

		pipelineColNames = new Vector<String>();
		pipelineColNames.add("Channel #");
		pipelineColNames.add("Message #");
		pipelineColNames.add("From");
		pipelineColNames.add("To");
		pipelineColNames.add("Message");
		pipelineColNames.add("Payload");
		// ////////////////////////////////

	}

	public final void setGateway(ConsortiumControllerModelGateway gateway) {
		this.gateway = gateway;
	}

	public final void updateViewSession(ConsortiumSession modifiedSession,
			ArrayList<ConsortiumSession> allSessions) {
		Vector<Vector<String>> sessionTableData = new Vector<Vector<String>>();
		Vector<Vector<String>> pipelineTableData = new Vector<Vector<String>>();
		Vector<Vector<String>> pendingTableData = new Vector<Vector<String>>();

		for (ConsortiumSession cs : allSessions) {
			Vector<String> sessionTableRow = new Vector<String>();
			sessionTableRow.add(cs.getBank().toString());
			sessionTableRow.add(cs.getContextState().getStateName().toString());
			sessionTableRow.add(String.valueOf(cs.getSumWithdrawals()));
			sessionTableRow.add(String.valueOf(cs.getSumDeposits()));
			sessionTableRow.add(String.valueOf(cs.getSumTransfers()));
			sessionTableRow.add(String.valueOf(cs.getPendingSize()));

			sessionTableData.add(sessionTableRow);
		}

		view.updateSessionTable(sessionTableData, sessionColNames);

		if (view.getSelectedBankId() == null
				|| modifiedSession == null || !view.getSelectedBankId().equals(
						(String) modifiedSession.getBank().getId())) {
			return;
		}

		ArrayList<DataMessage> pipelineMessages = modifiedSession
				.getAllCurrentMessages();
		for (DataMessage dm : pipelineMessages) {
			Vector<String> pipelineTableRow = new Vector<String>();
			pipelineTableRow.add(String.valueOf(dm.getChannelNumber()));
			pipelineTableRow.add(String.valueOf(dm.getMessageNumber()));
			pipelineTableRow.add(dm.getFrom().getId());
			pipelineTableRow.add(dm.getTo().toString());
			pipelineTableRow.add(dm.getClass().getSimpleName());
			pipelineTableRow.add(dm.toString());

			pipelineTableData.add(pipelineTableRow);
		}

		view.updatePipelineTable(pipelineTableData, pipelineColNames);

		ArrayList<PendingDataRequestMessage> pendingMessages = modifiedSession
				.getPendingMessages();
		
		for (PendingDataRequestMessage m : pendingMessages) {
			
			DataRequestMessage message = m.getDataRequestMessage();
			Vector<String> pendingTableRow = new Vector<String>();
			pendingTableRow.add(message.getFrom().toString());
			pendingTableRow.add(message.getTo().toString());
			pendingTableRow.add(message.getClass().getSimpleName());
			pendingTableRow.add(message.toString());

			pendingTableData.add(pendingTableRow);
		}

		view.updatePendingTable(pendingTableData, pendingColNames);

	}

	public final void requestRecovery(String bankID) {
		gateway.requestRecovery(bankID);
	}

	public final void requestEndRecovery(String bankID) {
		gateway.requestEndRecovery(bankID);
	}

	public void selectedSessionChange(String selectedBankId) {
		ConsortiumSession cs = gateway.fetchConsortiumSession(selectedBankId);

		Vector<Vector<String>> pipelineTableData = new Vector<Vector<String>>();
		Vector<Vector<String>> pendingTableData = new Vector<Vector<String>>();

		ArrayList<DataMessage> pipelineMessages = cs.getAllCurrentMessages();
		for (DataMessage dm : pipelineMessages) {
			Vector<String> pipelineTableRow = new Vector<String>();
			pipelineTableRow.add(String.valueOf(dm.getChannelNumber()));
			pipelineTableRow.add(String.valueOf(dm.getMessageNumber()));
			pipelineTableRow.add(dm.getFrom().getId());
			pipelineTableRow.add(dm.getTo().toString());
			pipelineTableRow.add(dm.getClass().getSimpleName());
			pipelineTableRow.add(dm.toString());

			pipelineTableData.add(pipelineTableRow);
		}

		view.updatePipelineTable(pipelineTableData, pipelineColNames);

		ArrayList<PendingDataRequestMessage> pendingMessages = cs.getPendingMessages();
		for (PendingDataRequestMessage m : pendingMessages) {
			
			DataRequestMessage message = m.getDataRequestMessage();
			Vector<String> pendingTableRow = new Vector<String>();
			pendingTableRow.add(message.getFrom().toString());
			pendingTableRow.add(message.getTo().toString());
			pendingTableRow.add(message.getClass().getSimpleName());
			pendingTableRow.add(message.toString());

			pendingTableData.add(pendingTableRow);
		}

		view.updatePendingTable(pendingTableData, pendingColNames);

	}
	
	public void setTransportProcessorBankLinkState(TransportProcessorLinkState linkState) {
		gateway.setTransportProcessorBankLinkState(linkState);
	}
	
	public void setTransportProcessorATMLinkState(TransportProcessorLinkState linkState) {
		gateway.setTransportProcessorATMLinkState(linkState);
	}

}
