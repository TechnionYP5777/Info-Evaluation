package infoeval.main.WikiData;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author moshiko
 * @since 05-04-2017
 * 
 * 
 */
public class WikiParsing {
	String url;
	String parsedText;
	Elements parsedParagraphs;
	String parsedDoc;
	ArrayList<String> names;
	Document doc;
	
	public WikiParsing(String URL) throws IOException {
		this.url = URL;
		this.doc = Jsoup.connect(this.url).get();
		this.parsedDoc = this.parsedText = "";
		this.names = new ArrayList<>();
	}

	public String getURL() {
		return this.url;
	}

	public String getParsedDoc() {
		return this.parsedDoc;
	}

	public String getText() {
		return this.parsedText;
	}

	public Elements getParagraphs() {
		return this.parsedParagraphs;
	}

	public ArrayList<String> getNames() {
		return this.names;
	}

	public String Parse(String filter) throws IOException {
		this.parsedParagraphs = this.doc.select("p:contains" + "(" + filter + ")");

//		Element contentDiv = doc.select("div[id=content]").first();
//		this.parsedDoc = contentDiv.text();
//
//		Elements elements = doc.select("p ~ ul a:eq(0)");
//
//		for (Element ¢ : elements)
//			names.add(¢.text());

		return this.parsedText = this.parsedParagraphs.text() + "";

	}
	
	public void CheckAmbiguities() throws IOException{
		Element contentDiv = this.doc.select("div[id=content]").first();
		this.parsedDoc = contentDiv.text();

		for (Element ¢ : this.doc.select("p ~ ul a:eq(0)"))
			names.add(¢.text());

	}

	public boolean isConflictedName() throws IOException {
		Element contentDiv = this.doc.select("div[id=content]").first();
		this.parsedDoc = contentDiv.text();
		return this.parsedDoc.contains("disambiguation page");
	}

}
