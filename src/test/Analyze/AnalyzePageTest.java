package test.Analyze;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import main.Analyze.*;

public class AnalyzePageTest {

	@Test
	public void test() {
		testCreateParagraphs();
	}

	void testCreateParagraphs() {
		String text="Moshiko likes Justin Biber so he writes tests with his name";
		AnalyzePage p = new AnalyzePage(text);
		List<String> paragraphs=p.getParagraphs();
		assertTrue(text.equals(paragraphs.get(0)));
		text="Rihanna was arrested for murder on December 2nd 2016. But she was released 2 days later. \n\nKanye West hates dogs. He spet on a dog and therefore was arrested and put to jail.";
		p = new AnalyzePage(text);
		paragraphs=p.getParagraphs();
		assertEquals(paragraphs.size(), 2);
		assertFalse(text.equals(paragraphs.get(0)));
		assertTrue("Kanye West hates dogs. He spet on a dog and therefore was arrested and put to jail."
				.equals(paragraphs.get(1)));
	}

}
