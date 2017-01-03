package main.Analyze;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import main.database.TableTuple;

/**
 *
 * @author MosheEliasof
 *
 */

public class AnalyzeParagragh {
	private Sentence input;
	private String year;

	public AnalyzeParagragh(final Sentence input, final String year) {
		if (input != null)
			this.input = new Sentence(input + "");
		if (!year.isEmpty())
			this.year = year;
	}

	// No May case cause it has no short version
	private String covertMonth(final String month) {
		switch (month) {
		case "Jan.":
			return "January";
		case "Feb.":
			return "February";
		case "Mar.":
			return "March";
		case "Apr.":
			return "April";
		case "Jun.":
			return "June";
		case "Jul.":
			return "July";
		case "Aug.":
			return "August";
		case "Sep.":
			return "September";
		case "Oct.":
			return "October";
		case "Nov.":
			return "November";
		case "Dec.":
			return "December";

		}
		return month;
	}
	
	
	public TableTuple AnalyzeSimple() {

		final List<String> nerTags = input.nerTags();
		String $ = "";
		int i = 0;
		String date = "";
		for (final String elem : nerTags) {
			if ("PERSON".equals(elem))
				$ += input.word(i) + " ";
			if ("DATE".equals(elem))
				date += covertMonth(input.word(i)) + " ";
			++i;

		}
		final DateFormat format = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
		Date date1 = null;
		try {
			date1 = format.parse(date);
		} catch (final ParseException ¢) {
			// TODO Auto-generated catch block @Genia
			¢.printStackTrace();
		}
		return new TableTuple($, date1, "b");
	}

	private String getDate(final String year) {
		final List<String> nerTags = input.nerTags();
		int i = 0, j = 0;
		String $ = "";
		for (final String elem : nerTags) {
			if ("DATE".equals(elem)) {
				$ += input.word(i) + " ";
				++j;
			}
			++i;

		}
		if (j == 2)
			$ += this.year + " ";
		
		return new SimpleDateFormat("MM/dd/yyyy").format(new AnalyzeDate($).getDateObj());
	}

	private String getName() {
		final List<String> nerTags = input.nerTags();
		String $ = "";
		int i = 0;

		for (final String elem : nerTags) {
			if ("PERSON".equals(elem))
				$ += input.word(i) + " ";
			++i;
		}

		return $.trim();
	}

	public TableTuple Analyze() {
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
		props.put("annotators", "tokenize,ssplit, pos, regexner, parse");
		final StanfordCoreNLP pipeLine = new StanfordCoreNLP(props);

		// inputText will be the text to evaluate in this example
		final String inputText = input + "";
		final Annotation document = new Annotation(inputText);

		// Finally we use the pipeline to annotate the document we created
		pipeLine.annotate(document);
		final String $ = getName();
		final String input_date = getDate(year);
		String reason = "";
		String details = ""; // more details about the reason. e.g - where it
								// happened.
		String aux = "";
		String accurate_name="";
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
						
						//Finding the name in a more accurate manner:
						if("nsubjpass".equals(rel)){	
							for (final SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep)){
								final IndexedWord dep2 = keshet.getDependent();
								final String rel2 = keshet.getRelation() + "";
								if((dep2.ner()!=null &&"PERSON".equals(dep2.ner())) || "compound".equals(rel2) || "det".equals(rel2) )
									accurate_name += dep2.word() + " ";
							}
							accurate_name+=dep.word();
						}
						
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

		}

		return new TableTuple(accurate_name.isEmpty()?$:accurate_name, input_date, (reason + " " + details).trim());
	}

	// EOF
}
