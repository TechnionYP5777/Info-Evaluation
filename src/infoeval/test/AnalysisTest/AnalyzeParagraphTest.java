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

	@Test
	public void Test1() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Axl_Rose"));
		wiki.Parse("arrested");
		(new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();

	}

	@Test
	public void Test2() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
		wiki.Parse("arrested");
		(new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();

	}


	// This test is for the new method of awards query.(see issue #224)
	@Test
	public void Test3() throws IOException {
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Adele"));
		wiki.Parse("won");
		AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.setParagraphs(wiki.getParagraphs());
		analyze.AwardsQuery();
		for (final String ¢ : analyze.getInformation())
			System.out.println(¢);
	}
}
