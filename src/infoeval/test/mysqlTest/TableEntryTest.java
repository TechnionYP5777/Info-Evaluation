package infoeval.test.mysqlTest;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Test;

import infoeval.main.mysql.TableEntry;

/**
 * 
 * @author Netanel
 * @Since 07-05-2017
 *
 */

public class TableEntryTest {
	@Test
	public void test1() {
		TableEntry te = new TableEntry();
		assertNull(te.getBirthDate());
		assertNull(te.getDeathDate());
		assertEquals("", te.getBirthPlace());
		assertEquals("", te.getDeathPlace());
		assertEquals("", te.getUrl());
		assertEquals("", te.getName());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void test2() {
		TableEntry te = new TableEntry("url", "name", "birthPlace", "deathPlace", new Date(2000, 10, 21),
				new Date(2015, 10, 21), "", "", "", "", "","","");
		assertEquals(te.getBirthDate(), new Date(2000, 10, 21));
		assertEquals(te.getDeathDate(), new Date(2015, 10, 21));
		assertEquals("birthPlace", te.getBirthPlace());
		assertEquals("deathPlace", te.getDeathPlace());
		assertEquals("url", te.getUrl());
		assertEquals("name", te.getName());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void test3() {
		TableEntry te = new TableEntry();
		te.setUrl("url");
		te.setName("name");
		te.setDeathPlace("deathPlace");
		te.setBirthPlace("birthPlace");
		te.setBirthDate(new Date(2000, 10, 21));
		te.setDeathDate(new Date(2015, 10, 21));
		assertEquals(te.getBirthDate(), new Date(2000, 10, 21));
		assertEquals(te.getDeathDate(), new Date(2015, 10, 21));
		assertEquals("birthPlace", te.getBirthPlace());
		assertEquals("deathPlace", te.getDeathPlace());
		assertEquals("url", te.getUrl());
		assertEquals("name", te.getName());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void test4() {
		TableEntry te = new TableEntry("bla", "bla", "bla", "bla", new Date(1, 1, 1), new Date(1, 1, 1), "", "", "", "",
				"","","");
		te.setUrl("url");
		te.setName("name");
		te.setDeathPlace("deathPlace");
		te.setBirthPlace("birthPlace");
		te.setBirthDate(new Date(2000, 10, 21));
		te.setDeathDate(new Date(2015, 10, 21));
		te.setOverview("An interesting overview");
		assertEquals(te.getBirthDate(), new Date(2000, 10, 21));
		assertEquals(te.getDeathDate(), new Date(2015, 10, 21));
		assertEquals("birthPlace", te.getBirthPlace());
		assertEquals("deathPlace", te.getDeathPlace());
		assertEquals("url", te.getUrl());
		assertEquals("name", te.getName());
		assertEquals("An interesting overview", te.getOverview());
	}
}