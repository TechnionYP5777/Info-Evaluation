package infoeval.test.mysqlTest;

import static org.junit.Assert.*;

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
}