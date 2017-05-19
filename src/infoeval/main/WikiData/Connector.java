package infoeval.main.WikiData;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

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
	private static final BasicDataSource dataSource = new BasicDataSource();

	public Connector() throws IOException {
		try {
			initializeConnectionPool();
			logger.log(Level.INFO, "connection pool initialized");
		} catch (Exception e) {
			throw e;
		}
	}

	public void initializeConnectionPool() throws IOException {
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
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(server + "://" + host+ "/" +db);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
	}

	public Connection getConnection() throws SQLException, IOException, ClassNotFoundException /* ,NamingException */ {
		return dataSource.getConnection();
	}

	public int runUpdate(final String query) throws SQLException, ClassNotFoundException, IOException {
		try (Connection connection = getConnection();) {
			return connection.createStatement().executeUpdate(query);
		}
	}

	public ResultSet runQuery(final String query) throws SQLException, ClassNotFoundException, IOException {
		try (Connection connection = getConnection();) {
			return connection.createStatement().executeQuery(query);
		}
	}

	public ResultSet runQuery(final String query, final Object[] inputs)
			throws SQLException, ClassNotFoundException, IOException {
		try (PreparedStatement ps = getConnection().prepareStatement(query);) {
			for (int ¢ = 1; ¢ <= inputs.length; ++¢)
				ps.setObject(¢, inputs[¢ - 1]);
			return ps.executeQuery();
		}
	}

	public int runUpdate(final String query, final Object[] inputs)
			throws SQLException, ClassNotFoundException, IOException {
		try (PreparedStatement ps = getConnection().prepareStatement(query);) {
			for (int ¢ = 1; ¢ <= inputs.length; ++¢)
				ps.setObject(¢, inputs[¢ - 1]);
			return ps.executeUpdate();
		}
	}

	public int runUpdate(final String query, final Object input)
			throws SQLException, ClassNotFoundException, IOException {
		try (PreparedStatement $ = getConnection().prepareStatement(query);) {
			$.setObject(1, input);
			return $.executeUpdate();
		}
	}

	public void clearBasicInfoTable() throws SQLException, ClassNotFoundException, IOException {
		runUpdate("DELETE FROM basic_info");
	}

	public void clearWikiIdTable() throws SQLException, ClassNotFoundException, IOException {
		runUpdate("DELETE FROM WikiID");
	}
}