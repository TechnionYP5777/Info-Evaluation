package main.Analyze;

import java.util.ArrayList;
import java.util.List;

import main.database.DataList;

/**
 *
 * @author netanel this class contains all the sources to be analyzed. It uses
 *         the AnalyzeParagraph class in order to analyze each source, and the
 *         merge all the lists of each source so we have one big list with all
 *         the information we were looking for ready to be delivered to the GUI
 *
 *
 *
 */

public class AnalyzeSources {
	private final List<String> sources;
	private int numOfSources;
	private final DataList data;

	public AnalyzeSources() {
		sources = new ArrayList<>();
		data = new DataList();
	}

	/**
	 * When we add a source, we immediately get the dataList of it and add it to
	 * our big list of info
	 * 
	 * @param src
	 */
	public void addSource(final String src) {
		sources.add(src);
		++numOfSources;
		data.merge(new AnalyzePage(src).getDetails());
	}

	public int getNumOfSources() {
		return numOfSources;
	}

	public DataList getData() {
		return data;
	}

}