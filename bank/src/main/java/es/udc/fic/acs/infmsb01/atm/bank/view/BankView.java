package es.udc.fic.acs.infmsb01.atm.bank.view;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import es.udc.fic.acs.infmsb01.atm.bank.controller.BankViewController;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class BankView extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BankViewController controller;

	private JButton dbActionBtn, sendBtn;
	private JComboBox dbActionCB, sendActionCombo;
	private JScrollPane sessionScp, pipelineScp, dbResultScp;
	private JTable sessionTable, pipelineTable, dbResultTable;
	private JPanel panel, panel2;
	private JLabel numberOfChannelsLbl;
	private JTextField numberOfChannelsTF, creditCardTF, accountNumberTF;

	public BankView(BankViewController controller, String title) {
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

		sessionScp = new JScrollPane(sessionTable);

		panel.add(sessionScp);

		panel2 = new JPanel();

		numberOfChannelsTF = new JTextField();
		numberOfChannelsTF.setPreferredSize(new Dimension(80, 25));
		numberOfChannelsLbl = new JLabel("# Channels");
		numberOfChannelsLbl.setLabelFor(numberOfChannelsTF);

		panel2.add(numberOfChannelsLbl);
		panel2.add(numberOfChannelsTF);

		String[] sendActions = { "Open Session", "Stop Traffic",
				"Restart Traffic", "Close Session" };

		sendActionCombo = new JComboBox(sendActions);
		sendActionCombo.setSelectedIndex(0);

		panel2.add(sendActionCombo);

		sendBtn = new JButton("Send");
		sendBtn.addActionListener(this);

		panel2.add(sendBtn);

		panel.add(panel2);

		pipelineTable = new JTable();
		pipelineTable
				.setPreferredScrollableViewportSize(new Dimension(700, 70));
		pipelineTable.setFillsViewportHeight(true);

		pipelineScp = new JScrollPane(pipelineTable);

		panel.add(pipelineScp);

		creditCardTF = new JTextField();
		creditCardTF.setPreferredSize(new Dimension(100, 25));

		JLabel creditCardLabel = new JLabel("CreditCard Number");
		creditCardLabel.setLabelFor(creditCardTF);
		panel.add(creditCardLabel);

		panel.add(creditCardTF);

		String[] actions = { "All Accounts w/ C.C.", "Check Movements", "C.C. Account List" };

		dbActionCB = new JComboBox(actions);
		dbActionCB.setSelectedIndex(0);

		panel.add(dbActionCB);

		accountNumberTF = new JTextField();
		accountNumberTF.setPreferredSize(new Dimension(100, 25));

		JLabel accountNumberLabel = new JLabel("Account #");
		accountNumberLabel.setLabelFor(accountNumberTF);

		panel.add(accountNumberLabel);
		panel.add(accountNumberTF);

		dbActionBtn = new JButton("Send");
		dbActionBtn.addActionListener(this);
		panel.add(dbActionBtn);

		dbResultTable = new JTable();
		dbResultTable
				.setPreferredScrollableViewportSize(new Dimension(700, 70));
		dbResultTable.setFillsViewportHeight(true);

		dbResultScp = new JScrollPane(dbResultTable);

		panel.add(dbResultScp);
		
		String[] linkActions = { "Online", "Disable Reception",
				"Disable Transmission", "Offline" };

		final JComboBox controlLinkStateCB = new JComboBox(linkActions);
		controlLinkStateCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				switch (controlLinkStateCB.getSelectedIndex()) {
				case 0:
					controller.setTransportProcessorLinkState(TransportProcessorLinkState.ONLINE);
					break;

				case 1:
					controller.setTransportProcessorLinkState(TransportProcessorLinkState.DISABLEDRECEPTION);
					break;

				case 2:
					controller.setTransportProcessorLinkState(TransportProcessorLinkState.DISABLEDTRANSMISSION);
					break;

				case 3:
					controller.setTransportProcessorLinkState(TransportProcessorLinkState.OFFLINE);
				}

			}
		});
		
		panel.add(controlLinkStateCB);

		this.getContentPane().add(panel);
		this.setTitle("ACME Bank [" + title + "]");
		this.pack();
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {

		JButton actionSource = (JButton) ae.getSource();

		try {

			if (sendBtn.equals(actionSource)) {

				switch (sendActionCombo.getSelectedIndex()) {
				case 0:
					controller
							.requestOpenSession(getTextFromTF(numberOfChannelsTF));
					break;

				case 1:
					controller.requestStopTraffic();
					break;

				case 2:
					controller.requestRestartTraffic();
					break;

				case 3:
					controller.requestCloseSession();
				}

			} else if (dbActionBtn.equals(actionSource)) {

				switch (dbActionCB.getSelectedIndex()) {
				case 0:
					controller.getAllAccounts();
					break;

				case 1:
					controller.getAccountMovements(getTextFromTF(creditCardTF),
							getTextFromTF(accountNumberTF));
					break;

				case 2:
					controller
							.getCreditcardAccounts(getTextFromTF(creditCardTF));
					break;

				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	private String getTextFromTF(JTextField tf) {
		String trimmedText = tf.getText().trim().toLowerCase();

		return trimmedText.equals("") ? "-1" : trimmedText;
	}

	public void updateSessionTable(Vector<Vector<String>> sessionTableData,
			Vector<String> sessionColNames) {
		DefaultTableModel sessionModel = (DefaultTableModel) sessionTable
				.getModel();
		sessionModel.setDataVector(sessionTableData, sessionColNames);
	}

	public void updatePipelineTable(Vector<Vector<String>> pipelineTableData,
			Vector<String> pipelineColNames) {
		DefaultTableModel pipelineModel = (DefaultTableModel) pipelineTable
				.getModel();
		pipelineModel.setDataVector(pipelineTableData, pipelineColNames);
	}

	public void updateDBQueryTable(Vector<Vector<String>> data,
			Vector<String> header) {
		DefaultTableModel model = (DefaultTableModel) dbResultTable.getModel();
		model.setDataVector(data, header);
	}

}
