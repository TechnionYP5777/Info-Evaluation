package main.Analyze;

import main.database.*;
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
import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.util.CoreMap;

/**
 * 
 * @author MosheEliasof 
 *
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
	
	private Date getDate(){	
		List<String> nerTags = this.input.nerTags(); 
		int i=0;
		String date="";
		for (String elem : nerTags){
			if("DATE".equals(elem))
				date += covertMonth(this.input.word(i)) + " ";
			++i;
			
		}
		DateFormat format = new SimpleDateFormat("MMMM dd , yyyy", Locale.ENGLISH);
		Date $ = null;
		try {
			$ = format.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block @Genia
			e.printStackTrace();
		}
		return $;
	}
	
	private String getName(){
		List<String> nerTags = this.input.nerTags(); 
		String name = "";
		int i=0;
		
		for (String elem : nerTags){
			if("PERSON".equals(elem))
				name += this.input.word(i) + " ";
			++i;	
		}
		
	
		return name.trim();
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
		props.put( "annotators", "tokenize,ssplit, pos, regexner, parse" );
		StanfordCoreNLP pipeLine = new StanfordCoreNLP( props );
		
		
	
		// inputText will be the text to evaluate in this example
		String inputText = this.input + "";
		Annotation document = new Annotation( inputText );
		

		// Finally we use the pipeline to annotate the document we created
		pipeLine.annotate( document );
		String name=getName();
		Date input_date = getDate();
		String reason="";
		
		
		for (CoreMap sentence : document.get(SentencesAnnotation.class)) {		
		SemanticGraph dependencies = sentence.get(CollapsedDependenciesAnnotation.class);
		for (IndexedWord root : dependencies.getRoots())
			for (SemanticGraphEdge edge : dependencies.getOutEdgesSorted(root)) {
				IndexedWord dep = edge.getDependent();
				if ("arrested".equals(edge.getGovernor().word()) && "nmod:for".equals((edge.getRelation() + ""))
						&& "nmod:for".equals((edge.getRelation() + ""))) {
					for (SemanticGraphEdge keshet : dependencies.getOutEdgesSorted(dep))
						if ("amod".equals((keshet.getRelation() + "")))
							reason += keshet.getDependent().word() + " ";
					reason += dep.word();
				}
			}
	}
		return new TableTuple(name,input_date,reason);
   } 
	
	//EOF
}

