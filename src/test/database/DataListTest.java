package test.database;

import static org.junit.Assert.*;

import org.junit.Test;
import main.database.*;

public class DataListTest {
	@Test
	public void test1() {
		TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		DataList dl = new DataList();
		assertEquals(0, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(1, dl.getNumOfTuples());
	}

	@Test
	public void test2() {
		TableTuple tup = new TableTuple("Charlie Sheen", "02/12/2016", "arrest on domestic violence charges");
		DataList dl = new DataList();
		dl.insert("Justin Bieber", "23/01/2015", "drunk driving");
		assertEquals(1, dl.getNumOfTuples());
		dl.insert(tup);
		assertEquals(2, dl.getNumOfTuples());
	}
}
