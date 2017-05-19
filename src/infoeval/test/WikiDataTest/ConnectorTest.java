package infoeval.test.WikiDataTest;

import static org.junit.Assert.*;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 */
public class ConnectorTest {
	private static final int ENTRIES_NUM = 10000;

	@Test
	public void connectionTest() throws Exception {
		Connector conn = new Connector();
		Connection connection = conn.getConnection();
		assert connection != null;
		connection.close();
	}

	@Test
	public void fillBasicInfoTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		Extractor ext = new Extractor();

		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		ext.executeQuery(QueryTypes.BASIC_INFO);
		assertEquals(ext.getResults().size(), size);
		conn.close();
	}

	@Test
	public void fillWikiIdTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		Extractor ext = new Extractor();

		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM wikiID");
		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		ext.executeQuery(QueryTypes.WIKI_ID);
		assertEquals(ext.getResults().size(), size);
		conn.close();
	}
}