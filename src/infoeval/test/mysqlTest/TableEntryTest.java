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

public class TableEntryTest{
	@Test public void test1(){
		TableEntry te= new TableEntry();
		assertNull(te.getBirthDate());
		assertNull(te.getDeathDate());
		assertEquals("", te.getBirthPlace());
		assertEquals("", te.getDeathPlace());
		assertEquals("", te.getUrl());
		assertEquals("", te.getName());
	}
	@Test
	@SuppressWarnings("deprecation") public void test2(){
		TableEntry te= new TableEntry("url", "name", "birthPlace", "deathPlace", new Date(2000, 10, 21), new Date(2015, 10, 21));
		assertEquals(te.getBirthDate(),new Date(2000, 10, 21) );
		assertEquals(te.getDeathDate(),new Date(2015, 10, 21));
		assertEquals("birthPlace", te.getBirthPlace());
		assertEquals("deathPlace", te.getDeathPlace());
		assertEquals("url", te.getUrl());
		assertEquals("name", te.getName());
	}
}