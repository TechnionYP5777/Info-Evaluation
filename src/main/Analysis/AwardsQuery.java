package main.Analysis;

import java.util.LinkedList;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.simple.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * 
 * @author moshiko
 * since 20-04/-017
 */

public class AwardsQuery {
	Elements Paragraphs;
	LinkedList<String> Information; // will hold the information about the query.
	
	public AwardsQuery(Elements paragraphs){
		this.Paragraphs = paragraphs;
		this.Information=new LinkedList<>();
	}

	public void analyze() {
		 // Create a CoreNLP document
	    // Iterate over the sentences in the document
		  for (final Element paragraph : this.Paragraphs)
			for (Sentence sent : (new Document((paragraph.text() + ""))).sentences())
				for (RelationTriple ¢ : sent.openieTriples()){
					 String rel = ¢.relationLemmaGloss();
					if (rel.contains("award") || rel.contains("win") || ¢.objectLemmaGloss().contains("award"))
						Information.add(¢.confidence + "\t" + ¢.subjectLemmaGloss() + "\t" + ¢.relationLemmaGloss()
								+ "\t" + ¢.objectLemmaGloss());
				}
		  
	}
	
	
	public LinkedList<String> getInformation(){
		return this.Information;
	}
	
	public Elements getParagraphs(){
		return this.Paragraphs;
	}

}
