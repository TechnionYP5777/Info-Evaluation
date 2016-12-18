package test.Analyze;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import main.Analyze.*;
import main.database.DataList;
import main.database.TableTuple;

public class AnalyzePageTest {

	@Test
	public void testBigParagraph() {
		String text= "Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n"+
					"\n"+
					"David Cassidy was arrested for driving under the influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n"+
					"\n"+
					"Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n";
		AnalyzePage p = new AnalyzePage(text);
		DataList detailsTable = p.getDetails();
		assertEquals(detailsTable.getNumOfTuples(), 3);
		ArrayList<TableTuple> details = detailsTable.getList();
		assertTrue("Tito Ortiz".equals(details.get(0).getName()));
		assertTrue("driving under influence".equals(details.get(0).getReason()));
		assertTrue("01/06/2015".equals(details.get(0).getDate()));
		
		assertTrue("David Cassidy".equals(details.get(1).getName()));
		assertTrue("driving under influence".equals(details.get(1).getReason()));
		assertTrue("01/10/2015".equals(details.get(1).getDate()));
		
		assertTrue("Soulja Boy".equals(details.get(2).getName()));
		assertTrue("possession of gun".equals(details.get(2).getReason()));
		assertTrue("01/22/2015".equals(details.get(2).getDate()));
		
	}

}
