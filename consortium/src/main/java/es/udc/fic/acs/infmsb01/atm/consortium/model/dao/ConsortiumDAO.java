package es.udc.fic.acs.infmsb01.atm.consortium.model.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.fic.acs.infmsb01.atm.consortium.model.session.ConsortiumSession;
import es.udc.fic.acs.infmsb01.atm.consortium.model.session.PendingDataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.BankInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.ConsortiumInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.agentinfo.RecipientInfo;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.CommonDAO;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateName;
import es.udc.fic.acs.infmsb01.atm.common.model.dao.to.StateTO;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.BusyChannelException;
import es.udc.fic.acs.infmsb01.atm.common.model.exception.SessionOpenedException;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataRequestMessage;
import es.udc.fic.acs.infmsb01.atm.common.model.message.DataResponseMessage;

public final class ConsortiumDAO extends CommonDAO {

	public final List<BankInfo> queryBanksInfo(Connection connection)
			throws SQLException {

		PreparedStatement preparedStatement;
		ResultSet resultSet;
		ArrayList<BankInfo> bis = new ArrayList<BankInfo>();

		String queryString = " SELECT bankid, bankip, bankport"
				+ " FROM session";

		preparedStatement = connection.prepareStatement(queryString);

		int i;

		resultSet = preparedStatement.executeQuery();

		while (resultSet.next()) {

			i = 1;
			String bankId = resultSet.getString(i++);
			String bankIp = resultSet.getString(i++);
			Integer bankPort = resultSet.getInt(i++);

			bis.add(new BankInfo(bankId, bankIp, bankPort));
		}

		return bis;

	}

	public final List<ConsortiumSession> querySessions(Connection connection)
			throws SQLException, IOException, ClassNotFoundException,
			BusyChannelException {

		PreparedStatement preparedStatement;
		ResultSet sessionResultSet, resultSet;
		ArrayList<ConsortiumSession> sessions = new ArrayList<ConsortiumSession>();

		String queryString = " SELECT consortiumid,"
				+ " 		 bankid, sumwithdrawals,"
				+ "        sumdeposits, sumtransfers, numberofchannels, state"
				+ " FROM session";

		preparedStatement = connection.prepareStatement(queryString);

		int i;

		sessionResultSet = preparedStatement.executeQuery();

		while (sessionResultSet.next()) {

			i = 1;
			String consortiumId = sessionResultSet.getString(i++);
			String bankId = sessionResultSet.getString(i++);
			long sumWithdrawals = sessionResultSet.getLong(i++);
			long sumDeposits = sessionResultSet.getLong(i++);
			long sumTransfers = sessionResultSet.getLong(i++);
			byte numberOfChannels = sessionResultSet.getByte(i++);
			byte stateCode = sessionResultSet.getByte(i);

			ConsortiumSession cs = new ConsortiumSession(new RecipientInfo(
					consortiumId), new RecipientInfo(bankId), numberOfChannels);
			cs.setSumDeposits(sumDeposits);
			cs.setSumTransfers(sumTransfers);
			cs.setSumWithdrawals(sumWithdrawals);
			cs.setContextState(new StateTO(StateName.getStateName(stateCode)));

			String queryPendingMessages = " SELECT messageobjectpayload, pendingnumber"
					+ " FROM pendingmessage"
					+ " WHERE bankid = ?"
					+ " ORDER BY receivedtime ASC";

			preparedStatement = connection
					.prepareStatement(queryPendingMessages);
			preparedStatement.setString(1, bankId);

			resultSet = preparedStatement.executeQuery();

			byte[] messageAsBytes;
			while (resultSet.next()) {

				i = 1;
				messageAsBytes = resultSet.getBytes(i++);
				short pendingNumber = resultSet.getShort(i);

				ByteArrayInputStream baip = new ByteArrayInputStream(
						messageAsBytes);
				ObjectInputStream ois = new ObjectInputStream(baip);

				PendingDataRequestMessage msg = new PendingDataRequestMessage(
						(DataRequestMessage) ois.readObject(), pendingNumber);

				cs.appendPendingMessage(msg);
			}

			String queryRequestMessages = " SELECT messageobjectpayload"
					+ " FROM channelmessagerequest c" + " WHERE bankid = ?"
					+ " GROUP BY channelnumber, requestnumber"
					+ " HAVING requestnumber = "
					+ " (SELECT MAX(requestnumber)"
					+ "  FROM channelmessagerequest"
					+ "  WHERE channelnumber = c.channelnumber)";

			preparedStatement = connection
					.prepareStatement(queryRequestMessages);
			preparedStatement.setString(1, bankId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				messageAsBytes = resultSet.getBytes(1);
				ByteArrayInputStream baip = new ByteArrayInputStream(
						messageAsBytes);
				ObjectInputStream ois = new ObjectInputStream(baip);

				DataRequestMessage drm = (DataRequestMessage) ois.readObject();

				cs.appendMessageFromDAO(drm);
			}

			String queryResponseMessages = " SELECT messageobjectpayload"
					+ " FROM channelmessageresponse c" + " WHERE bankid = ?"
					+ " GROUP BY channelnumber, responsenumber"
					+ " HAVING responsenumber = "
					+ " (SELECT MAX(responsenumber)"
					+ "  FROM channelmessageresponse"
					+ "  WHERE channelnumber = c.channelnumber)";

			preparedStatement = connection
					.prepareStatement(queryResponseMessages);
			preparedStatement.setString(1, bankId);
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				messageAsBytes = resultSet.getBytes(1);
				ByteArrayInputStream baip = new ByteArrayInputStream(
						messageAsBytes);
				ObjectInputStream ois = new ObjectInputStream(baip);

				DataResponseMessage drm = (DataResponseMessage) ois
						.readObject();

				cs.appendMessageFromDAO(drm);
			}

			sessions.add(cs);
		}

