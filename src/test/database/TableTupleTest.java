package test.database;

import static org.junit.Assert.assertNull;

import org.junit.Test;

import main.database.TableTuple;

/**
 *
 * @author netanel Unit tests for TableTuple
 *
 */
public class TableTupleTest {
	@Test
	public void test0() {
		final TableTuple nullTup = new TableTuple();
		assertNull(nullTup.getDate());
		assertNull(nullTup.getName());
		assertNull(nullTup.getReason());
		assertNull(nullTup.getRegularDate());
	}

	@Test
	public void test1() {
		final TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		assert "02/12/2016".equals(tup.getDate());
		assert "Charlie Sheen".equals(tup.getName());
		assert "arrest on domestic violence charges".equals(tup.getReason());
		assert "Fri Dec 02 00:00:00 PST 2016".equals(tup.getRegularDate() + "");
	}

	@Test
	public void test2() {
		final TableTuple tup = new TableTuple();
		tup.setName("Charlie Sheen");
		tup.setDate("02/12/2016");
		tup.setReason("arrest on domestic violence charges");
		assert "02/12/2016".equals(tup.getDate());
		assert "Charlie Sheen".equals(tup.getName());
		assert "arrest on domestic violence charges".equals(tup.getReason());
		assert "Fri Dec 02 00:00:00 PST 2016".equals(tup.getRegularDate() + "");
	}
}