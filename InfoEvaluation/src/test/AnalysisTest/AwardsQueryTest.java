package AnalysisTest;
import java.io.IOException;
import org.junit.Test;
import Analysis.AwardsQuery;
/**
 * 
 * 
 * @author moshiko
 * since 20-04/-017
 */
import WikiData.WikiParsing;

public class AwardsQueryTest {
	
	@Test
	public void test1() throws IOException{
		WikiParsing wiki = new WikiParsing("https://en.wikipedia.org/wiki/Justin_Timberlake");
		wiki.Parse("awarded");
		System.out.print(wiki.getText());
	 AwardsQuery aw =  new AwardsQuery(wiki.getParagraphs());
	 aw.analyze();
	 for (final String ¢ : aw.getInformation())
		System.out.println(¢);
	}
}
