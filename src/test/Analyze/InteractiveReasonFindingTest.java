package test.Analyze;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Test;

import edu.stanford.nlp.simple.Sentence;
import main.Analyze.AnalyzeParagragh;
import main.database.ReasonPair;

/**
 * 
 * @author MosheEliasof
 * 
 */

public class InteractiveReasonFindingTest {
	@Test
	public void test1() {
		for (ReasonPair ¢ : new AnalyzeParagragh(
				new Sentence(
						"Axl Rose was arrested for consuming drugs on October 5th 2012 in NY during rock concert ."),
				"2015").InteractiveReasonFinding()) {
			System.out.print(¢.getProbability() + " ");
			System.out.println(¢.getReason());
		}

	}
	

}
