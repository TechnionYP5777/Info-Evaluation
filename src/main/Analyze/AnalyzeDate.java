package main.Analyze;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeDate {
	String date;
	String format;

	public AnalyzeDate(String d) {
		date = d;
		format=findFormat();
	}

	private String findFormat() {
		// TODO: implement function
		boolean success=true;
		
		Date regularDate = null;
		
		//first format
		try {
			regularDate = (new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)).parse(date);
		} catch (Exception e) {
			success=false;
		}
		if(success)
			return "MM/dd/yyyy";
		success=true;
		
		//second format
		try {
			regularDate = (new SimpleDateFormat("MMM. dd, yyyy", Locale.ENGLISH)).parse(date);
		} catch (Exception e) {
			success=false;
		}
		if(success)
			return "MMM. dd, yyyy";
		success=true;
		
		// third format
		try {
			regularDate = (new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)).parse(date);
		} catch (Exception e) {
			success=false;
		}
		if(success)
			return "MMM dd, yyyy";
		success=true;
		
		// forth format
		try {
			regularDate = (new SimpleDateFormat(("MMMM dd'" + getDayOfMonthSuffix(getDay(date)) + "', yyyy"),
					Locale.ENGLISH)).parse(date);
		} catch (Exception e) {
			success=false;
		}
		if(success)
			return "MMMM dd'"+getDayOfMonthSuffix(getDay(date))+"', yyyy";
		success=true;
		
		
		return null; //none of the formats we work with
	}

	public String getFormat() {
		return format;
	}

	public Date getDateObj() {
		// TODO: implement function
		return null;
	}

	private int getDay(String s) {
		Pattern pattern = Pattern.compile("\\s([A-Za-z]+)");
		Matcher matcher = pattern.matcher(s);
		return !matcher.find() ? -1 : Integer.parseInt(matcher.group(1));
	}
	
	private String getDayOfMonthSuffix(int day) {
		switch (day) {
		case 1:
		case 21:
		case 31:
			return ("st");

		case 2:
		case 22:
			return ("nd");

		case 3:
		case 23:
			return ("rd");

		default:
			return ("th");
		}
	}

}
