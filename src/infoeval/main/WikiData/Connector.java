package infoeval.main.WikiData;

import java.sql.DriverManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	public Connector() throws Exception {
		try {
			conn = getConnection();
			logger.log(Level.INFO, "connected to server");
			dropTables();
			createTables();
			fillBasicInfoTable();
			fillWikiIDTable();
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

	public void createTables() throws SQLException {
		runUpdate("CREATE TABLE IF NOT EXISTS basic_info(Name VARCHAR(100) NOT NULL,"
				+ "BirthPlace VARCHAR(100) NULL,DeathPlace VARCHAR(100) NULL,BirthDate DATE NULL,"
				+ "DeathDate DATE NULL)");
		logger.log(Level.INFO, "basic_info table created successfully");
		runUpdate("CREATE TABLE IF NOT EXISTS WikiID(Name VARCHAR(100) NOT NULL,WikiPageID VARCHAR(50) NOT NULL)");
		// + "PRIMARY KEY (Name,wikiPageID))");
		logger.log(Level.INFO, "wiki ID's table created successfully");
	}

	public void fillBasicInfoTable() throws SQLException, ParseException {
		clearBasicInfoTable();
		Extractor ext = new Extractor();
		ext.executeQuery(ext.getQuery("basicInfoQuery"));
		ResultSetRewindable results = ext.getResults();
		results.reset();
		for (int i = 0; i < results.size(); ++i) {

			System.out.println(i);

			QuerySolution solution = results.nextSolution();

			String name = solution.getLiteral("name").getString();

			RDFNode bPlace = solution.get("birth");
			String birthPlace = null;
			if (bPlace != null)
				if (bPlace.isResource())
					birthPlace = (bPlace.asResource() + "").split("resource/")[1];
				else if (bPlace.isLiteral())
					birthPlace = (bPlace.asLiteral() + "").split("@")[0];

			RDFNode dPlace = solution.get("death");
			String deathPlace = null;
			if (dPlace != null)
				if (dPlace.isResource())
					deathPlace = (dPlace.asResource() + "").split("resource/")[1];
				else if (dPlace.isLiteral())
					deathPlace = (dPlace.asLiteral() + "").split("@")[0];

			RDFNode bDate = solution.get("bDate");
			String birthDate = !bDate.isLiteral() ? null : bDate.asLiteral().getValue() + "";

			RDFNode dDate = solution.get("dDate");
			String deathDate = null;
			java.sql.Date sqlDeathDate = null;
			if (dDate == null)
				sqlDeathDate = null;
			else {
				deathDate = !dDate.isLiteral() ? null : dDate.asLiteral().getValue() + "";
				if (deathDate.contains("."))
					sqlDeathDate = null;
				else if (deathDate.split("-").length == 1 && deathDate.matches("[0-9]+") && birthDate.length() <= 4)
					sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy"));
				else if (deathDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
					sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy-MM-dd"));
				int monthNum = 1;
				for (String month : months) {
					if (deathDate.startsWith(month)) {
						String parseDeathDate = deathDate.split(" ")[1] + "-" + monthNum;
						sqlDeathDate = stringToSqlDate(parseDeathDate, new SimpleDateFormat("yyyy-MM"));
						break;
					}
					++monthNum;
				}
			}

			java.sql.Date sqlBirthDate = null;
			if (birthDate.contains("."))
				sqlDeathDate = null;
			else if (birthDate.split("-").length == 1 && birthDate.matches("[0-9]+") && birthDate.length() <= 4)
				sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy"));
			else if (birthDate.startsWith("--") && birthDate.split("--").length == 2)
				sqlBirthDate = stringToSqlDate(birthDate.split("--")[1], new SimpleDateFormat("MM-dd"));
			else if (birthDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
				sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy-MM-dd"));
			int monthNum = 1;
			for (String month : months) {
				if (birthDate.startsWith(month)) {
					String parseBirthDate = birthDate.split(" ")[1] + "-" + monthNum;
					sqlBirthDate = stringToSqlDate(parseBirthDate, new SimpleDateFormat("yyyy-MM"));
					break;
				}
				++monthNum;
			}

			Object[] inp = new Object[5];
			inp[0] = name;
			inp[1] = birthPlace;
			inp[2] = deathPlace;
			inp[3] = sqlBirthDate;
			inp[4] = sqlDeathDate;
			runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?)", inp);
		}
	}

	public void fillWikiIDTable() throws SQLException {
		clearWikiIDTable();
		Extractor ext = new Extractor();
		ext.executeQuery(ext.getQuery("wikiIDQuery"));
		ResultSetRewindable results = ext.getResults();
		results.reset();
		for (int i = 0; i < results.size(); ++i) {
			QuerySolution solution = results.nextSolution();
			String name = solution.getLiteral("name").getString(),
					wikiPageID = solution.getLiteral("wikiPageID").getString();
			Object[] inp = new Object[2];
			inp[0] = name;
			inp[1] = wikiPageID;
			runUpdate("INSERT INTO wikiID VALUES(?,?)", inp);
		}
	}

	private java.sql.Date stringToSqlDate(String stringDate, SimpleDateFormat f) throws ParseException {
		return new java.sql.Date(f.parse(stringDate).getTime());
	}

	public int runUpdate(final String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public ResultSet runQuery(final String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	public ResultSet runQuery(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int $ = 1; $ <= inputs.length; ++$)
			ps.setObject($, inputs[$ - 1]);
		return ps.executeQuery();
	}

	public int runUpdate(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int $ = 1; $ <= inputs.length; ++$)
			ps.setObject($, inputs[$ - 1]);
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

	public void clearWikiIDTable() throws SQLException {
		runUpdate("DELETE FROM wikiID");
	}

	public void clearDB() throws SQLException {
		clearBasicInfoTable();
		clearWikiIDTable();
	}

	public void dropTables() throws SQLException {
		runUpdate("DROP TABLE basic_info");
		runUpdate("DROP TABLE wikiID");
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