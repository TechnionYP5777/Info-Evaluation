package main.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import main.database.ReasonPair;
/**
 * 
 * 
 * @author MosheEliasof.
*Kudos to NetanelFelcher
 *This class is the same as tabletuple, only that instead of having a string in the third element,it has a list of strings.(in order to let the user choose the reasons)
 */


public class InteractiveTableTuple {
	
	private String name;
	private String date;
	private LinkedList<ReasonPair> reasons;
	private Date regularDate;
	private List<String> keyWords;
	
	public InteractiveTableTuple() {
		date = name = null;
		regularDate=null;
		reasons = new LinkedList<>();
		keyWords=new ArrayList<>();
	}
	
	public InteractiveTableTuple(final String name, final String date, final LinkedList<ReasonPair> lst) {
		this.name = name;
		this.date = date;
		this.reasons = lst;
		try {
			regularDate = new SimpleDateFormat("MM/dd/yyyy").parse(this.date);
		} catch (final Exception e) {
			regularDate = null;
		}
		keyWords=new ArrayList<>();
	}
	
	public InteractiveTableTuple(final String name, final Date date, final LinkedList<ReasonPair> lst) {
		this.name = name;
		regularDate = date;
		this.reasons = lst;
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

	public LinkedList<ReasonPair> getReasons() {
		return reasons;
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
			regularDate = new SimpleDateFormat("dd/MM/yyyy").parse(this.date);
		} catch (final Exception ¢) {
			¢.printStackTrace();
		}
	}
	
	public void setReasons(final LinkedList<ReasonPair> lst) {
		this.reasons = lst;
	}
	
	public void addKeyWord(String key) {
		if(!keyWords.contains(key))
			keyWords.add(key);
	}
	public List<String> getKeyWords(){
		return this.keyWords;
	}

}
