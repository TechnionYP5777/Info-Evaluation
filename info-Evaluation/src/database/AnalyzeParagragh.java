package database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.stanford.nlp.simple.*;

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
	
	public tableTuple Analyze (){
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new tableTuple(name, date1, "b");
	}

}
