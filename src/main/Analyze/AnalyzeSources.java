package main.Analyze;
import java.util.ArrayList;
import java.util.List;
import main.database.*;

/**
 * 
 * @author netanel
 * this class contains all the sources to be analyzed.
 * It uses the AnalyzeParagraph class in order to analyze
 * each source, and the merge all the lists of each source
 * so we have one big list with all the information we were looking for
 * ready to be delivered to the GUI
 * 
 * 
 *
 */

public class AnalyzeSources{
	private List<String> sources;
	private int numOfSources;
	private DataList data;
	
	public AnalyzeSources(){
		this.sources=new ArrayList<>();
		this.data=new DataList();
	}
	/**
	 * When we add a source, we immediately get the dataList of it
	 * and add it to our big list of info
	 * @param src
	 */
	public void addSource(String src){
		this.sources.add(src);
		++this.numOfSources;
		//add next line after the details are actually being updated in AnalyzePage
		//this.data.merge((new AnalyzePage(src)).getDetails());
	}
	public int getNumOfSources(){
		return this.numOfSources;
	}
	
}