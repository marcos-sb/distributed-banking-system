package es.udc.fic.acs.infmsb01.atm.atm.view;

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
import javax.swing.table.DefaultTableModel;

import es.udc.fic.acs.infmsb01.atm.atm.controller.ATMViewController;
import es.udc.fic.acs.infmsb01.atm.common.net.transport.TransportProcessorLinkState;

public final class ATMView extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ATMViewController observer;

	private JComboBox cb;
	private JButton btn;
	private JTextField creditCardTF, ammountTF, sourceTF, destinationTF;
	private JLabel sourceLabel;
	private JTable table;
	private JScrollPane scp;
	private JPanel panel;

	public ATMView(ATMViewController observer, String title) {
		this.observer = observer;
		paintGUI(title);
	}

	private void paintGUI(String title) {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();

		creditCardTF = new JTextField();
		creditCardTF.setPreferredSize(new Dimension(100, 25));

		JLabel creditCardLabel = new JLabel("Credit Card Number");
		creditCardLabel.setLabelFor(creditCardTF);
		panel.add(creditCardLabel);

		panel.add(creditCardTF);

		String[] actions = { "Cash Withdrawal", "Cash Deposit",
				"Check Balance", "Check Movements", "Accounts Transfer" };

		cb = new JComboBox(actions);
		cb.setSelectedIndex(0);

		panel.add(cb);

		btn = new JButton("Send");
		btn.addActionListener(this);
		panel.add(btn);

		ammountTF = new JTextField();
		ammountTF.setPreferredSize(new Dimension(80, 25));

		JLabel ammountLabel = new JLabel("Ammount");
		ammountLabel.setLabelFor(ammountTF);

		panel.add(ammountLabel);
		panel.add(ammountTF);

		sourceTF = new JTextField();
		sourceTF.setPreferredSize(new Dimension(80, 25));

		sourceLabel = new JLabel("Source Account");
		sourceLabel.setLabelFor(sourceTF);

		panel.add(sourceLabel);
		panel.add(sourceTF);

		destinationTF = new JTextField();
		destinationTF.setPreferredSize(new Dimension(80, 25));

		JLabel destinationLabel = new JLabel("Destination Account");
		destinationLabel.setLabelFor(destinationTF);

		panel.add(destinationLabel);
		panel.add(destinationTF);

		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(700, 70));
		table.setFillsViewportHeight(true);

		scp = new JScrollPane(table);

		panel.add(scp);

		String[] linkActions = { "Online", "Disable Reception",
				"Disable Transmission", "Offline" };

		final JComboBox controlLinkStateCB = new JComboBox(linkActions);
		controlLinkStateCB.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				switch (controlLinkStateCB.getSelectedIndex()) {
				case 0:
					observer.setTransportProcessorLinkState(TransportProcessorLinkState.ONLINE);
					break;

				case 1:
					observer.setTransportProcessorLinkState(TransportProcessorLinkState.DISABLEDRECEPTION);
					break;

				case 2:
					observer.setTransportProcessorLinkState(TransportProcessorLinkState.DISABLEDTRANSMISSION);
					break;

				case 3:
					observer.setTransportProcessorLinkState(TransportProcessorLinkState.OFFLINE);
				}

			}
		});
		
		panel.add(controlLinkStateCB);

		this.getContentPane().add(panel);
		this.setTitle("ACME ATM [" + title + "]");
		this.pack();
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent ae) {

		if (btn.equals((JButton) ae.getSource())) {

			try {
				switch (cb.getSelectedIndex()) {
				case 0:
					observer.requestWithdraw(getTextFromTF(creditCardTF),
							getTextFromTF(sourceTF), getTextFromTF(ammountTF));
					break;

				case 1:
					observer.requestDeposit(getTextFromTF(creditCardTF),
							getTextFromTF(destinationTF),
							getTextFromTF(ammountTF));
					break;

				case 2:
					observer.requestCheckBalance(getTextFromTF(creditCardTF),
							getTextFromTF(sourceTF));
					break;

				case 3:
					observer.requestAccountMovements(
							getTextFromTF(creditCardTF),
							getTextFromTF(sourceTF));
					break;

				case 4:
					observer.requestAccountsTransfer(
							getTextFromTF(creditCardTF),
							getTextFromTF(sourceTF),
							getTextFromTF(destinationTF),
							getTextFromTF(ammountTF));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private String getTextFromTF(JTextField tf) {
		String trimmedText = tf.getText().trim().toLowerCase();

		return trimmedText.equals("") ? "-1" : trimmedText;
	}

	public void paintResult(Vector<Vector<String>> data, Vector<String> header) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setDataVector(data, header);
	}

}
