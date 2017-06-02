package infoeval.test.WikiDataTest;

import org.junit.Test;
import org.junit.Ignore;

import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;

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

	@Ignore
	@Test
	public void wikiIdTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(QueryTypes.WIKI_ID);
		assertEquals(extr.getResults().size(), ENTRIES_NUM);
	}

	@Ignore
	@Test
	public void basicInfoTest() {
		Extractor extr = new Extractor();
		extr.executeQuery(QueryTypes.BASIC_INFO);
		assertEquals(extr.getResults().size(), ENTRIES_NUM);
	}

	@Ignore
	@Test
	public void abstractTest() {
		String name = "Jessica Zelinka", newName = WordUtils.capitalize(name).replaceAll(" ", "_");
		Extractor extr = new Extractor(newName);
		extr.executeQuery(QueryTypes.ABSTRACT);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		QuerySolution solution = results.nextSolution();
		RDFNode overview = solution.get("abstract");
		String overviewStr = "No Abstract";
		if (overview != null)
			if (overview.isResource())
				overviewStr = (overview.asResource() + "").split("resource/")[1];
			else if (overview.isLiteral())
				overviewStr = (overview.asLiteral() + "").split("@")[0];

		assertEquals(overviewStr,
				"Jessica Zelinka (born September 3, 1981 in London, Ontario) is a Canadian pentathlete, heptathlete, and 100 m hurdler. Her personal best score is 6599 points for the heptathlon. She was the gold medalist at the 2007 Pan American Games. Zelinka won silver at the 2010 Commonwealth Games and repeated her silver medal at the 2014 Commonwealth Games. At the 2012 Summer Olympics Zelinka finished in 7th overall in both the heptathlon and 100 m hurdles.");
	}
}
