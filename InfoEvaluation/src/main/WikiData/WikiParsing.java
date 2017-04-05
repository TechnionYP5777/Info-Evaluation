package WikiData;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * @author moshiko
 * @since  05-04-2017
 * 
 * 
 */
public class WikiParsing {
String url;
String parsedText;
Elements parsedParagraphs;

public WikiParsing(String URL){
	this.url = URL;
	this.parsedText="";
}

public String getURL(){
	return this.url;
}

public String getText(){
	return this.parsedText;
}

public String Parse(String filter) throws IOException{
	Document doc = Jsoup.connect(this.url).get();
	System.out.println("hi");
	 this.parsedParagraphs = doc.select("p:contains"+"("+filter+")");
	 this.parsedText = this.parsedParagraphs.text().toString();
	 return this.parsedText;
}

}
