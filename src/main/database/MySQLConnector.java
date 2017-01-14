package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class set up the connects to a MySQL Sever, creates a DB and a table and
 * give the user access permissions provides an API to run query with or without
 * parameters, update the table and add events from a DataList to the DB
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
		runUpdate("CREATE database events");
		logger.log(Level.INFO, "DB events created successfully");
		runQuery("use events");
		runUpdate(
				"CREATE TABLE celebs_arrests (Name VARCHAR(30) NOT NULL,Arrest_Date DATE NOT NULL,Reason VARCHAR(150) NOT NULL,keywords VARCHAR(50) NOT NULL,PRIMARY KEY (Name,Arrest_Date,Reason))");
		runUpdate("CREATE TABLE keywords_table (Keyword VARCHAR(15))");
		logger.log(Level.INFO, "tables celebs_arrests created successfully");
	}

	private void givePermissions() throws SQLException {
		runQuery("grant all privileges on *.* to root@localhost;");
	}

	public static int runUpdate(final String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public static ResultSet runQuery(final String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public static ResultSet runSafeQuery(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			$.setObject(¢, inputs[¢ - 1]);
		return $.executeQuery();
	}

	public static ResultSet runSafeQuery(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeQuery();
	}

	public static int runSafeUpdate(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeUpdate();
	}

	private static java.sql.Date utilDateToSQLDateConvertor(final java.util.Date utilDate) {
		return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(utilDate));
	}

	public static void addEvents(final DataList lin) {
		for (final TableTuple tuple : lin)
			try (PreparedStatement ps = conn.prepareStatement("INSERT INTO celebs_arrests values (?,?,?,?);");) {
				ps.setString(1, tuple.getName());
				ps.setDate(2, utilDateToSQLDateConvertor(tuple.getRegularDate()));
				ps.setString(3, tuple.getReason());
				List<String> keywords = tuple.getKeyWords();
				String keywordsStr = "";
				for (String word : keywords)
					keywordsStr.concat(word + " ");
				ps.setString(4, keywordsStr);
				ps.executeUpdate();
			} catch (final SQLException ¢) {
				¢.printStackTrace();
			}
	}

	public static void addAllKeywords(DataList lin) throws SQLException {
		Set<String> keywords = new HashSet<>();
		for (final TableTuple ¢ : lin)
			keywords.addAll(¢.getKeyWords());
		for (String keyword : keywords)
			runSafeUpdate("INSERT INTO keywords_table(?)", keyword);
	}

	public static void clearTable() throws SQLException {
		runUpdate("DELETE FROM celebs_arrests");
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