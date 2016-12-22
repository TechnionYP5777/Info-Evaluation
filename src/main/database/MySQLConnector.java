package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * set up the connection to MySQL DB and provide a function to run a query
 *
 * @author osherh
 * @since 6/12/2016
 */
public class MySQLConnector {
	private static final Logger logger = Logger.getLogger("MySQLConnector".getClass().getName());
	private static Connection conn;
	private boolean dbExists;

	public MySQLConnector() throws Exception {
		try {
			connectToServer();
			logger.log(Level.INFO, "connected to server");
			runQuery("use sys");
		} catch (Exception ¢) {
			throw ¢;
		}
		try (ResultSet r = runQuery("SHOW DATABASES;");) {
			while (r.next())
				if ("events".equals(r.getString(1)))
					dbExists = true;
			if (!dbExists) {
				givePermissions();
				createDatabase();
				logger.log(Level.INFO, "database created succesfully");

			}
			runQuery("use events;");
		} catch (final SQLException ¢) {
			throw ¢;
		}
	}

	private void connectToServer() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ¢) {
			throw ¢;
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "mysqlpass");
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	private void createDatabase() throws SQLException {
		updateDB("CREATE database events");
		logger.log(Level.INFO, "DB events created successfully");
		runQuery("use events");		
		updateDB("CREATE TABLE celebs_arrests (Name VARCHAR(30) NOT NULL,Arrest_Date DATE NOT NULL,Reason VARCHAR(150) NOT NULL,PRIMARY KEY (Name,Arrest_Date,Reason))");
		logger.log(Level.INFO, "table celebs_arrests created successfully");
	}

	private void givePermissions() throws SQLException {
		runQuery("grant all privileges on *.* to root@localhost;");
	}

	public static int updateDB(final String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public static ResultSet runQuery(final String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public static ResultSet runSafeQuery(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			$.setObject(¢, inputs[¢]);
		return $.executeQuery();
	}

	public static ResultSet runSafeQuery(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeQuery();
	}

	private static java.sql.Date utilDateToSQLDateConvertor(final java.util.Date utilDate) {
		return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(utilDate));
	}

	public static void addEvents(final DataList lin) {
		for (final TableTuple tuple : lin)
			try (PreparedStatement ps = conn.prepareStatement("INSERT INTO celebs_arrests values (?,?,?);");) {
				ps.setString(1, tuple.getName());
				ps.setDate(2, utilDateToSQLDateConvertor(tuple.getRegularDate()));
				ps.setString(3, tuple.getReason());
				ps.executeUpdate();
			} catch (final SQLException ¢) {
				¢.printStackTrace();
			}
	}

	public static void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (final SQLException ¢) {
			¢.printStackTrace();
		}
	}
}