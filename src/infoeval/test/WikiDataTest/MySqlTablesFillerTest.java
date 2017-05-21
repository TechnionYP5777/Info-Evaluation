package infoeval.test.WikiDataTest;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.MySqlTablesFiller;
import infoeval.main.mysql.Row;

/**
 * @author osherh
 * @since 20-05-2017
 */
public class MySqlTablesFillerTest {
	private static final int ENTRIES_NUM = 10000;

	@BeforeClass
	public static void test() throws IOException, ClassNotFoundException, SQLException, ParseException {
		MySqlTablesFiller filler = new MySqlTablesFiller();
		filler.createTables();
		filler.fillBasicInfoTable();
		filler.fillWikiIdTable();
	}

	@Test
	public void basicInfoTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;

		ArrayList<Row> rows = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		Object size = col.getValue().cast(col.getKey());

		assertEquals(ENTRIES_NUM, size);

		conn.close();
	}

	@Test
	public void wikiIdTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;

		ArrayList<Row> rows = conn.runQuery("SELECT COUNT(*) FROM WikiID");
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		Object size = col.getValue().cast(col.getKey());

		assertEquals(ENTRIES_NUM, size);

		conn.close();
	}
}
