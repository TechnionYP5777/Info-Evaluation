package infoeval.test.WikiDataTest;
import static org.junit.Assert.*;

import java.sql.ResultSet;

import org.apache.jena.query.ResultSetRewindable;
import org.junit.Ignore;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;

public class ConnectorTest{
	@Test 
	public void connectionTest() throws Exception{
		assert (new Connector()).getConnection() != null;
	}

	@Ignore
	@Test 
	public void fillTableSizeTest() throws Exception{
		Connector conn = new Connector();
		assert conn.getConnection() != null;
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