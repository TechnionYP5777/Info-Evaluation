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

	public AnalyzeDate(final String d) {
		date = d;

		popularFormats = new ArrayList<>();
		popularFormats.add("MM/dd/yyyy");
		popularFormats.add("MMM. dd, yyyy");
		popularFormats.add("MMM. dd yyyy");
		popularFormats.add("MMM dd, yyyy");
		popularFormats.add("MMM dd yyyy");
		popularFormats.add("MMMM dd'" + getDayOfMonthSuffix(getDay(date)) + "', yyyy");
		popularFormats.add("MMMM dd'" + getDayOfMonthSuffix(getDay(date)) + "' yyyy");
		popularFormats.add("MMMM dd , yyyy");
		format = findFormat();
	}

	private String findFormat() {
		for (final String ¢ : popularFormats) {
			final String $ = whatFormat(¢);
			if ($ != null)
				return $;
		}
		return null; // none of the formats we work with
	}

	public String getFormat() {
		return format;
	}

	public Date getDateObj() {
		if (format == null)
			return null;
		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (final ParseException ¢) {
			¢.printStackTrace();
		}
		return null;
	}

	private String whatFormat(final String $) {
		try {
			new SimpleDateFormat($, Locale.ENGLISH).parse(date);
		} catch (final Exception e) {
			return null;
		}
		return $;
	}

	private int getDay(final String ¢) {
		final Matcher $ = Pattern.compile("\\s([A-Za-z]+)").matcher(¢);
		return !$.find() ? -1 : Integer.parseInt($.group(1));
	}

	private String getDayOfMonthSuffix(final int day) {
		switch (day) {
		case 1:
		case 21:
		case 31:
			return "st";

		case 2:
		case 22:
			return "nd";

		case 3:
		case 23:
			return "rd";

		default:
			return "th";
		}
	}

}
