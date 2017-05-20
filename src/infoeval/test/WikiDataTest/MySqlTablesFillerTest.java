package infoeval.test.WikiDataTest;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.MySqlTablesFiller;


/**
 * @author osherh
 * @since 20-05-2017
 */
public class MySqlTablesFillerTest {
	private static final int ENTRIES_NUM = 10000;

	
	@BeforeClass
	public static void test() throws IOException, ClassNotFoundException, SQLException, ParseException{
		MySqlTablesFiller filler = new MySqlTablesFiller();
		filler.createTables();
		filler.fillBasicInfoTable();
		filler.fillWikiIdTable();
	}

	@Test
	public void basicInfoTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		conn.close();
	}

	@Test
	public void wikiIdTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM WikiID");
		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		conn.close();
	}
}
