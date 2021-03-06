package test.database;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.database.DataList;
import main.database.TableTuple;

public class DataListTest {
	@Test
	public void test1() {
		final TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		final DataList dl = new DataList();
		assertEquals(0, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(1, dl.getNumOfTuples());
	}

	@Test
	public void test2() {
		final TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		final DataList dl = new DataList();
		dl.insert("Justin Bieber", "23/01/2015", "drunk driving");
		assertEquals(1, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(2, dl.getNumOfTuples());
	}

	@Test
	public void test3() {
		final TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		final DataList dl = new DataList();
		dl.insert("Justin Bieber", "23/01/2015", "drunk driving");
		assertEquals(1, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(2, dl.getNumOfTuples());
		final DataList dl2 = new DataList(dl);
		assertEquals(2, dl2.getNumOfTuples());
		assert dl2.getList().contains(tup);
	}

	@Test
	public void test4() {
		final TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		final DataList dl = new DataList();
		dl.insert("Justin Bieber", "23/01/2015", "drunk driving");
		assertEquals(1, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(2, dl.getNumOfTuples());
		final DataList dl2 = new DataList();
		dl2.insert("Lindsay Lohan", "26/05/2007", "driving under influence and possession of cocaine");
		final TableTuple tup2 = new TableTuple("Lindsay Lohan", "24/07/2007",
				"driving under influence and possession of cocaine");
		dl2.insert(tup2);
		dl2.merge(dl);
		assertEquals(4, dl2.getNumOfTuples());
		assert dl2.getList().contains(tup);
		assert dl2.getList().contains(tup2);
	}
}
