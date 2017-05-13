package infoeval.test.WikiDataTest;
import static org.junit.Assert.*;

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
		int size = conn.runQuery("SELECT COUNT(*) FROM basic_info").getInt(1);
		Extractor ext = new Extractor();
		ext.executeQuery();
		assertEquals(ext.getResults().size(),size);
		assertEquals(10000,size);
		conn.closeConnection();
	}
}