package infoeval.main.WikiData;

import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;

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
			//createTables();
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
		//conn = getConnection();
		//logger.log(Level.INFO, "connected to server in run update");		
		int res = conn.createStatement().executeUpdate(query);
		//closeConnection();
		//logger.log(Level.INFO, "close connection in run update");
		return res;
	}

	public ResultSet runQuery(final String query) throws SQLException{
		//conn = getConnection();
		//logger.log(Level.INFO, "connected to server in run query");		
		Statement s = conn.createStatement();
		ResultSet res = s.executeQuery(query);
		//closeConnection();
		//logger.log(Level.INFO, "close connection in run query");
		return res;
	}

	public ResultSet runQuery(final String query, final Object[] inputs) throws SQLException {
		//conn = getConnection();
		//logger.log(Level.INFO, "get connection in run query with params");		
		PreparedStatement ps = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢ - 1]);
		//closeConnection();
		//logger.log(Level.INFO, "close connection in run query with params");
		return ps.executeQuery();
	}

	public int runUpdate(final String query, final Object[] inputs) throws SQLException {
		//conn = getConnection();		
		//logger.log(Level.INFO, "get connection in run update array");
		PreparedStatement ps = conn.prepareStatement(query);
		for (int ¢ = 1; ¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢ - 1]);
		//closeConnection();
		//logger.log(Level.INFO, "close connection in run update array");
		return ps.executeUpdate();
	}

	public int runUpdate(final String query, final Object input) throws SQLException {
		//conn = getConnection();		
		//logger.log(Level.INFO, "get connection in run update");
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		//closeConnection();
		//logger.log(Level.INFO, "close connection in run update");
		return $.executeUpdate();
	}

	//public void dropTables() throws SQLException {
	//	runUpdate("DROP TABLE basic_info");
	//	runUpdate("DROP TABLE WikiID");
	//}

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