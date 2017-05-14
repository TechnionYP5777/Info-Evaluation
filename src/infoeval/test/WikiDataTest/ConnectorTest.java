package infoeval.test.WikiDataTest;

import static org.junit.Assert.*;
import java.sql.ResultSet;
import org.junit.Ignore;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;

public class ConnectorTest {
	@Test
	public void connectionTest() throws Exception {
		assert (new Connector()).getConnection() != null;
	}

	@Ignore
	@Test
	public void fillTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		Extractor ext = new Extractor();

		int size = 0;
		ResultSet rs = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		if (rs.next())
			size = rs.getInt(1);
		assertEquals(10000, size);
		ext.executeQuery(ext.getQuery("basicInfoQuery"));
		assertEquals(ext.getResults().size(), size);

		size = 0;
		rs = conn.runQuery("SELECT COUNT(*) FROM wikiID");
		if (rs.next())
			size = rs.getInt(1);
		assertEquals(10, size);
		ext.executeQuery(ext.getQuery("wikiIDQuery"));
		assertEquals(ext.getResults().size(), size);
		conn.closeConnection();
	}
}