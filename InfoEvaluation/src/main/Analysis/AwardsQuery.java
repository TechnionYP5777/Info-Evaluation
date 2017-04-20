package Analysis;

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
	 
	

	public AwardsQuery(Elements paragraphs) {
		 // Create a CoreNLP document
	    // Iterate over the sentences in the document
		  for (final Element paragraph : paragraphs)
			for (Sentence sent : (new Document((paragraph.text() + ""))).sentences())
				for (RelationTriple ¢ : sent.openieTriples())
					System.out.println(¢.confidence + "\t" + ¢.subjectLemmaGloss() + "\t" + ¢.relationLemmaGloss()
							+ "\t" + ¢.objectLemmaGloss());
	}

}
