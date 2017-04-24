package AnalysisTest;
import java.io.IOException;

/**
 * 
 * @author moshiko
 * @since  05-04-2017
 * 
 * 
 */
import org.junit.Test;
import Analysis.AnalyzeParagraph;
import WikiData.WikiParsing;
public class AnalyzeParagraphTest {
	
	@Test
	public void Test1() throws IOException{
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/Axl_Rose"));
		wiki.Parse("arrested");
		(new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();
		
	}
	
	@Test
	public void Test2() throws IOException{
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
		wiki.Parse("arrested");
		(new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();
		
	}
	
	@Test
	public void Test3() throws IOException{
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/Shia_LaBeouf"));
		wiki.Parse("arrested");
		(new AnalyzeParagraph(wiki.getParagraphs())).AnalyzeArrestsQuery();
		
	}
	
	@Test
	public void Test4() throws IOException{
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/Adele"));
		wiki.Parse("won");
		AnalyzeParagraph an = new AnalyzeParagraph(wiki.getParagraphs());
		an.AnalyzeAwardsQuery();
		for (final String ¢ : an.getInformation())
			System.out.println(¢);
		}
}
