package database;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author netanel This class represents a tuple in the final table that is
 *         shown to the user in the GUI
 *
 */
public class tableTuple {

	private String name;
	private String date;
	private String reason;
	private Date regularDate;
	// we save in date format in order to be able to do manipulations on the DB
	// according to the date

	public tableTuple() {
		this.reason = this.date = this.name = this.date = null;
	}

	public tableTuple(String name, String date, String reason) {
		this.name = name;
		this.date = date;
		this.reason = reason;
		try {
			this.regularDate = (new SimpleDateFormat("dd/MM/yyyy")).parse(this.date);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public tableTuple(String name, Date date, String reason) {
		this.name = name;
		this.regularDate = date;
		this.reason = reason;
		try {
			this.date = (new SimpleDateFormat("dd/MM/yyyy")).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getName() {
		return this.name;
	}

	public String getDate() {
		return this.date;
	}

	public String getReason() {
		return this.reason;
	}

	public Date getRegularDate() {
		return this.regularDate;
	}

}