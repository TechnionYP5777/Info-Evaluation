package infoeval.main.Analysis;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.simple.Document;
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
	LinkedList<String> Information; // will hold the information about the
									// query.
	final StanfordCoreNLP pipeLine;

	public AnalyzeParagraph(Elements Paragraphs) throws IOException {
		this.Paragraphs = Paragraphs;
		this.Information = new LinkedList<String>();
		final Properties props = new Properties();
		props.put("annotators", "tokenize,ssplit, pos ,parse,lemma,natlog,openie");
		// props.put("ner.model",
		// "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
		// props.put("ner.model",
		// "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
		this.pipeLine = new StanfordCoreNLP(props);
		//LoadClassifiers();

	}

	public AnalyzeParagraph() throws IOException {
		this.Paragraphs = new Elements();
		this.Information = new LinkedList<String>();
		final Properties props = new Properties();
		props.put("annotators", "tokenize,ssplit, pos,parse,lemma,natlog,openie");
		// props.put("ner.model",
		// "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
		// props.put("ner.model",
		// "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
		this.pipeLine = new StanfordCoreNLP(props);
		//LoadClassifiers();

	}

	public void setParagraphs(Elements Paragraphs) {
		if (!this.Paragraphs.isEmpty())
			this.Paragraphs.empty();
		this.Paragraphs = Paragraphs;
		if (!this.Information.isEmpty())
			this.Information.clear();
	}

	public void LoadNLPClassifiers() throws IOException {
		//Load coreNLP classifiers
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/The_Weeknd"));
		wiki.Parse("arrested");
		setParagraphs(wiki.getParagraphs());
		AnalyzeArrestsQuery();
	}
	
	public void LoadIEClassifiers() throws IOException{
		//Load openIE classifiers
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/Adele"));
		wiki.Parse("won");
		setParagraphs(wiki.getParagraphs());
		AwardsQuery();
	}

	public void AnalyzeAwardsQuery() {
		AwardsQuery aw = new AwardsQuery(this.Paragraphs);
		aw.analyze();
		this.Information = aw.getInformation();
	}

	public void AwardsQuery() {
		System.out.println("Started analyzing awards query");
		// Annotate an example document.
		for (final Element paragraph : this.Paragraphs) {
			for(final String sent : paragraph.text().split("\\.")){
				if(!(sent.contains("won") || sent.contains("award") || sent.contains("awarded") || sent.equals("recieved")))
					continue;
					
			Annotation doc = new Annotation(sent);
			pipeLine.annotate(doc);
			// Loop over sentences in the document
			for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class))
				//Information.add(sentence.toShorterString("PartOfSpeech"));
				for (RelationTriple ¢ : sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class)) {
					String rel = ¢.relationLemmaGloss();
					if (rel.contains("award") || rel.contains("win") || ¢.objectLemmaGloss().contains("award")){
						Information.add(¢.confidence + "\t" + ¢.subjectGloss() + "\t" + ¢.relationGloss()
								+ "\t" + ¢.objectGloss());
					break;
					}
				}
		}
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

	public LinkedList<String> RefineResults(int limit) {
		// Take only the longest #limit results.
		LinkedList<String> $ = new LinkedList<String>(this.Information);
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
					this.Information.add(prefixDetails.trim());
					System.out.println((this.Information.get(index) + ""));
					++index;
				}
				this.Information.add((reason + " " + details).trim());
				System.out.println((this.Information.get(index) + ""));
				++index;
			}
		}
	}

	public LinkedList<String> getInformation() {
		return this.Information;
	}

}
