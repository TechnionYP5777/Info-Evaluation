package infoeval.main.Analysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import infoeval.main.WikiData.WikiParsing;

/**
 * @author moshiko
 * @since 05-04-2017
 * 
 */

public class AnalyzeParagraph {
	Elements Paragraphs;
	
	LinkedList<String> dynamicInformation;
	LinkedList<String> arrestsInformation;
	LinkedList<String> awardsInformation;
	final StanfordCoreNLP pipeLine;

	public AnalyzeParagraph(Elements Paragraphs) throws IOException {
		this.Paragraphs = new Elements();
		this.Paragraphs = Paragraphs;
		this.dynamicInformation= new LinkedList<String>();
		this.arrestsInformation=new LinkedList<String>();
		this.awardsInformation=new LinkedList<String>();
		final Properties props = new Properties();
		props.put("annotators", "tokenize,ssplit, pos ,parse,lemma");
		this.pipeLine = new StanfordCoreNLP(props);
	}

	public AnalyzeParagraph() throws IOException {
		this.Paragraphs = new Elements();
		
		this.dynamicInformation= new LinkedList<String>();
		this.arrestsInformation=new LinkedList<String>();
		this.awardsInformation=new LinkedList<String>();
		final Properties props = new Properties();
		props.put("annotators", "tokenize,ssplit, pos,parse,lemma");
		this.pipeLine = new StanfordCoreNLP(props);
	}

	public void setParagraphs(Elements Paragraphs) {
		if (!this.Paragraphs.isEmpty())
			this.Paragraphs.empty();
		this.Paragraphs = Paragraphs;
		
	}
	

	public void clearDynamicInformation(){
		if (!this.dynamicInformation.isEmpty())
			this.dynamicInformation.clear();
	}
	public void clearArrestsInformation(){
		if (!this.arrestsInformation.isEmpty())
			this.arrestsInformation.clear();
	}
	public void clearAwardsInformation(){
		if (!this.awardsInformation.isEmpty())
			this.awardsInformation.clear();
	}

	public void LoadNLPClassifiers() throws IOException {
		// Load coreNLP classifiers
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
		wiki.Parse("arrested");
		setParagraphs(wiki.getParagraphs());
		clearArrestsInformation();
		AnalyzeArrestsQuery();
		clearArrestsInformation();
	}

	public void LoadIEClassifiers() throws IOException {
		// Load openIE classifiers
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Adele"));
		wiki.Parse("won");
		setParagraphs(wiki.getParagraphs());
		clearAwardsInformation();
		AwardsQuery();
		clearAwardsInformation();
	}

	public static boolean containsItemFromArray(String inputString, String[] items) {
		// Convert the array of String items as a Stream
		// For each element of the Stream call inputString.contains(element)
		// If you have any match returns true, false otherwise
		return Arrays.stream(items).anyMatch(inputString::contains);
	}

	public void dynamicQuery(String name, String query) throws Exception {
		clearDynamicInformation();
		try {
			// Prepare list of similar words to the query
			LinkedList<String> keywords = new LinkedList<String>();
			keywords.add(query);
			name = name.trim().replaceAll(" ", "_").toLowerCase();
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + name));
	

			Annotation doc = new Annotation(query);
			this.pipeLine.annotate(doc);
			for (CoreMap sentence : doc.get(SentencesAnnotation.class))
				for (CoreLabel token : sentence.get(TokensAnnotation.class))
					keywords.add(token.get(LemmaAnnotation.class));

