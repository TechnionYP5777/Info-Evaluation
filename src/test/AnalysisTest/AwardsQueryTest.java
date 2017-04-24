package test.AnalysisTest;
import java.io.IOException;
import org.junit.Test;
import main.Analysis.AwardsQuery;
/**
 * 
 * 
 * @author moshiko
 * since 20-04/-017
 */
import main.WikiData.WikiParsing;

public class AwardsQueryTest {
	
	@Test
	public void test1() throws IOException{
		WikiParsing wiki = new WikiParsing("https://en.wikipedia.org/wiki/Taylor_Swift");
		wiki.Parse("awarded");
		System.out.print(wiki.getText());
	 main.Analysis.AwardsQuery aw =  new AwardsQuery(wiki.getParagraphs());
	 aw.analyze();
	 for (final String ¢ : aw.getInformation())
		System.out.println(¢);
	}
}
