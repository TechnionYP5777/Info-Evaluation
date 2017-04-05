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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class WikiParsingTest {
	
	@Test
	public void test1() throws IOException{
		  Elements paragraphs = Jsoup.connect("http://en.wikipedia.org/wiki/New_York_City").get().select(".mw-content-ltr p, .mw-content-ltr li");

		    Element firstParagraph = paragraphs.first(), lastParagraph = paragraphs.last(), p;
		    int i = 1;
		    p = firstParagraph;
		    System.out.println(p.text());
		    for (; p != lastParagraph; ++i) {
				p = paragraphs.get(i);
				System.out.println(p.text());
			}
		
	}
	
	

}
