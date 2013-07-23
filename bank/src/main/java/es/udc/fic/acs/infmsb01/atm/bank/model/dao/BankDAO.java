package es.udc.fic.acs.infmsb01.atm.bank.model.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.management.InstanceNotFoundException;

import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.AccountMovementsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.BankAccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.Card2AccountTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardAccountsCTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.dao.to.CreditCardTO;
import es.udc.fic.acs.infmsb01.atm.bank.model.session.BankSession;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.CommonDAO;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateTO;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadCreditCardException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadDestinationAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BadSourceAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.NoFundsInSourceAccountException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.Message;

public final class BankDAO extends CommonDAO {

	private static final int MAX_NUMBER_OF_MOVEMENTS = 20;
	
	public final List<Card2AccountTO> queryAllAccounts(Connection connection)
			throws SQLException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;
		ArrayList<Card2AccountTO> cards2accounts;

		String queryString = "SELECT creditcardnumber, associatedaccountnumber, realaccountnumber"
				+ " FROM card2account" + " ORDER BY creditcardnumber ASC;";

		preparedStatement = connection.prepareStatement(queryString);

		resultSet = preparedStatement.executeQuery();

		int i;
		cards2accounts = new ArrayList<Card2AccountTO>();

		while (resultSet.next()) {
			i = 1;

			String creditCardNumber = resultSet.getString(i++);
			byte associatedAccountNumber = resultSet.getByte(i++);
			String realAccountNumber = resultSet.getString(i);

			cards2accounts.add(new Card2AccountTO(creditCardNumber,
					associatedAccountNumber, realAccountNumber));
		}