			// The query itself
			for(String queryWord : keywords){
				wiki.Parse(queryWord);
				System.out.println(wiki.getParagraphs().text());
				setParagraphs(wiki.getParagraphs());
			for (final Element paragraph : this.Paragraphs)
				for (String sent : paragraph.text().split("\\.")) { // Split to
																	// sentences.

					boolean hasTerm = false;
					for (String word : sent.split("\\s+"))
						for (String keyword : keywords)
							if (word.equals(keyword)) {
								hasTerm = true;
								break;
							}

					if (!hasTerm)
						continue;

					sent = sent.replaceAll("\\[\\d+\\]", "");
					this.dynamicInformation.add(sent);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void AwardsQuery() {
		System.out.println("Started analyzing awards query");
		for (final Element paragraph : this.Paragraphs)
			for (String sent : paragraph.text().split("\\.")) {
				if (!sent.contains("won") && !sent.contains("award") && !sent.contains("awarded")
						&& !"recieved".equals(sent))
					continue;
				sent = sent.replaceAll("\\[\\d+\\]", "");
				this.awardsInformation.add(sent);
			}
	}

	private String ArrestPenalty(Sentence s) {
		// This function gets a sentence which may include the penalty for an
		// arrest and returns it.
		// e.g. - 'He was sentenced to 2 years in jail' and the function returns
		// '2 years in jail' .
		final List<String> nerTags = s.nerTags();
		String res = "";
		int i = 0;
		for (final String ¢ : nerTags) {
			if ("DURATION".equals(¢))
				res += s.word(i) + " ";
			++i;
		}
		System.out.println(res);
		return res.trim();
	}

	static String getShortestString(LinkedList<String> refined_lst) {
		int maxLength = refined_lst.get(0).length();
		String $ = null;
		for (int i = 0; i < refined_lst.size(); ++i) {
			String s = refined_lst.get(i);
			if (s.length() <= maxLength) {
				maxLength = s.length();
				$ = s;
			}
		}
		return $;
	}

	public LinkedList<String> RefineResults(int limit, LinkedList<String> info) {
		// Take only the longest #limit results.
		LinkedList<String> $ = new LinkedList<String>(info);
		System.out.println($.size());
		while ($.size() > limit)
			$.remove(getShortestString($));
		return $;
	}

	public void AnalyzeArrestsQuery() {
		/*
		 * First step is initiating the Stanford CoreNLP pipeline (the pipeline
		 * will be later used to evaluate the text and annotate it) Pipeline is
		 * initiated using a Properties object which is used for setting all
		 * needed entities, annotations, training data and so on, in order to
		 * customized the pipeline initialization to contains only the models
		 * you need
		 */
		// final Properties props = new Properties();

		/*
		 * The "annotators" property key tells the pipeline which entities
		 * should be initiated with our pipeline object, See
		 * http://nlp.stanford.edu/software/corenlp.shtml for a complete
		 * reference to the "annotators" values you can set here and what they
		 * will contribute to the analyzing process
		 */
		// props.put("annotators", "tokenize,ssplit, pos, regexner,
		// parse,lemma,natlog,openie");
		// final StanfordCoreNLP pipeLine = new StanfordCoreNLP(props);

		// inputText will be the text to evaluate in this example
		int index = 0;
		for (final Element paragraph : this.Paragraphs) {
			final String inputText = paragraph.text() + "";
			final Annotation document = new Annotation(inputText);
			System.out.println(document);

			String reason = "";
			String details = ""; // more details about the reason. e.g - where
									// it
									// happened.
			String aux = "";
			String prefixDetails = "";
			String penalty = ""; // this string tells us what is the penalty for
									// the arrest.

			// Finally we use the pipeline to annotate the document we created
			pipeLine.annotate(document);
			for (final CoreMap sentence : document.get(SentencesAnnotation.class)) {
				Sentence sent = new Sentence(sentence);
				if (sent.text().contains("sentenced") || sent.text().contains("juried")
						|| sent.text().contains("sent to jail") || sent.text().contains("charged")) {
					penalty = ArrestPenalty(sent);
					System.out.println("Sentenced for:" + penalty);
				}
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

							// Finding the reason in the paragraph
							if ("advcl".equals(rel) || "advcl:for".equals(rel) || "nmod:for".equals(rel)) {
								for (final SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep)) {
									final String rel2 = keshet.getRelation() + "";
									final IndexedWord dep2 = keshet.getDependent();
									if ("amod".equals(rel2) || "dobj".equals(rel2)) {
										reason += dep2.word() + " ";
										try {
											prefixDetails = ((sentence + "").substring(dep.beginPosition(),
													dep2.endPosition()));
										} catch (IndexOutOfBoundsException e) {
											prefixDetails = sentence + "";
										}
									}
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
				if (!"".equals(prefixDetails.trim())) {
					this.arrestsInformation.add(prefixDetails.trim());
					
					++index;
				}
				this.arrestsInformation.add((reason + " " + details).trim());
				
				++index;
			}
		}
	}

	
	public LinkedList<String> getDynamicInformation() {
		return this.dynamicInformation;
	}
	public LinkedList<String> getArrestsInformation() {
		return this.arrestsInformation;
	}
	public LinkedList<String> getAwardsInformation() {
		return this.awardsInformation;
	}

}