		return sessions;

	}

	public final void closeSession(ConsortiumSession session,
			Connection connection) throws SQLException {

		PreparedStatement ps;

		String queryString = " DELETE FROM channelmessagerequest"
				+ " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getBank().getId());

		ps.executeUpdate();

		queryString = " DELETE FROM channelmessageresponse"
				+ " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getBank().getId());

		ps.executeUpdate();

		queryString = " DELETE FROM session" + " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getBank().getId());

		ps.executeUpdate();

	}

	public final void createSession(ConsortiumSession session,
			ConsortiumInfo ci, BankInfo bi,
			Connection connection) throws SQLException, SessionOpenedException {
		PreparedStatement ps;

		String queryString = " UPDATE session"
				+ " SET bankip = ?, bankport = ?, sumwithdrawals = ?,"
				+ " sumdeposits = ?, sumtransfers = ?, numberofchannels = ?, state = ?" + " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setString(i++, bi.getIp());
		ps.setInt(i++, bi.getPort());
		ps.setLong(i++, session.getSumWithdrawals());
		ps.setLong(i++, session.getSumDeposits());
		ps.setLong(i++, session.getSumTransfers());
		ps.setByte(i++, session.getNumberOfChannels());
		ps.setByte(i++, session.getContextState().getStateName().getCode());
		ps.setString(i, bi.getId());

		int updatedRows = ps.executeUpdate();

		if (updatedRows > 0) {
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
		ps.setInt(i++, ci.getBankPort());
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

	public final void createPartialSession(ConsortiumSession session,
			ConsortiumInfo ci, Connection connection) throws SQLException {
		PreparedStatement ps;
		ResultSet rs;

		String queryString = " SELECT bankid" + " FROM session"
				+ " WHERE bankid = ?";

		ps = connection.prepareStatement(queryString);
		ps.setString(1, session.getBank().getId());

		rs = ps.executeQuery();

		if (!rs.next()) { // first atm request

			queryString = " INSERT INTO session (consortiumid, consortiumip,"
					+ " consortiumport, bankid, bankip, bankport, sumwithdrawals,"
					+ " sumdeposits, sumtransfers, numberofchannels, state)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

			ps = connection.prepareStatement(queryString);

			int i = 1;
			ps.setString(i++, session.getConsortium().getId());
			ps.setString(i++, ci.getIp());
			ps.setInt(i++, ci.getBankPort());
			ps.setString(i++, session.getBank().getId());
			ps.setNull(i++, java.sql.Types.VARCHAR);
			ps.setNull(i++, java.sql.Types.NUMERIC);
			ps.setLong(i++, session.getSumWithdrawals());
			ps.setLong(i++, session.getSumDeposits());
			ps.setLong(i++, session.getSumTransfers());
			ps.setByte(i++, session.getNumberOfChannels());
			ps.setByte(i, session.getContextState().getStateName().getCode());

			ps.executeUpdate();
		}

	}

	public final void insertPendingMessage(String bankID,
			PendingDataRequestMessage message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement ps;

		String queryString = " INSERT INTO pendingmessage"
				+ " (bankid, pendingnumber, receivedtime, messageobjectpayload)"
				+ " VALUES (?,?,?,?)";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setString(i++, bankID);
		ps.setShort(i++, message.getPendingNumber());
		java.sql.Timestamp date = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		ps.setTimestamp(i++, date);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(message.getDataRequestMessage());
		byte[] messageAsBytes = baos.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(messageAsBytes);

		ps.setBinaryStream(i++, bais, messageAsBytes.length);

		ps.executeUpdate();

	}

	public final void removePendingMessage(String bankID, short pendingNumber,
			Connection connection) throws IOException, SQLException {

		PreparedStatement ps;

		String queryString = " DELETE FROM pendingmessage"
				+ " WHERE bankid = ?" + " AND pendingnumber = ?";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setString(i++, bankID);
		ps.setShort(i, pendingNumber);

		ps.executeUpdate();

	}

	public final void updateSessionState(String bankID, StateTO state,
			Connection connection) throws SQLException {
		PreparedStatement ps;

		String queryString = " UPDATE session" + " SET state = ?"
				+ " WHERE bankId = ?";

		ps = connection.prepareStatement(queryString);

		int i = 1;
		ps.setByte(i++, state.getStateName().getCode());
		ps.setString(i, bankID);

		ps.executeUpdate();

	}

	public final void insertDataRequestMessage(String bankId,
			DataRequestMessage message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement preparedStatement;

		String queryString = " INSERT INTO channelmessagerequest"
				+ " (bankid, channelnumber, requestnumber, messageobjectpayload)"
				+ " VALUES (?,?,?,?)";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setString(i++, bankId);
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

	public final void insertDataResponseMessage(String bankId,
			DataResponseMessage message, Connection connection)
			throws SQLException, IOException {

		PreparedStatement preparedStatement;

		String queryString = " INSERT INTO channelmessageresponse"
				+ " (bankid, channelnumber, responsenumber, messageobjectpayload)"
				+ " VALUES (?,?,?,?)";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setString(i++, bankId);
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

	public final void updateSessionTotals(String bankID, long sumDeposits,
			long sumTransfers, long sumWithdrawals, Connection connection)
			throws SQLException {

		PreparedStatement preparedStatement;

		String queryString = " UPDATE session"
				+ " SET sumdeposits = ?, sumtransfers = ?,"
				+ " 	  sumwithdrawals = ?" + " WHERE bankID = ?";

		preparedStatement = connection.prepareStatement(queryString);

		int i = 1;
		preparedStatement.setLong(i++, sumDeposits);
		preparedStatement.setLong(i++, sumTransfers);
		preparedStatement.setLong(i++, sumWithdrawals);
		preparedStatement.setString(i++, bankID);

		preparedStatement.executeUpdate();

	}

}
