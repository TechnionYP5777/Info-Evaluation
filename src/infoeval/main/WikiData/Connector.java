package infoeval.main.WikiData;

import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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
	private Connection conn;
	//TODO: add caching connection

	public Connector() throws Exception {
		try {
			conn = getConnection();
			logger.log(Level.INFO, "connected to server");
		} catch (Exception ¢) {
			throw ¢;
		}
		givePermissions();
		logger.log(Level.INFO, "database created succesfully");
	}

	private Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
		Properties props = new Properties();
		try {
			props.loadFromXML(getClass().getResourceAsStream("resources/MySQLServerConfiguration.xml"));
		} catch (InvalidPropertiesFormatException ¢) {
			throw ¢;
		}
		String username = props.getProperty("jdbc.username"), password = props.getProperty("jdbc.password"),
				driver = props.getProperty("jdbc.driver"), url = props.getProperty("jdbc.url");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException ¢) {
			throw ¢;
		}
		try {
			return DriverManager.getConnection(url, username, password);
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	private void givePermissions() throws SQLException, IOException {
		Properties props = new Properties();
		try {
			props.loadFromXML(getClass().getResourceAsStream("resources/MySQLServerConfiguration.xml"));
		} catch (InvalidPropertiesFormatException ¢) {
			throw ¢;
		}
		runQuery("GRANT ALL PRIVILEGES ON *.* TO user@% IDENTIFIED BY " + props.getProperty("jdbc.password"));
	}

	public int runUpdate(final String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public ResultSet runQuery(final String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public ResultSet runQuery(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			$.setObject(¢, inputs[¢ - 1]);
		return $.executeQuery();
	}

	public ResultSet runQuery(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeQuery();
	}

	public int runUpdate(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeUpdate();
	}

	public void clearDB() throws SQLException {
		runUpdate("DELETE FROM celebs_arrests");
		runUpdate("DELETE FROM keywords_table");
	}

	public void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (final SQLException ¢) {
			¢.printStackTrace();
		}
	}
}