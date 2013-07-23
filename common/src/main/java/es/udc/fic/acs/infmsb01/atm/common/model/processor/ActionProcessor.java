package es.udc.fic.acs.infmsb01.atm.common.model.processor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.Action;
import es.udc.fic.acs.infmsb01.atm.common.model.processor.action.PersistenceAction;

public abstract class ActionProcessor {

	private static final Object processTransactional(Connection connection,
			PersistenceAction action) throws Exception {

		boolean rollback = false;

		try {
			connection
					.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);

			Object result = action.execute(connection);

			return result;

		} catch (SQLException e) {
			rollback = true;
			throw e;
		} catch (Exception e) {
			rollback = true;
			throw e;
		} finally {
			if (connection != null) {
				if (rollback) {
					connection.rollback();
				} else {
					connection.commit();
				}
				connection.close();
			}

		}

	}

	private static final Object processNonTransactional(Connection connection,
			PersistenceAction action) throws Exception {

		try {
			
			Object result = action.execute(connection);
			
			return result;
			
		} catch(Exception e) {
			throw e;
		} finally {
			connection.close();
		}
	}

	public static final Object process(Connection connection, PersistenceAction action) throws Exception {
		return action.isTransactional() ? processTransactional(connection,
				action) : processNonTransactional(connection, action);
	}

	public static final Object process(Action action) throws IOException,
			InstantiationException, IllegalAccessException, ParseException {

		return action.execute();

	}

}
