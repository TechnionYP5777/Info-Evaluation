package infoeval.test.WikiDataTest;

import org.junit.Test;

import infoeval.main.WikiData.Extractor;

import static org.junit.Assert.*;

/**
 * 
 * @author Netanel
 * @author osherh
 * @since 05-04-2017
 * 
 *
 */
public class ExtractorTest {
	@Test
	public void basicInfoTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(extr.getQuery("basicInfoQuery"));
		assertEquals(extr.getResults().size(), 10000);
	}

	@Test
	public void wikiIDTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(extr.getQuery("wikiIDQuery"));
		assertEquals(extr.getResults().size(), 10000);
	}
}