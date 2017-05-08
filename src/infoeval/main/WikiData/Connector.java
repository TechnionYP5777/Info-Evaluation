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
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.impl.ResourceImpl;


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
				+ "BirthPlace VARCHAR(50) NULL,"
				+ "DeathPlace VARCHAR(50) NULL,"
				+ "BirthDate DATE NULL,"
				+ "DeathDate DATE NULL,"
				+ "PRIMARY KEY (Name))");
		logger.log(Level.INFO, "table created successfully");
	}

	public void fillTableWithDBpediaResults() throws SQLException, ParseException{
		clearDB();
		Extractor ext = new Extractor();
		ext.executeQuery();
		ResultSetRewindable results = ext.getResults();
		results.reset();
	    for(int i=0;i<results.size();++i){
//TODO:
	    	System.out.println(i);

	    	QuerySolution solution = results.nextSolution();
	    	
	    	String name=solution.getLiteral("name").getString();
	    	
	    	RDFNode bPlace = solution.get("birth");
	    	String birthPlace=null;
	    	if (bPlace.isResource())
				birthPlace = (bPlace.asResource() + "").split("resource/")[1];
			else if(bPlace.isLiteral())
				birthPlace = (bPlace.asLiteral() + "").split("@")[0];
	    	
	    	RDFNode dPlace = solution.get("death");
    		
	    	String deathPlace=null;
	    	if(dPlace!=null)
				if (dPlace.isResource())
					deathPlace = (dPlace.asResource() + "").split("resource/")[1];
				else if (dPlace.isLiteral())
					deathPlace = (dPlace.asLiteral() + "").split("@")[0];
	    	
	    	RDFNode bDate = solution.get("bDate");
	    	String birthDate=!bDate.isLiteral() ? null : bDate.asLiteral().getValue() + "";
	    	
	    	RDFNode dDate = solution.get("dDate");
	    	String deathDate=null;
	    	java.sql.Date sqlDeathDate=null;
	    	if(dDate!=null){
	    		deathDate=!dDate.isLiteral() ? null : dDate.asLiteral().getValue() + "";
	    		sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy" + (deathDate.split("-").length == 1 ? "" : "-MM-dd")));
	    	}
	    	
	    	java.sql.Date sqlBirthDate=birthDate.split("-").length == 1 ? stringToSqlDate(birthDate, new SimpleDateFormat("yyyy"))
					: !birthDate.startsWith("--") || birthDate.split("--").length != 2
							? stringToSqlDate(birthDate, new SimpleDateFormat("yyyy-MM-dd"))
							: stringToSqlDate(birthDate.split("--")[1], new SimpleDateFormat("MM-dd"));	    	
	    	Object[] inp=new Object[5];
		    inp[0]=name;
		    inp[1]=birthPlace;
		    inp[2]=deathPlace;
		    inp[3]=sqlBirthDate;
		    inp[4]=sqlDeathDate;
		    runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?)",inp);
		}
	}

	private java.sql.Date stringToSqlDate(String stringDate,SimpleDateFormat f) throws ParseException{
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
		for (int ¢ = 1;¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢-1]);
		return ps.executeQuery();
	}

	public int runUpdate(final String query, final Object[] inputs) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(query);
		for (int ¢ = 1;¢ <= inputs.length; ++¢)
			ps.setObject(¢, inputs[¢-1]);
		return ps.executeUpdate();
	}
	
	public int runUpdate(final String query, final Object input) throws SQLException {
		PreparedStatement $ = conn.prepareStatement(query);
		$.setObject(1, input);
		return $.executeUpdate();
	}
	
	public void clearDB() throws SQLException{
		runUpdate("DELETE FROM basic_info");
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