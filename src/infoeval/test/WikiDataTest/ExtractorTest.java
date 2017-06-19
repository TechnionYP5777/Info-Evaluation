package infoeval.test.WikiDataTest;

import org.junit.Test;
import org.junit.Ignore;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import infoeval.main.WikiData.SqlTablesFiller;
import infoeval.main.mysql.TableEntry;
import static org.junit.Assert.*;
import java.sql.Date;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;
import org.jsoup.Jsoup;

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
	public void basicInfoByIDTest() throws Exception {
		int wikiPageID = Integer.parseInt((Jsoup
				.connect(
						"https://en.wikipedia.org/w/api.php?action=query&titles=Michael_Jackson&prop=pageimages&format=xml&pithumbsize=350")
				.get() + "").split("pageid=\"")[1].split("\"")[0]);

		Extractor extr = new Extractor(wikiPageID);
		extr.executeQuery(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		TableEntry te = SqlTablesFiller.getBasicInfo(results);

		System.out.println("Name is " + te.getName());

		System.out.println("Birth Place is " + te.getBirthPlace());

		System.out.println("Death Place is " + te.getDeathPlace());

		Date birthDate = te.getBirthDate();
		if (birthDate != null)
			System.out.println("Birth Date is " + birthDate);

		Date deathDate = te.getDeathDate();
		if (deathDate != null)
			System.out.println("Death Date is " + deathDate);

		System.out.println("Occupation is " + te.getOccupation());

		System.out.println("Spouse Name is " + te.getSpouseName());

		System.out.println("Spouse Occupation is " + te.getSpouseOccupation());

		System.out.println("PhotoLink is " + te.getPhotoLink());
	}

	@Ignore
	@Test
	public void abstractByWikiPageIdTest() throws Exception {
		int wikiPageID = Integer.parseInt((Jsoup
				.connect(
						"https://en.wikipedia.org/w/api.php?action=query&titles=Shakira&prop=pageimages&format=xml&pithumbsize=350")
				.get() + "").split("pageid=\"")[1].split("\"")[0]);
		Extractor extr = new Extractor(wikiPageID);
		extr.executeQuery(QueryTypes.ABSTRACT_BY_WIKI_PAGE_ID);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		RDFNode overview = results.nextSolution().get("abstract");
		String overviewStr = "No Abstract";
		if (overview != null)
			if (overview.isResource())
				overviewStr = (overview.asResource() + "").split("resource/")[1];
			else if (overview.isLiteral())
				overviewStr = (overview.asLiteral() + "").split("@")[0];

		System.out.println("Abstract is " + overviewStr);
	}
}
