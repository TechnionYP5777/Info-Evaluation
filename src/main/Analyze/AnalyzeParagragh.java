package main.Analyze;

import main.database.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.util.CoreMap;

/**
 * [[SuppressWarningsSpartan]]
 */
public class AnalyzeParagragh {
	private Sentence input;
	
	public AnalyzeParagragh(Sentence input){
		if(input!=null)
		this.input = new Sentence(input + "");
	}
	
	//No May case cause it has no short version 
	private String covertMonth(String month) {
		switch(month) {
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
	
	public TableTuple AnalyzeSimple (){
		
		List<String> nerTags = this.input.nerTags(); 
		String name = "";
		int i=0;
		String date="";
		for (String elem : nerTags){
			if("PERSON".equals(elem))
				name += this.input.word(i) + " ";
			if("DATE".equals(elem))
				date += covertMonth(this.input.word(i)) + " ";
			++i;
			
		}
		DateFormat format = new SimpleDateFormat("MMMM d", Locale.ENGLISH);
		Date date1 = null;
		try {
			date1 = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block @Genia
			e.printStackTrace();
		}
		return new TableTuple(name, date1, "b");
	}
	
	public TableTuple Analyze(){
		/* First step is initiating the Stanford CoreNLP pipeline 
		   (the pipeline will be later used to evaluate the text and annotate it)
		   Pipeline is initiated using a Properties object which is used for setting all needed entities, 
		   annotations, training data and so on, in order to customized the pipeline initialization to 
		   contains only the models you need */
		Properties props = new Properties();

		/* The "annotators" property key tells the pipeline which entities should be initiated with our
		     pipeline object, See http://nlp.stanford.edu/software/corenlp.shtml for a complete reference 
		     to the "annotators" values you can set here and what they will contribute to the analyzing process  */
		props.put( "annotators", "tokenize, ssplit, pos, lemma, ner, regexner, parse, dcoref" );
		StanfordCoreNLP pipeLine = new StanfordCoreNLP( props );
		
		
		// Next we generate an annotation object that we will use to annotate the text with
		SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
		String currentTime = formatter.format( System.currentTimeMillis() );
		
		// inputText will be the text to evaluate in this example
		String inputText = this.input + "";
		Annotation document = new Annotation( inputText );
		document.set( CoreAnnotations.DocDateAnnotation.class, currentTime );

		// Finally we use the pipeline to annotate the document we created
		pipeLine.annotate( document );
		String name="";
		String input_date = "" ;
		String reason="";
		
		
		for (CoreMap sentence : document.get(SentencesAnnotation.class)) {		
		for (CoreLabel token : sentence.get(TokensAnnotation.class)){
		//find the date of the incident
		if("DATE".equals(token.getString(NamedEntityTagAnnotation.class)))
			input_date += token.get(CoreAnnotations.LemmaAnnotation.class) + " ";
			
		}
		
		SemanticGraph dependencies = sentence.get(CollapsedDependenciesAnnotation.class);
		Collection<IndexedWord> roots = dependencies.getRoots();
		for(IndexedWord root : roots){
		for (SemanticGraphEdge edge : dependencies.getOutEdgesSorted(root)) {
			IndexedWord dep = edge.getDependent();
			IndexedWord gov = edge.getGovernor();
	
			if("arrested".equals(gov.word())){			
				if("PERSON".equals(dep.ner())){
					name=dep.word()+" ";	
					for (SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep)) {
						IndexedWord dep2 = keshet.getDependent();
						if(dep2.ner().toString().equals("PERSON"))
							name+=dep2.word().toString()+" ";
					}
					
					}
				
				else if("nmod:for".equals((edge.getRelation() + ""))){
					for (SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep)) {
						IndexedWord dep2 = keshet.getDependent();
						if("amod".equals((keshet.getRelation() + "")))
							reason+=dep2.word()+" ";
					}
					reason+=dep.word();
					
				}
				
				}
			}
			
		}
	}
		return new TableTuple(name,input_date,reason);
   } 
	
	//EOF
}

