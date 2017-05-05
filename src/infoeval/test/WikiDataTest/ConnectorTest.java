package infoeval.test.WikiDataTest;
import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.apache.jena.query.ResultSetRewindable;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;

public class ConnectorTest{
	@Test 
	public void connectionTest() throws Exception{
		Connector conn=new Connector();
		assertNotNull(conn.getConnection());
	}
	
	@Test 
	public void fillTableSizeTest() throws Exception{
		Connector conn=new Connector();
		assertNotNull(conn.getConnection());
		conn.fillTableWithDBpediaResults();
		ResultSet rs = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		int size = rs.getInt(1);
		Extractor ext = new Extractor();
		ext.executeQuery();
		int dbpediaResultsSize = ext.getResults().size();
		assertEquals(dbpediaResultsSize,size);
		assertEquals(10000,size);
		conn.closeConnection();
	}
}