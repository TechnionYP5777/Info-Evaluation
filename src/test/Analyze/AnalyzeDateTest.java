package test.Analyze;

import static org.junit.Assert.*;

//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;

import org.junit.Test;

import main.Analyze.AnalyzeDate;

public class AnalyzeDateTest {

	@Test
	public void test() {

		// testing first format
		AnalyzeDate d = new AnalyzeDate("04/07/1995");
		assertTrue("MM/dd/yyyy".equals(d.getFormat()));
//		assertTrue("04/07/1995".equals((new SimpleDateFormat("MM/dd/yyyy")).format(d.getDateObj())));

		// testing second format
		d = new AnalyzeDate("Feb. 24, 1966");
		assertTrue("MMM. dd, yyyy".equals(d.getFormat()));
//		assertTrue("02/24/1966".equals((new SimpleDateFormat("MM/dd/yyyy")).format(d.getDateObj())));

		// testing third format
		d = new AnalyzeDate("Dec 8, 2006");
		assertTrue("MMM dd, yyyy".equals(d.getFormat()));
//		assertTrue("12/08/2006".equals((new SimpleDateFormat("MM/dd/yyyy")).format(d.getDateObj())));

		// testing third format, different style
		d = new AnalyzeDate("May 9, 1971");
		assertTrue("MMM dd, yyyy".equals(d.getFormat()));
//		assertTrue("05/09/1971".equals((new SimpleDateFormat("MM/dd/yyyy")).format(d.getDateObj())));

		// testing forth format
		d = new AnalyzeDate("May 26th, 1998");
		assertTrue("MMMM dd'th', yyyy".equals(d.getFormat()));
//		assertTrue("05/26/1998".equals((new SimpleDateFormat("MM/dd/yyyy")).format(d.getDateObj())));

	}

}
