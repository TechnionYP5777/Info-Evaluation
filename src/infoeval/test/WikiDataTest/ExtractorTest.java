package infoeval.test.WikiDataTest;

import org.junit.Test;
import org.junit.Ignore;

import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import infoeval.main.WikiData.SqlTablesFiller;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;

import java.sql.Date;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
		String name = "michelle williams (actress)", newName = WordUtils.capitalize(name).replaceAll(" ", "_");
		Extractor extr = new Extractor(newName);
		extr.executeQuery(QueryTypes.ABSTRACT);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		RDFNode overview = results.nextSolution().get("abstract");
		String overviewStr = "No Abstract";
		if (overview != null)
			if (overview.isResource())
				overviewStr = (overview.asResource() + "").split("resource/")[1];
			else if (overview.isLiteral())
				overviewStr = (overview.asLiteral() + "").split("@")[0];

		assertEquals(overviewStr,
				"Michelle Ingrid Williams (born September 9, 1980) is an American actress. She began her career with television guest appearances, and made her feature film debut in Lassie (1994), which earned her a Youth in Film nomination. She is known for her role as Jen Lindley on the The WB series Dawson's Creek, from 1998 to 2003. Williams received critical acclaim for her role as the wife of Ennis Del Mar in Brokeback Mountain (2005), for which she won a Broadcast Film Critics Association Award and was nominated for the SAG Award, BAFTA Award, Golden Globe, and Academy Award for Best Supporting Actress. She followed this with films such as Martin Scorsese's Shutter Island (2010). Williams' performance as a drifter in 2008's Wendy and Lucy earned her a Independent Spirit Award nomination, and her work in Blue Valentine (2010) garnered her nominations for the Golden Globe Award and the Academy Award for Best Actress. She won a Golden Globe and an Independent Spirit Award for her portrayal of Marilyn Monroe in My Week with Marilyn (2011), which also garnered her BAFTA, SAG, and Academy Award nominations. In 2014, she made her Broadway debut in a revival of Cabaret as Sally Bowles. She returned to Broadway in 2016 in a production of Blackbird alongside Jeff Daniels.");
	}

	@Ignore
	@Test
	public void basicInfoBracketsNameTest() throws Exception {
		String name = "Michelle_Williams_(singer)";
		Extractor extr = new Extractor(name);
		extr.executeQuery(QueryTypes.BASIC_INFO_BRACKETS_NAME);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		SqlTablesFiller filler = new SqlTablesFiller();
		TableEntry te = filler.getInfo(results);
		filler.close();

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
	public void basicInfoByIDTest() throws Exception {
		int wikiPageID = Integer.parseInt(Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=Shakira&prop=pageimages&format=xml&pithumbsize=350").get().toString().split("pageid=\"")[1].split("\"")[0]);

		Extractor extr = new Extractor(wikiPageID);
		extr.executeQuery(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID);
		ResultSetRewindable results = extr.getResults();
		results.reset();

		SqlTablesFiller filler = new SqlTablesFiller();
		TableEntry te = filler.getInfo(results);
		filler.close();

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
		int wikiPageID = Integer.parseInt(Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=Shakira&prop=pageimages&format=xml&pithumbsize=350").get().toString().split("pageid=\"")[1].split("\"")[0]);
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
