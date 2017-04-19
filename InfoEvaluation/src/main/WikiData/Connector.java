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

	    DriverManager.getConnection(("jdbc:mysql :// " + "/"), "", "").getSchema();
	    
	  }
	}

