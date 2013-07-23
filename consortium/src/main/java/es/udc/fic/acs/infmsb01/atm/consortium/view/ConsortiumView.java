package es.udc.fic.acs.infmsb01.atm.consortium.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;
import es.udc.fic.acs.infmsb01.atm.consortium.controller.ConsortiumViewController;

public final class ConsortiumView extends JFrame implements ActionListener,
		ListSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ConsortiumViewController controller;

	private JButton sendBtn;
	private JScrollPane sessionScp, pendingScp, pipelineScp;
	private JTable sessionTable, pendingTable, pipelineTable;
	private JPanel panel;
	private JComboBox actionComboBox;
	private JLabel bankIDLabel;

	private String selectedBankId = "";

	public ConsortiumView(ConsortiumViewController controller, String title) {
		this.controller = controller;
		paintGUI(title);
	}

	private void paintGUI(String title) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();

		sessionTable = new JTable();
		sessionTable.setPreferredScrollableViewportSize(new Dimension(700, 70));
		sessionTable.setFillsViewportHeight(true);
		sessionTable.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		sessionTable.getSelectionModel().addListSelectionListener(this);

		sessionScp = new JScrollPane(sessionTable);

		panel.add(sessionScp);

		String[] actions = { "Request Recovery", "Request End Recovery" };

		actionComboBox = new JComboBox(actions);
		panel.add(actionComboBox);

		sendBtn = new JButton("Send");
		sendBtn.setEnabled(false);
		sendBtn.addActionListener(this);

		bankIDLabel = new JLabel("bankID");
		bankIDLabel.setLabelFor(sendBtn);

		panel.add(bankIDLabel);
		panel.add(sendBtn);

		pendingTable = new JTable();
		pendingTable.setPreferredScrollableViewportSize(new Dimension(700, 70));
		pendingTable.setFillsViewportHeight(true);

		pendingScp = new JScrollPane(pendingTable);

		panel.add(pendingScp);

		pipelineTable = new JTable();
		pipelineTable
				.setPreferredScrollableViewportSize(new Dimension(700, 70));
		pipelineTable.setFillsViewportHeight(true);

		pipelineScp = new JScrollPane(pipelineTable);

		panel.add(pipelineScp);

		String[] ATMLinkActions = { "Link to ATMs Online", "Disable Reception",
				"Disable Transmission", "Offline" };

		final JComboBox controlATMLinkStateCB = new JComboBox(ATMLinkActions);
		controlATMLinkStateCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				switch (controlATMLinkStateCB.getSelectedIndex()) {
				case 0:
					controller.setTransportProcessorATMLinkState(TransportProcessorLinkState.ONLINE);
					break;

				case 1:
					controller.setTransportProcessorATMLinkState(TransportProcessorLinkState.DISABLEDRECEPTION);
					break;

				case 2:
					controller.setTransportProcessorATMLinkState(TransportProcessorLinkState.DISABLEDTRANSMISSION);
					break;

				case 3:
					controller.setTransportProcessorATMLinkState(TransportProcessorLinkState.OFFLINE);
				}

			}
		});
		
		panel.add(controlATMLinkStateCB);
		
		String[] bankLinkActions = { "Link to Banks Online", "Disable Reception",
				"Disable Transmission", "Offline" };

		final JComboBox controlBankLinkStateCB = new JComboBox(bankLinkActions);
		controlBankLinkStateCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				switch (controlBankLinkStateCB.getSelectedIndex()) {
				case 0:
					controller.setTransportProcessorBankLinkState(TransportProcessorLinkState.ONLINE);
					break;

				case 1:
					controller.setTransportProcessorBankLinkState(TransportProcessorLinkState.DISABLEDRECEPTION);
					break;

				case 2:
					controller.setTransportProcessorBankLinkState(TransportProcessorLinkState.DISABLEDTRANSMISSION);
					break;

				case 3:
					controller.setTransportProcessorBankLinkState(TransportProcessorLinkState.OFFLINE);
				}

			}
		});
		
		panel.add(controlBankLinkStateCB);
		
		this.getContentPane().add(panel);
		this.setTitle("ACME Consortium [" + title + "]" );
		this.pack();
		this.setVisible(true);
	}

	public final void updateSessionTable(Vector<Vector<String>> data,
			Vector<String> header) {
		sessionTable.getSelectionModel().removeListSelectionListener(this);
		sessionTable.getSelectionModel().clearSelection();
		DefaultTableModel sessionModel = (DefaultTableModel) sessionTable
				.getModel();
		sessionModel.setDataVector(data, header);

		sessionTable.getSelectionModel().addListSelectionListener(this);

	}

	public final void updatePendingTable(Vector<Vector<String>> data,
			Vector<String> header) {
		sessionTable.getSelectionModel().removeListSelectionListener(this);
		sessionTable.getSelectionModel().clearSelection();
		DefaultTableModel pendingModel = (DefaultTableModel) pendingTable
				.getModel();
		pendingModel.setDataVector(data, header);

		sessionTable.getSelectionModel().addListSelectionListener(this);
	}

	public final void updatePipelineTable(Vector<Vector<String>> data,
			Vector<String> header) {
		sessionTable.getSelectionModel().removeListSelectionListener(this);
		sessionTable.getSelectionModel().clearSelection();
		DefaultTableModel pipelineModel = (DefaultTableModel) pipelineTable
				.getModel();
		pipelineModel.setDataVector(data, header);

		sessionTable.getSelectionModel().addListSelectionListener(this);
	}

	public final void actionPerformed(ActionEvent ae) {

		if (sendBtn.equals((JButton) ae.getSource())) {

			if (selectedBankId != "") {
				switch (actionComboBox.getSelectedIndex()) {
				case 0:
					controller.requestRecovery(selectedBankId);
					break;

				case 1:
					controller.requestEndRecovery(selectedBankId);
					break;
				}
			}
		}

	}

	public final void valueChanged(ListSelectionEvent ae) {
		String newSelectedBankId;
		if (ae.getSource().equals(sessionTable.getSelectionModel())) {
			newSelectedBankId = (String) sessionTable.getValueAt(sessionTable.getSelectedRow(),0);

			if (selectedBankId.equals(newSelectedBankId)) {
				return;
			}

			selectedBankId = newSelectedBankId;
			bankIDLabel.setText(selectedBankId);
			sendBtn.setEnabled(true);
			controller.selectedSessionChange(selectedBankId);
		}

	}

	public final String getSelectedBankId() {
		return selectedBankId;
	}

}
