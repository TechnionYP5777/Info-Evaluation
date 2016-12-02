package database;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author netanel Unit tests for tableTuple
 *
 */
public class tableTupleTest {
	@Test
	public void test0() {
		tableTuple nullTup = new tableTuple();
		assertNull(nullTup.getDate());
		assertNull(nullTup.getName());
		assertNull(nullTup.getReason());
		assertNull(nullTup.getRegularDate());
	}

	@Test
	public void test1() {
		tableTuple tup = new tableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		assertTrue("02/12/2016".equals(tup.getDate()));
		assertTrue("Charlie Sheen".equals(tup.getName()));
		assertTrue("arrest on domestic violence charges".equals(tup.getReason()));
		assertTrue("Fri Dec 02 00:00:00 PST 2016".equals((tup.getRegularDate() + "")));
	}
}