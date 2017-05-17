package infoeval.test.WikiDataTest;

import org.junit.Test;

import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;

import static org.junit.Assert.*;

//import org.apache.jena.query.ResultSetFormatter;


/**
 * 
 * @author Netanel
 * @author osherh
 * @since 05-04-2017
 * 
 *
 */
public class ExtractorTest {
	private static final int ENTRIES_NUM = 10000;

	

	@Test
	public void wikiIdTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(QueryTypes.WIKI_ID);
		assertEquals(extr.getResults().size(),ENTRIES_NUM);
	}
	@Test
	public void basicInfoTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(QueryTypes.BASIC_INFO);
		assertEquals(extr.getResults().size(),ENTRIES_NUM);
		//ResultSetFormatter.out(extr.getResults());
	}
}
