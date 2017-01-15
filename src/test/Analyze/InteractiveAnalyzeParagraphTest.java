package test.Analyze;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.stanford.nlp.simple.Sentence;
import main.Analyze.AnalyzeParagragh;
import main.database.InteractiveTableTuple;
import main.database.ReasonPair;;
/**
 * 
 * @author MosheEliasof
 *
 */

public class InteractiveAnalyzeParagraphTest {
	
	@Test
	public void test1() {
		final Sentence sent = new Sentence(
				"Justin Bieber was arrested for drunk driving in Georgia on February 22, 2015 .");
		final InteractiveTableTuple itt = new AnalyzeParagragh(sent, "2015").InteractiveAnalyze();
		assertEquals("Justin Bieber", itt.getName());
		assertEquals("02/22/2015", itt.getDate());
		for (ReasonPair ¢ : itt.getReasons())
			System.out.println(¢.getReason()+" with probability: "+¢.getProbability());
	}

}
