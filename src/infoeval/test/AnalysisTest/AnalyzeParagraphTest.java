package infoeval.test.AnalysisTest;

import java.io.IOException;

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

	// @Test
	// public void Test1() throws IOException {
	// WikiParsing wiki = (new
	// WikiParsing("https://en.wikipedia.org/wiki/Axl_Rose"));
	// wiki.Parse("arrested");
	// (new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();
	//
	// }
	//
	// @Test
	// public void Test2() throws IOException {
	// WikiParsing wiki = (new
	// WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
	// wiki.Parse("arrested");
	// (new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();
	//
	// }
	//
	//
	// // This test is for the new method of awards query.(see issue #224)
	// @Test
	// public void Test3() throws IOException {
	// WikiParsing wiki = (new
	// WikiParsing("https://en.wikipedia.org/wiki/Adele"));
	// wiki.Parse("won");
	// AnalyzeParagraph analyze = new AnalyzeParagraph();
	// analyze.setParagraphs(wiki.getParagraphs());
	// analyze.AwardsQuery();
	// for (final String ¢ : analyze.getInformation())
	// System.out.println(¢);
	// }

	// Test for dynamic query:
	@Test
	public void Test4() throws Exception {

		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.dynamicQuery("Donald Trump", "married");
//		for (final String ¢ : analyze.getInformation())
//			System.out.println(¢);
		assert analyze.getInformation().contains(" On a visit to Kallstadt, he met Elisabeth Christ and married her in 1902");
		assert analyze.getInformation().contains(" Fred and Mary were married in 1936 and raised their family in Queens");
		assert analyze.getInformation().contains("Trump married his first wife, Czech model Ivana Zelníčková, on April 7, 1977, at the Marble Collegiate Church in Manhattan in a ceremony performed by the Reverend Norman Vincent Peale");
		assert analyze.getInformation().contains(" Maples and Trump were married two months later on December 20, 1993");
		assert analyze.getInformation().contains("Trump married Slovene model Melania Knauss on January 22, 2005 at Bethesda-by-the-Sea Episcopal Church in Palm Beach, Florida, followed by a reception at Trump's Mar-a-Lago estate");
		assert analyze.getInformation().contains(" His parents married in a Manhattan Presbyterian church in 1936");

		
	}
}
