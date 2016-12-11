package main.database;

import java.util.ArrayList;

/**
 * 
 * @author netanel This class holds all the data to be shown in the table in the
 *         GUI
 */
public class DataList {
	private ArrayList<TableTuple> list;
	private int numOfTuples;

	public DataList() {
		this.list = new ArrayList<>();
	}
	public DataList(DataList dl){
		this.list = new ArrayList<>();
		this.merge(dl);
	}

	public void insert(String name, String date, String reason) {
		this.list.add(new TableTuple(name, date, reason));
		++this.numOfTuples;
	}

	public void insert(TableTuple ¢) {
		this.list.add(¢);
		++this.numOfTuples;
	}

	public int getNumOfTuples() {
		return this.numOfTuples;
	}

	public ArrayList<TableTuple> getList() {
		return this.list;
	}
	public void merge(DataList lst){
		for(TableTuple i:lst.getList())
			this.list.add(i);
		this.numOfTuples+=lst.getNumOfTuples();
	}
	// if needed, we can implement here sorts, refines etc.
}
