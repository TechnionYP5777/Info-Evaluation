package test.Analyze;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import main.Analyze.AnalyzePage;
import main.database.DataList;
import main.database.TableTuple;

public class AnalyzePageTest {

	@Test
	public void testBigParagraph() {
		final String text = "Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n"
				+ "\n"
				+ "David Cassidy was arrested for driving under the influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n"
				+ "\n" + "Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n"
				+ "Mark Salling was arrested for felony possession of child pornography on Dec 29. His representative had no comment on the matter.";
		final DataList detailsTable = new AnalyzePage(text).getDetails();
		assertEquals(detailsTable.getNumOfTuples(), 4);
		final ArrayList<TableTuple> details = detailsTable.getList();
		assert "Tito Ortiz".equals(details.get(0).getName());
		assert "driving under influence".equals(details.get(0).getReason());
		assert "01/06/2015".equals(details.get(0).getDate());

		assert "David Cassidy".equals(details.get(1).getName());
		assert "driving under influence".equals(details.get(1).getReason());
		assert "01/10/2015".equals(details.get(1).getDate());

		assert "Soulja Boy".equals(details.get(2).getName());
		assert "possession of gun".equals(details.get(2).getReason());
		assert "01/22/2015".equals(details.get(2).getDate());
		
		assertTrue("12/29/2015".equals(details.get(3).getDate()));

	}

}
