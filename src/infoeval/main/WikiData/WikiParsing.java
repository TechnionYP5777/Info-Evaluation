package infoeval.main.WikiData;

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
String doc;

public WikiParsing(String URL){
	this.url = URL;
	this.parsedText="";
	this.doc="";
}

public String getURL(){
	return this.url;
}

public String getText(){
	return this.parsedText;
}
public String getDoc(){
	return this.doc;
}

public Elements getParagraphs(){
	return this.parsedParagraphs;
}

public String Parse(String filter) throws IOException{
	Document doc = Jsoup.connect(this.url).get();
	 this.parsedParagraphs = doc.select("p:contains"+"("+filter+")");
	 this.doc=doc.toString();
	 return this.parsedText = this.parsedParagraphs.text() + "";
	
}
public boolean isConflictedName(){
	if(this.doc.contains("Human name disambiguation pages")){
		return true;
	}
	return false;
}

}
