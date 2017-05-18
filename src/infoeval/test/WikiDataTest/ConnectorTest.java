package infoeval.test.WikiDataTest;

import static org.junit.Assert.*;

import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import java.sql.ResultSet;

/**
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 */
public class ConnectorTest {
	private static final int ENTRIES_NUM = 10000;
	
	//@Test
	public void connectionTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		conn.closeConnection();
	}

	//@Test
	public void fillBasicInfoTableSizeTest() throws Exception {
		Connector conn = new Connector();
		System.out.println("Conencor works");
		assert conn.getConnection() != null;
		System.out.println("Conenction is established");
		Extractor ext = new Extractor();

		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM basic_info");
		System.out.println("got num of basic info entries");
		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		ext.executeQuery(QueryTypes.BASIC_INFO);
		assertEquals(ext.getResults().size(), size);
	
		conn.closeConnection();
	}

	@Test
	public void fillWikiIdTableSizeTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		System.out.println("Conenction is established");
		Extractor ext = new Extractor();

		int size = 0;
		ResultSet s = conn.runQuery("SELECT COUNT(*) FROM wikiID");
		System.out.println("got num of wiki id entries");

		if (s.next())
			size = s.getInt(1);
		assertEquals(ENTRIES_NUM, size);
		ext.executeQuery(QueryTypes.WIKI_ID);
		assertEquals(ext.getResults().size(), size);
		
		conn.closeConnection();
	}

	
	//@Test
	public void printBasicInfoTableTest() throws Exception {
		Connector conn = new Connector();		
		ResultSet allB = conn.runQuery("SELECT * FROM basic_info");
		while(allB.next()){
			String name = allB.getString("name");
			String bPlace = allB.getString("birthPlace");
			String dPlace = allB.getString("deathPlace");
			String bDate = allB.getString("birthDate");
			String dDate = allB.getString("deathDate");
			System.out.println(name+" "+bPlace+" "+dPlace+" "+bDate+" "+dDate);
		}
		conn.closeConnection();
	}
	
	//@Test
	public void printWikiIdTableTest() throws Exception {
		Connector conn = new Connector();		
		ResultSet allI = conn.runQuery("SELECT name FROM WikiID");
		while(allI.next()){
			String name = allI.getString("name");
			String wikiPageID = allI.getString("wikiPageID");
			System.out.println(name+" "+wikiPageID); 
		}
		conn.closeConnection();
	}
}