		return cards2accounts;

	}

	public final void closeSession(BankSession session, Connection connection)
			throws SQLException {

		PreparedStatement ps;

		String queryString = " DELETE FROM channelmessagerequest"
				+ " WHERE consortiumid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getConsortium().getId());

		ps.executeUpdate();

		queryString = " DELETE FROM channelmessageresponse"
				+ " WHERE consortiumid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getConsortium().getId());

		ps.executeUpdate();

		queryString = " DELETE FROM session" + " WHERE consortiumid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getConsortium().getId());

		ps.executeUpdate();

	}

	public final CreditCardAccountsCTO queryCreditCardAccounts(
			CreditCardTO creditCard, Connection connection)
			throws SQLException, BadCreditCardException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;
		CreditCardAccountsCTO associatedAccounts;

		String queryCard = " SELECT number" + "	 FROM creditcard"
				+ " WHERE number = ?";

		preparedStatement = connection.prepareStatement(queryCard);

		int i = 1;
		preparedStatement.setString(i++, creditCard.getCardNumber());

		resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) { // no credit card with input number
			throw new BadCreditCardException(creditCard.toString());
		}

		String queryString = " SELECT number, balance" + " FROM bankaccount"
				+ " WHERE number IN" + "	(SELECT realaccountnumber"
				+ "	 FROM card2account" + "	 WHERE creditcardnumber = ?)";

		preparedStatement = connection.prepareStatement(queryString);

		i = 1;
		preparedStatement.setString(i, creditCard.getCardNumber());

		resultSet = preparedStatement.executeQuery();

		associatedAccounts = new CreditCardAccountsCTO(creditCard);
		while (resultSet.next()) {
			i = 1;

			String accountNumber = resultSet.getString(i++);
			double balance = resultSet.getDouble(i);

			associatedAccounts.addAccount(new BankAccountTO(accountNumber,
					balance));
		}

		return associatedAccounts;

	}

	public final BankAccountTO queryCreditCardAccount(CreditCardTO creditCard,
			byte associatedAccountNumber, Connection connection)
			throws SQLException, BadCreditCardException, BadAccountException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;

		String queryCard = " SELECT number" + "	 FROM creditcard"
				+ " WHERE number = ?";

		preparedStatement = connection.prepareStatement(queryCard);

		int i = 1;
		preparedStatement.setString(i++, creditCard.getCardNumber());

		resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) { // no credit card with input number
			throw new BadCreditCardException(creditCard.toString());
		}

		String queryAccount = " SELECT number, balance" + " FROM bankaccount"
				+ " WHERE number = (SELECT realaccountnumber"
				+ "				  FROM card2account"
				+ "				  WHERE creditcardnumber = ?"
				+ "				  AND associatedaccountnumber = ?)";

		preparedStatement = connection.prepareStatement(queryAccount);

		i = 1;
		preparedStatement.setString(i++, creditCard.getCardNumber());
		preparedStatement.setByte(i, associatedAccountNumber);

		resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) {
			throw new BadAccountException(creditCard.toString());
		}

		i = 1;
		String accountNumber = resultSet.getString(i++);
		double balance = resultSet.getDouble(i);

		return new BankAccountTO(accountNumber, balance);

	}

	public final AccountMovementsCTO queryAccountMovements(
			CreditCardTO creditCard, byte associatedAccountNumber,
			Connection connection) throws SQLException,
			InstanceNotFoundException, BadCreditCardException,
			BadAccountException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;
		AccountMovementsCTO accountMovements;

		BankAccountTO account = queryCreditCardAccount(creditCard,
				associatedAccountNumber, connection);

		String queryString = " SELECT movementtype, ammount, date"
				+ " FROM movement" + " WHERE accountnumber = ? ORDER BY date ASC";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1, processedResults = 0;
		preparedStatement.setString(i, account.getAccountNumber());

		resultSet = preparedStatement.executeQuery();

		accountMovements = new AccountMovementsCTO(account);

		while (resultSet.next()) {
			i = 1;

			byte movementType = resultSet.getByte(i++);
			float ammount = resultSet.getFloat(i++);
			Calendar date = Calendar.getInstance();
			date.setTimeInMillis(resultSet.getTimestamp(i++).getTime());

			accountMovements.addMovement(new AccountMovementTO(movementType,
					ammount, date));
			if(processedResults++ >= MAX_NUMBER_OF_MOVEMENTS) {
				break;
			}
		}

		return accountMovements;

	}

	public final BankSession querySession(Connection connection)
			throws SQLException, IOException, ClassNotFoundException, BusyChannelException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;

		String queryString = " SELECT consortiumid,"
				+ " 		 bankid, sumwithdrawals,"
				+ "        sumdeposits, sumtransfers, numberofchannels, state"
				+ " FROM session";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;

		resultSet = preparedStatement.executeQuery();

		if (!resultSet.next()) {
			return null;
		}

		i = 1;
		String consortiumId = resultSet.getString(i++);
		String bankId = resultSet.getString(i++);
		long sumWithdrawals = resultSet.getLong(i++);
		long sumDeposits = resultSet.getLong(i++);
		long sumTransfers = resultSet.getLong(i++);
		byte numberOfChannels = resultSet.getByte(i++);
		byte stateCode = resultSet.getByte(i);

		RecipientInfo consortiumID = new RecipientInfo(consortiumId);
		RecipientInfo bankID = new RecipientInfo(bankId);

		BankSession bs = new BankSession(consortiumID, bankID, numberOfChannels);
		bs.setSumDeposits(sumDeposits);
		bs.setSumTransfers(sumTransfers);
		bs.setSumWithdrawals(sumWithdrawals);
		bs.setContextState(new StateTO(StateName.getStateName(stateCode)));

		String queryRequestMessages = " SELECT messageobjectpayload"
				+ " FROM channelmessagerequest c" + " WHERE consortiumid = ?"
				+ " GROUP BY channelnumber, requestnumber"
				+ " HAVING requestnumber = " + " (SELECT MAX(requestnumber)"
				+ "  FROM channelmessagerequest"
				+ "  WHERE channelnumber = c.channelnumber)";

		preparedStatement = connection.prepareStatement(queryRequestMessages);

		i = 1;
		preparedStatement.setString(i++, consortiumId);

		resultSet = preparedStatement.executeQuery();

		byte[] messageAsBytes;
		while (resultSet.next()) {
			messageAsBytes = resultSet.getBytes(1);
			ByteArrayInputStream baip = new ByteArrayInputStream(messageAsBytes);
			ObjectInputStream ois = new ObjectInputStream(baip);

			DataRequestMessage drm = (DataRequestMessage) ois.readObject();

			bs.appendMessageFromDAO(drm);
		}

		String queryResponseMessages = " SELECT messageobjectpayload"
				+ " FROM channelmessageresponse c" + " WHERE consortiumid = ?"
				+ " GROUP BY channelnumber, responsenumber"
				+ " HAVING responsenumber = " + " (SELECT MAX(responsenumber)"
				+ "  FROM channelmessageresponse"
				+ "  WHERE channelnumber = c.channelnumber)";

		preparedStatement = connection.prepareStatement(queryResponseMessages);

		i = 1;
		preparedStatement.setString(i++, consortiumId);

		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {
			messageAsBytes = resultSet.getBytes(1);
			ByteArrayInputStream baip = new ByteArrayInputStream(messageAsBytes);
			ObjectInputStream ois = new ObjectInputStream(baip);

			DataResponseMessage drm = (DataResponseMessage) ois.readObject();

			bs.appendMessageFromDAO(drm);
		}

		return bs;

	}

	public final void createSession(BankSession session, ConsortiumInfo ci,
			BankInfo bi, Connection connection) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;

		String queryString = " SELECT bankid" + " FROM session"
				+ " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);
		int i = 1;
		ps.setString(i++, session.getBank().getId());

		rs = ps.executeQuery();

		if (rs.next()) {
			return;
		}

		queryString = " INSERT INTO session (consortiumid, consortiumip,"
				+ " consortiumport, bankid, bankip, bankport, sumwithdrawals,"
				+ " sumdeposits, sumtransfers, numberofchannels, state)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		ps = connection.prepareStatement(queryString);

		i = 1;
		ps.setString(i++, session.getConsortium().getId());
		ps.setString(i++, ci.getIp());
		ps.setInt(i++, ci.getPort());
		ps.setString(i++, session.getBank().getId());
		ps.setString(i++, bi.getIp());
		ps.setInt(i++, bi.getPort());
		ps.setLong(i++, session.getSumWithdrawals());
		ps.setLong(i++, session.getSumDeposits());
		ps.setLong(i++, session.getSumTransfers());
		ps.setByte(i++, session.getNumberOfChannels());
		ps.setByte(i, session.getContextState().getStateName().getCode());

		ps.executeUpdate();

	}

	public final void insertLogMessage(Message message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement preparedStatement;

		String queryString = " INSERT INTO logmessage(messageobjectpayload)"
				+ " VALUES (?)";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		byte[] messageAsBytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(messageAsBytes);

		preparedStatement.setBinaryStream(i++, bais, messageAsBytes.length);

		preparedStatement.executeUpdate();

	}

	public final void updateSessionState(String consortiumID, StateTO state,
			Connection connection) throws SQLException {
		PreparedStatement ps;

		String queryString = " UPDATE session" + " SET state = ?"
				+ " WHERE consortiumId = ?";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setByte(i++, state.getStateName().getCode());
		ps.setString(i, consortiumID);

		ps.executeUpdate();

	}

	public final void updateSessionTotals(String consortiumID,
			long sumDeposits, long sumTransfers, long sumWithdrawals,
			Connection connection) throws SQLException {

		PreparedStatement preparedStatement;

		String queryString = " UPDATE session"
				+ " SET sumdeposits = ?, sumtransfers = ?,"
				+ " 	  sumwithdrawals = ?" + " WHERE consortiumId = ?";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setLong(i++, sumDeposits);
		preparedStatement.setLong(i++, sumTransfers);
		preparedStatement.setLong(i++, sumWithdrawals);
		preparedStatement.setString(i++, consortiumID);

		preparedStatement.executeUpdate();

	}

	public final void insertDataRequestMessage(String consortiumId,
			DataRequestMessage message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement preparedStatement;

		String queryString = " INSERT INTO channelmessagerequest"
				+ " (consortiumid, channelnumber, requestnumber, messageobjectpayload)"
				+ " VALUES (?,?,?,?)";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setString(i++, consortiumId);
		preparedStatement.setByte(i++, message.getChannelNumber());
		preparedStatement.setShort(i++, message.getMessageNumber());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		byte[] messageAsBytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(messageAsBytes);

		preparedStatement.setBinaryStream(i++, bais, messageAsBytes.length);

		preparedStatement.executeUpdate();

	}

	public final void insertDataResponseMessage(String consortiumId,
			DataResponseMessage message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement preparedStatement;

		String queryString = " INSERT INTO channelmessageresponse"
				+ " (consortiumid, channelnumber, responsenumber, messageobjectpayload)"
				+ " VALUES (?,?,?,?)";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setString(i++, consortiumId);
		preparedStatement.setByte(i++, message.getChannelNumber());
		preparedStatement.setShort(i++, message.getMessageNumber());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message);
		byte[] messageAsBytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(messageAsBytes);

		preparedStatement.setBinaryStream(i++, bais, messageAsBytes.length);

		preparedStatement.executeUpdate();

	}

	public final List<BankAccountTO> accountsTransfer(CreditCardTO creditCard,
			byte sourceAccountNumber, byte destinationAccountNumber,
			short ammount, Connection connection) throws SQLException,
			BadCreditCardException, BadSourceAccountException,
			BadDestinationAccountException, NoFundsInSourceAccountException {

		BankAccountTO source;
		BankAccountTO destination;
		ArrayList<BankAccountTO> accounts;

		try {
			source = queryCreditCardAccount(creditCard, sourceAccountNumber,
					connection);
		} catch (BadAccountException e) {
			throw new BadSourceAccountException(e.getMessage());
		}

		try {
			destination = queryCreditCardAccount(creditCard,
					destinationAccountNumber, connection);
		} catch (BadAccountException e) {
			throw new BadDestinationAccountException(e.getMessage());
		}

		source.setBalance(source.getBalance() - ammount);
		if (source.getBalance() < 0) {
			throw new NoFundsInSourceAccountException(creditCard.toString());
		}
		destination.setBalance(destination.getBalance() + ammount);

		updateAccount(source, connection);
		updateAccount(destination, connection);

		accounts = new ArrayList<BankAccountTO>();
		accounts.add(source);
		accounts.add(destination);

		return accounts;

	}

	public final void insertAccountMovements(AccountMovementsCTO movs,
			Connection connection) throws SQLException {

		PreparedStatement ps;

		String queryString;
		String accountNumber = movs.getAccount().getAccountNumber();
		ArrayList<AccountMovementTO> movsArr = movs.getMovements();

		for (AccountMovementTO am : movsArr) {
			queryString = " INSERT INTO movement (movementtype, ammount, date, accountnumber)"
					+ " VALUES (?,?,?,?)";

			ps = connection.prepareStatement(queryString);

			int i = 1;
			ps.setByte(i++, am.getType());
			ps.setFloat(i++, am.getAmmount());
			java.sql.Timestamp date = new java.sql.Timestamp(am.getDate()
					.getTimeInMillis());
			ps.setTimestamp(i++, date);
			ps.setString(i, accountNumber);

			ps.executeUpdate();
		}

	}

	public final void updateAccount(BankAccountTO account, Connection connection)
			throws SQLException {

		PreparedStatement ps;

		String queryString = "UPDATE bankaccount" + " SET balance = ?"
				+ " WHERE number = ?";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setDouble(i++, account.getBalance());
		ps.setString(i, account.getAccountNumber());

		ps.executeUpdate();

	}
}
