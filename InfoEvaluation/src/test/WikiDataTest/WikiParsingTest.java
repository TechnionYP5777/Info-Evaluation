package WikiDataTest;
import java.io.IOException;

/**
 * 
 * @author moshiko
 * @since  05-04-2017
 * 
 * 
 */
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class WikiParsingTest {
	
	@Test
	public void test1() throws IOException{
		  String url = "http://en.wikipedia.org/wiki/New_York_City";
		    Document doc = Jsoup.connect(url).get();
		    Elements paragraphs = doc.select(".mw-content-ltr p, .mw-content-ltr li");

		    Element firstParagraph = paragraphs.first();
		    Element lastParagraph = paragraphs.last();
		    Element p;
		    int i = 1;
		    p = firstParagraph;
		    System.out.println(p.text());
		    while (p != lastParagraph) {
		        p = paragraphs.get(i);
		        System.out.println(p.text());
		        i++;
		    }
		
	}
	
	

}
