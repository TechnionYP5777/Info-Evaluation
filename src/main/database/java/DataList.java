package main.database.java;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author netanel This class holds all the data to be shown in the table in the
 *         GUI
 * @author osherh
 */
public class DataList implements Iterable<TableTuple> {
	private final ArrayList<TableTuple> list;
	private int numOfTuples;

	@Override
	public Iterator<TableTuple> iterator() {
		return list.iterator();
	}

	public DataList() {
		list = new ArrayList<>();
	}

	public DataList(final DataList dl) {
		list = new ArrayList<>();
		merge(dl);
	}

	public void insert(final String name, final String date, final String reason) {
		list.add(new TableTuple(name, date, reason));
		++numOfTuples;
	}

	public void insert(final TableTuple ¢) {
		list.add(¢);
		++numOfTuples;
	}

	public int getNumOfTuples() {
		return numOfTuples;
	}

	public ArrayList<TableTuple> getList() {
		return list;
	}

	public void merge(final DataList lst) {
		for (final TableTuple i : lst.getList())
			list.add(i);
		numOfTuples += lst.getNumOfTuples();
	}

	public void printList() {
		for (final TableTuple ¢ : getList()) {
			System.out.println(¢.getName());
			System.out.println(¢.getDate());
			System.out.println(¢.getReason());
			System.out.println();
		}
	}
	// if needed, we can implement here sorts, refines etc.
}
