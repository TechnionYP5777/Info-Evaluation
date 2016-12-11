package test.Analyze;

import static org.junit.Assert.*;

import org.junit.Test;
import main.Analyze.*;

public class AnalyzePageTest {

	@Test
	public void test() {
		testCreateParagraphs();
	}

	void testCreateParagraphs() {
		assertTrue("Moshiko likes Justin Biber so he writes tests with his name"
				.equals((new AnalyzePage("Moshiko likes Justin Biber so he writes tests with his name")).getParagraphs()
						.get(0)));
	}

}
