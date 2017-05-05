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

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;


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
	//TODO: add caching connection

	public Connector() throws Exception {
		try {
			conn = getConnection();
			createTable();
			fillTableWithDBpediaResults();
			//TODO: check if needed 
			//runQuery("use infoeval");
		} catch (Exception e) {
			throw  e;
		}
		logger.log(Level.INFO, "connected to server");
	}

	public Connection getConnection() throws SQLException, IOException, ClassNotFoundException {
		Properties props = new Properties();
		try {
			props.loadFromXML(getClass().getResourceAsStream("MySQLServerConfiguration.xml"));
		} catch (InvalidPropertiesFormatException  e) {
			throw  e;
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
			return DriverManager.getConnection(server+"://"+host+"/"+db, username, password);
		} catch (SQLException e) {
			throw e;
		}
	}

	public void createTable() throws SQLException{
		runUpdate("CREATE TABLE IF NOT EXISTS "
				+ "basic_info("
				+ "Name VARCHAR(50) NOT NULL,"
				+ "BirthPlace VARCHAR(50) NOT NULL,"
				+ "DeathPlace VARCHAR(50) NULL,"
				+ "BirthDate DATE NOT NULL,"
				+ "DeathDate DATE NULL,"
				+ "PRIMARY KEY (Name))");
		logger.log(Level.INFO, "table created successfully");
	}

	public void fillTableWithDBpediaResults() throws SQLException{
		Extractor ext = new Extractor();
		ext.executeQuery();
		ResultSetRewindable results = ext.getResults();
		//TODO: print results
	    ResultSetFormatter.outputAsXML(System.out,results);
		for(int i=0;i<results.size();++i){
			QuerySolution solution = results.nextSolution();
			String name=solution.getLiteral("name").getString();
		    String birthPlace=solution.getLiteral("birthPlace").getString();
		    String birthDate=solution.getLiteral("birthDate").getString();
		    String deathDate=solution.getLiteral("deathDate").getString();
		    String deathPlace=solution.getLiteral("deathPlace").getString();
		    Object[] inp=new Object[]{};
		    inp[0]=name;
		    inp[1]=birthPlace;
		    inp[2]=deathPlace;
		    inp[3]=birthDate;
		    inp[4]=deathDate;
		    runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?)",inp);			
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
		for (int i = 1;i <= inputs.length; ++i)
			ps.setObject(i, inputs[i-1]);
		return ps.executeQuery();
	}

	public int runUpdate(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeUpdate();
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