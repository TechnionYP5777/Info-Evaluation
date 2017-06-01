package infoeval.test.WikiDataTest;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.SqlTablesFiller;
import infoeval.main.mysql.Row;

/**
 * @authors osherh , Moshe
 * @since 20-05-2017
 */


/*
 * ATTENTION ! When you want to test this class , remove the @Ignore attributes.
 * I added it since the connector tries to read from the config.xml file which won't be uploaded to GitHub and it causes
 * travisCI to fail. 
 * like moshiko did in the connectorTest it's relevant here too
 * @osherh 
 */
public class SqlTablesFillerTest {
	private final int ENTRIES_NUM = 10000;
	static SqlTablesFiller filler;
	
	@BeforeClass
	@Ignore
	public static void init() throws IOException, ClassNotFoundException, SQLException, ParseException {
	filler = new SqlTablesFiller();
		//filler.createTables();
		//filler.fillBasicInfoTable();
		//filler.fillWikiIdTable();
	//	filler.close();
	}
	
	@AfterClass
	@Ignore
	public static void close() throws ClassNotFoundException, IOException, SQLException{
		filler.close();
	}

	@Ignore
	@Test
	public void basicInfoTableSizeTest() throws Exception {
		Connector conn = new Connector();
		ArrayList<Row> rows = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		long size = (long) col.getValue().cast(col.getKey());
		assertEquals(ENTRIES_NUM, size);
		//conn.close();
	}

	@Ignore
	@Test
	public void wikiIdTableSizeTest() throws Exception {
		Connector conn = new Connector();
		ArrayList<Row> rows = conn.runQuery("SELECT COUNT(*) FROM WikiID");
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		long size = (long) col.getValue().cast(col.getKey());
		assertEquals(ENTRIES_NUM, size);
		//conn.close();
	}
	
	@Ignore
	@Test
	public void IndexTest() throws Exception {
//		SqlTablesFiller filler = new SqlTablesFiller();
		filler.dropIndex();
		filler.addIndexBasicInfo();
		filler.addIndexWikiID();
		//filler.close();
	}
	
}
