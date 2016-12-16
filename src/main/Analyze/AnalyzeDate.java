package main.Analyze;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyzeDate {
	String date;
	String format;
	List<String> popularFormats;

	public AnalyzeDate(String d) {
		date = d;
		
		popularFormats=new ArrayList<>();
		popularFormats.add("MM/dd/yyyy");
		popularFormats.add("MMM. dd, yyyy");
		popularFormats.add("MMM dd, yyyy");
		popularFormats.add("MMMM dd'" + getDayOfMonthSuffix(getDay(date)) + "', yyyy");
		popularFormats.add("MMMM dd'" + getDayOfMonthSuffix(getDay(date)) + "' yyyy");
		popularFormats.add("MMMM dd , yyyy");
		format = findFormat();
	}

	private String findFormat() {
		for(String ¢:popularFormats) {
			String $ = whatFormat(¢);
			if($!=null)
				return $;
		}
		return null; // none of the formats we work with
	}

	public String getFormat() {
		return format;
	}

	public Date getDateObj() {
		if(format==null)
			return null;
		try {
			return (new SimpleDateFormat(format)).parse(this.date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String whatFormat(String s) {
		try {
			(new SimpleDateFormat(s, Locale.ENGLISH)).parse(date);
		} catch (Exception e) {
			return null;
		}
		return s;
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
