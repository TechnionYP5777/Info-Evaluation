package infoeval.main.WikiData;

import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 *
 */
public class Connector {
	private static final Logger logger = Logger.getLogger("Connector".getClass().getName());
	private String username;
	private String password;
	private String driver;
	private String server;
	private String host;
	private String db;
	private Connection conn;

	public Connector() throws Exception {
		try {
			conn = getConnection();
			logger.log(Level.INFO, "connected to server");
		} catch (Exception e) {
			throw e;
		}
	}

	public Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
		Properties props = new Properties();
		try {
			props.loadFromXML(getClass().getResourceAsStream("MySQLServerConfiguration.xml"));
		} catch (InvalidPropertiesFormatException e) {
			throw e;
		}
		username = props.getProperty("jdbc.username");
		password = props.getProperty("jdbc.password");
		driver = props.getProperty("jdbc.driver");
		server = props.getProperty("jdbc.server");
		host = props.getProperty("jdbc.host");
		db = props.getProperty("jdbc.db");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw e;
		}
		try {
			return DriverManager.getConnection(server + "://" + host + "/" + db, username, password);
		} catch (SQLException e) {
			throw e;
		}
	}

	public int runUpdate(final String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public ResultSet runQuery(final String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public ResultSet runQuery(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢ - 1]);
		return ps.executeQuery();
	}

	public int runUpdate(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢ - 1]);
		return ps.executeUpdate();
	}

	public int runUpdate(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeUpdate();
	}

	public void clearBasicInfoTable() throws SQLException {
		runUpdate("DELETE FROM basic_info");
	}

	public void clearWikiIdTable() throws SQLException {
		runUpdate("DELETE FROM WikiID");
	}

	public void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
}