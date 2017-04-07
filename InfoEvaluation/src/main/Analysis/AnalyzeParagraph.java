package Analysis;

import java.util.LinkedList;
import java.util.Properties;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

/** 
 * @author moshiko
 * @since  05-04-2017
 * 
 */

public class AnalyzeParagraph {
 Elements Paragraphs;
 LinkedList<String> Information; // will hold the information about the query.
 
 public AnalyzeParagraph(Elements Paragraphs){
	 this.Paragraphs = Paragraphs;
	 this.Information = new LinkedList<String>();
 }
 
 public void AnalyzeArrestsQuery(){
	 /*
		 * First step is initiating the Stanford CoreNLP pipeline (the pipeline
		 * will be later used to evaluate the text and annotate it) Pipeline is
		 * initiated using a Properties object which is used for setting all
		 * needed entities, annotations, training data and so on, in order to
		 * customized the pipeline initialization to contains only the models
		 * you need
		 */
		final Properties props = new Properties();

		/*
		 * The "annotators" property key tells the pipeline which entities
		 * should be initiated with our pipeline object, See
		 * http://nlp.stanford.edu/software/corenlp.shtml for a complete
		 * reference to the "annotators" values you can set here and what they
		 * will contribute to the analyzing process
		 */
		props.put("annotators", "tokenize,ssplit, pos, regexner, parse,lemma,natlog,openie");
		final StanfordCoreNLP pipeLine = new StanfordCoreNLP(props);

		// inputText will be the text to evaluate in this example
		int index = 0;
		for (final Element paragraph : this.Paragraphs){
		final String inputText = paragraph.text() + "";
		final Annotation document = new Annotation(inputText);
		String reason = "";
		String details = ""; // more details about the reason. e.g - where it
								// happened.
		String aux = "";
		// Finally we use the pipeline to annotate the document we created
		pipeLine.annotate(document);
		for (final CoreMap sentence : document.get(SentencesAnnotation.class)) {
			final SemanticGraph dependencies = sentence.get(CollapsedDependenciesAnnotation.class);
			for (final IndexedWord root : dependencies.getRoots())
				for (final SemanticGraphEdge edge : dependencies.getOutEdgesSorted(root)) {
					final IndexedWord dep = edge.getDependent();
					final String rel = edge.getRelation() + "";
					if (!"arrested".equals(edge.getGovernor().word()))
						switch (rel) {
						case "nmod:in":
							details += "in" + " " + dep.word() + " ";
							break;
						case "nmod:during":
							details += "during" + " " + dep.word() + " ";
							break;
						case "nmod:at":
							details += "at" + " " + dep.word() + " ";
							break;
						}
					else {
						
						//Finding the reason in the paragraph
						if ("advcl".equals(rel) || "advcl:for".equals(rel) || "nmod:for".equals(rel)) {
						for (final SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep)) {
							final String rel2 = keshet.getRelation() + "";
							final IndexedWord dep2 = keshet.getDependent();
							if ("amod".equals(rel2) || "dobj".equals(rel2))
								reason += dep2.word() + " ";
							if ("xcomp".equals(rel2))
								aux += " " + dep2.word();
							switch (rel2) {
							case "nmod:in":
								final String longLocation = dep2.word();
								details += "in ";
								for (final SemanticGraphEdge keshet2 : dependencies.getOutEdgesSorted(dep2))
									if ("compound".equals(keshet2.getRelation() + ""))
										details += keshet2.getDependent().word() + " ";
								details += longLocation;
								break;
							case "nmod:during":
								details += "during" + " " + dep2.word() + " ";
								break;
							case "nmod:under":
								details += "under " + dep2.word() + " ";
								break;
							case "nmod:of":
								details += "of " + dep2.word();
								break;
							case "nmod:at":
								details += "at" + " " + dep2.word() + " ";
								break;
							}

							if ("suspicion".equals(keshet.getSource().word()) && "acl:of".equals(rel2))
								details += dep2.word();
						}
						reason += dep.word();
						reason += aux;
					}
					
				}
				}
			//Set reason in list of possible reasons ( could be there's only one reason found)
			this.Information.add((reason + " " + details).trim());
			System.out.println((reason + " " + details).trim());
			System.out.println((this.Information.get(index) + ""));
		}
		  ++index;
		  
		}
 }

}
