package infoeval.test.AnalysisTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 
 * @author moshiko
 * @since  05-04-2017
 * 
 * 
 */
import org.junit.Test;

import infoeval.main.Analysis.AnalyzeParagraph;
import infoeval.main.WikiData.WikiParsing;

public class AnalyzeParagraphTest {

	@Test
	public void Test1() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
		wiki.Parse("arrested");
		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.setParagraphsArrests(wiki.getParagraphs());
		analyze.AnalyzeArrestsQuery();
		LinkedList<String> info = analyze.getArrestsInformation();
		assert(info.size() > 0);
	}

	// This test is for the new method of awards query.(see issue #224)
	@Test
	public void Test2() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Adele"));
		wiki.Parse("won");
		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.setParagraphsAwards(wiki.getParagraphs());
		analyze.AwardsQuery();
		LinkedList<String> info = analyze.getAwardsInformation();
		for (final String ¢ : info)
			System.out.println(¢);
			assert(info.contains(" At the 51st Grammy Awards in 2009, Adele received the awards for Best New Artist and Best Female Pop Vocal Performance"));
				}

	// This test is for the new method of awards query.(see issue #224)
	@Test
	public void Test3() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Justin_Bieber"));
		wiki.Parse("arrested");
		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.setParagraphsArrests(wiki.getParagraphs());
		analyze.ArrestsQuery();
		LinkedList<String> info = analyze.getArrestsInformation();
		assert info.contains(" When he failed to do so, an arrest warrant was issued and two of his bodyguards were released in Argentina in April 2015");
	}

	// Test for dynamic query:
	@Test
	public void Test4() throws Exception {

		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.dynamicQuery("Donald Trump", "married");
		assert analyze.getDynamicInformation()
				.contains(" On a visit to Kallstadt, he met Elisabeth Christ and married her in 1902");
		assert analyze.getDynamicInformation()
				.contains(" Fred and Mary were married in 1936 and raised their family in Queens");
		assert analyze.getDynamicInformation().contains(
				"Trump married his first wife, Czech model Ivana Zelníčková, on April 7, 1977, at the Marble Collegiate Church in Manhattan in a ceremony performed by the Reverend Norman Vincent Peale");
		assert analyze.getDynamicInformation()
				.contains(" Maples and Trump were married two months later on December 20, 1993");
		assert analyze.getDynamicInformation().contains(
				"Trump married Slovene model Melania Knauss on January 22, 2005 at Bethesda-by-the-Sea Episcopal Church in Palm Beach, Florida, followed by a reception at Trump's Mar-a-Lago estate");
		assert analyze.getDynamicInformation()
				.contains(" His parents married in a Manhattan Presbyterian church in 1936");

	}
}
