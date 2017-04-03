package main.database.java;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author netanel This class represents a tuple in the final table that is
 *         shown to the user in the GUI
 * @author osherh
 */

public class TableTuple {

	private String name;
	private String date;
	private String reason;
	private Date regularDate;
	private List<String> keyWords;
	// we save in date format in order to be able to do manipulations on the DB
	// according to the date

	public TableTuple() {
		reason = date = name = date = null;
		keyWords=new ArrayList<>();
	}

	public TableTuple(final String name, final String date, final String reason) {
		this.name = name;
		this.date = date;
		this.reason = reason;
		try {
			regularDate = new SimpleDateFormat("MM/dd/yyyy").parse(this.date);
		} catch (final Exception e) {
			regularDate = null;
		}
		keyWords=new ArrayList<>();

	}

	public TableTuple(final String name, final Date date, final String reason) {
		this.name = name;
		regularDate = date;
		this.reason = reason;
		try {
			this.date = new SimpleDateFormat("MM/dd/yyyy").format(date);
		} catch (final Exception ¢) {
			¢.printStackTrace();
		}

	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date;
	}

	public String getReason() {
		return reason;
	}

	public Date getRegularDate() {
		return regularDate;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDate(final String date) {
		this.date = date;
		try {
			regularDate = new SimpleDateFormat("MM/dd/yyyy").parse(this.date);
		} catch (final Exception ¢) {
			¢.printStackTrace();
		}
	}

	public void setReason(final String reason) {
		this.reason = reason;
	}
	
	public void addKeyWord(String key) {
		if(!keyWords.contains(key))
			keyWords.add(key);
	}
	public List<String> getKeyWords(){
		return this.keyWords;
	}

}
