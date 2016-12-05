package database;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.Test;

import edu.stanford.nlp.simple.Sentence;

/**
 * [[SuppressWarningsSpartan]]
 */
public class AnalyzeParagraphTest {
	@Test
	public void test1() {
        Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds on Jan. 26");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        assertEquals("Justin Bieber ",anal.Analyze().getName());
        LocalDate date=anal.Analyze().getRegularDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(1, date.getMonthValue());
        assertEquals(26, date.getDayOfMonth());
        assertEquals(anal.Analyze().getDate(), "26/01/1970");
	}

}