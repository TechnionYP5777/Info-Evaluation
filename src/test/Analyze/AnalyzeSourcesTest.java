package test.Analyze;

import static org.junit.Assert.*;
import org.junit.Test;
import main.Analyze.AnalyzeSources;
public class AnalyzeSourcesTest{
	
	@Test public void test1(){
		AnalyzeSources as= new AnalyzeSources();
		assertEquals(0, as.getNumOfSources());
		as.addSource(
				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
		assertEquals(1, as.getNumOfSources());
	}
	@Test public void test2(){
		AnalyzeSources as= new AnalyzeSources();
		assertEquals(0, as.getNumOfSources());
		as.addSource(
				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
		assertEquals(1, as.getNumOfSources());
		assertEquals(2, as.getData().getNumOfTuples());
	}
	@Test public void test3(){
		AnalyzeSources as= new AnalyzeSources();
		assertEquals(0, as.getNumOfSources());
		as.addSource(
				"Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.");
		as.addSource(
				"Lebron James was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nBritney Spears hates dogs. He spet on a dog and therefore was arrested and put to jail.");

		assertEquals(2, as.getNumOfSources());
		assertEquals(4, as.getData().getNumOfTuples());
	}
}