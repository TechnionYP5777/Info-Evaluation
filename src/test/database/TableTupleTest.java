package test.database;


import main.database.*;
import static org.junit.Assert.*;

import org.junit.Test;


/**
 * 
 * @author netanel Unit tests for TableTuple
 *
 */
public class TableTupleTest {
	@Test
	public void test0() {
		TableTuple nullTup = new TableTuple();
		assertNull(nullTup.getDate());
		assertNull(nullTup.getName());
		assertNull(nullTup.getReason());
		assertNull(nullTup.getRegularDate());
	}

	@Test
	public void test1() {
		TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		assertTrue("02/12/2016".equals(tup.getDate()));
		assertTrue("Charlie Sheen".equals(tup.getName()));
		assertTrue("arrest on domestic violence charges".equals(tup.getReason()));
		assertTrue("Fri Dec 02 00:00:00 PST 2016".equals((tup.getRegularDate() + "")));
	}
	
	@Test
	public void test2() {
		TableTuple tup = new TableTuple();
		tup.setName("Charlie Sheen");	
		tup.setDate("02/12/2016");
		tup.setReason("arrest on domestic violence charges");
		assertTrue("02/12/2016".equals(tup.getDate()));
		assertTrue("Charlie Sheen".equals(tup.getName()));
		assertTrue("arrest on domestic violence charges".equals(tup.getReason()));
		assertTrue("Fri Dec 02 00:00:00 PST 2016".equals((tup.getRegularDate() + "")));
	}
}