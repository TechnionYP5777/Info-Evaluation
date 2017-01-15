package test.Analyze;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import main.Analyze.AnalyzePage;
import main.database.DataList;
import main.database.InteractiveTableTuple;
import main.database.ReasonPair;
import main.database.TableTuple;

public class AnalyzePageTest {

	private String text;

	@Before
	public void startTest() {
		text = "Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n"
				+ "\n"
				+ "David Cassidy was arrested for driving under the influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n"
				+ "\n" + "Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n"
				+ "Mark Salling was arrested for felony possession of child pornography on Dec 29 2013. His representative had no comment on the matter.";
	}

	@Test
	public void testBigParagraph() {
		DataList detailsTable = new AnalyzePage(text).getDetails();
		assertEquals(detailsTable.getNumOfTuples(), 4);
		ArrayList<TableTuple> details = detailsTable.getList();
		assert "Tito Ortiz".equals(details.get(0).getName());
		assert "driving under influence".equals(details.get(0).getReason());
		assert "01/06/2016".equals(details.get(0).getDate());

		assert "David Cassidy".equals(details.get(1).getName());
		assert "driving under influence".equals(details.get(1).getReason());
		assert "01/10/2016".equals(details.get(1).getDate());

		assert "Soulja Boy".equals(details.get(2).getName());
		assert "possession of gun".equals(details.get(2).getReason());
		assert "01/22/2016".equals(details.get(2).getDate());

		assert "12/29/2013".equals(details.get(3).getDate());

		detailsTable = new AnalyzePage(text, "2014").getDetails();
		details = detailsTable.getList();
		assert "01/06/2014".equals(details.get(0).getDate());
		assert "01/10/2014".equals(details.get(1).getDate());
		assert "01/22/2014".equals(details.get(2).getDate());
		assert "12/29/2013".equals(details.get(3).getDate());

	}

	@Test
	public void testInteractiveParagraph() {
		List<InteractiveTableTuple> detailsTable = new AnalyzePage(text).getInteractiveDetails();
		assertEquals(detailsTable.size(), 4);
		assertTrue("Soulja Boy".equals(detailsTable.get(2).getName()));
		boolean realReason=false;
		for(ReasonPair ¢: detailsTable.get(1).getReasons())
			if ("driving under influence".equals(¢.getReason()))
				realReason = true;
		assertTrue(realReason);
		assert "12/29/2013".equals(detailsTable.get(3).getDate());
		
	}

}
