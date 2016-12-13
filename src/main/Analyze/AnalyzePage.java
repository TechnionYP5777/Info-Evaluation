package main.Analyze;
import main.database.DataList;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class AnalyzePage {
	private String originalText;
	private List<String> paragraphs;
	private DataList details;
	//GENIA: I changed the details to be from type DataList (Netanel)

	public AnalyzePage(String text) {
		originalText = text;
		paragraphs = createParagraphs();
		details=detectDetails();
	}

	private List<String> createParagraphs() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit");
		props.setProperty("ssplit.eolonly", "true");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document1 = new Annotation(originalText);
		pipeline.annotate(document1);
		List<CoreMap> sentences = document1.get(CoreAnnotations.SentencesAnnotation.class);
		List<String> $ = new ArrayList<>();
		for (CoreMap ¢ : sentences)
			$.add((¢ + ""));
		return $;
	}
	
	public List<String> getParagraphs() {
		return paragraphs;
	}

	private DataList detectDetails() {
		return null;
	}

	public DataList getDetails() {
		return details;
	}
}
