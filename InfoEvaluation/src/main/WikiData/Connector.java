package WikiData;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 * 
 * @author Netanel
 * @since 19-04-2017
 *
 */

public class Connector {
	

	
	  public void connect() throws Exception {
	    String driverName = "org.gjt.mm.mysql.Driver";
	    Class.forName(driverName);

	    String serverName = "";//will represent the real server name
	    String mydatabase = "";//will represent the real DB name
	    String url = "jdbc:mysql :// " + serverName + "/" + mydatabase; 

	    String username = "";//will represent the real user name
	    String password = "";//will represent the real password
	    Connection connection = DriverManager.getConnection(url, username, password);
	    connection.getSchema();
	    
	  }
	}

