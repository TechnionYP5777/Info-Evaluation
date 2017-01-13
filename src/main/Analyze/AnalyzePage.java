package main.Analyze;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import main.database.DataList;

public class AnalyzePage {
	private final String originalText;
	private final List<String> paragraphs;
	private final DataList details;
	private String year;


	public AnalyzePage(final String text, String year) {
		originalText = text;
		paragraphs = createParagraphs();
		details = new DataList();
		this.year=year;
		detectDetails();
	}
	
	public AnalyzePage(final String text) {
		originalText = text;
		paragraphs = createParagraphs();
		details = new DataList();
		this.year=Integer.toString(Calendar.getInstance().get(Calendar.YEAR)-1);
		detectDetails();
	}

	private List<String> createParagraphs() {
		final Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit");
		props.setProperty("ssplit.eolonly", "true");
		final StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		final Annotation document1 = new Annotation(originalText);
		pipeline.annotate(document1);
		final List<CoreMap> sentences = document1.get(CoreAnnotations.SentencesAnnotation.class);
		final List<String> $ = new ArrayList<>();
		for (final CoreMap ¢ : sentences)
			$.add(¢ + "");
		return $;
	}

	public List<String> getParagraphs() {
		return paragraphs;
	}

	public List<Sentence> getSentenceParagraphs() {
		final List<Sentence> $ = new ArrayList<>();
		for (final String ¢ : paragraphs)
			$.add(new Sentence(¢));
		return $;
	}

	private void detectDetails() {
		for (final Sentence ¢ : getSentenceParagraphs())
			details.insert(new AnalyzeParagragh(¢, year).Analyze());
	}

	public DataList getDetails() {
		return details;
	}
}
