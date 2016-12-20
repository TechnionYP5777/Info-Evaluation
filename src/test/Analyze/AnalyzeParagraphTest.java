package test.Analyze;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Test;

import edu.stanford.nlp.simple.Sentence;
import main.Analyze.AnalyzeParagragh;
import main.database.TableTuple;

/**
 *
 * @author MosheEliasof
 *
 */

public class AnalyzeParagraphTest {
	@Test
	public void test1() {
		final Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds on Jan. 26 1970");
		final AnalyzeParagragh anal = new AnalyzeParagragh(sent, "1970");
		assertEquals("Justin Bieber ", anal.AnalyzeSimple().getName());
		final LocalDate date = anal.AnalyzeSimple().getRegularDate().toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
		assertEquals(1, date.getMonthValue());
		assertEquals(26, date.getDayOfMonth());
		assertEquals(anal.AnalyzeSimple().getDate(), "01/26/1970");
	}

	@Test
	public void test2() {
		final Sentence sent = new Sentence(
				"Justin Bieber was arrested for drunk driving in Georgia on February 22, 2015 .");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("Justin Bieber", tt.getName());
		assertEquals("drunk driving in Georgia", tt.getReason());
		assertEquals("02/22/2015", tt.getDate());

	}

	@Test
	public void test3() {
		final Sentence sent = new Sentence(
				"Vin Diesel was arrested for furious driving on August 14, 2002 in California");
		final TableTuple tt = new AnalyzeParagragh(sent, "2002").Analyze();
		assertEquals("Vin Diesel", tt.getName());
		assertEquals("driving furious in California", tt.getReason());
		assertEquals("08/14/2002", tt.getDate());

	}

	@Test
	public void test4() {
		final Sentence sent = new Sentence(
				"Axl Rose was arrested for consuming drugs on October 5th 2012 in NY during rock concert .");
		final TableTuple tt = new AnalyzeParagragh(sent, "2012").Analyze();
		assertEquals("Axl Rose", tt.getName());
		assertEquals("drugs consuming during concert", tt.getReason());
		assertEquals("10/05/2012", tt.getDate());

	}

	@Test
	public void test5() {
		final Sentence sent = new Sentence("Axl Rose was arrested after driving drunk in New Zeland on March 1.");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("Axl Rose", tt.getName());
		assertEquals("driving drunk in New Zeland", tt.getReason());
		assertEquals("03/01/2015", tt.getDate());
	}

	@Test
	public void test6() {
		final Sentence sent = new Sentence("Joe Francis was arrested for attacking at a Nightclub on May 16.");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("Joe Francis", tt.getName());
		assertEquals("attacking at Nightclub", tt.getReason());
		assertEquals("05/16/2015", tt.getDate());
	}

	@Test
	public void test7() {
		final Sentence sent = new Sentence(
				"Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("Tito Ortiz", tt.getName());
		assertEquals("driving under influence", tt.getReason());
		assertEquals("01/06/2015", tt.getDate());
	}

	@Test
	public void test8() {
		final Sentence sent = new Sentence(
				"David Cassidy was arrested for driving under the influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("David Cassidy", tt.getName());
		assertEquals("driving under influence", tt.getReason());
		assertEquals("01/10/2015", tt.getDate());
	}

	@Test
	public void test9() {
		final Sentence sent = new Sentence(
				"Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n");
		final TableTuple tt = new AnalyzeParagragh(sent, "2015").Analyze();
		assertEquals("Soulja Boy", tt.getName());
		assertEquals("possession of gun", tt.getReason());
		assertEquals("01/22/2015", tt.getDate());
	}

}