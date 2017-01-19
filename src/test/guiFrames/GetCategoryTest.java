/**
 * 
 */
package test.guiFrames;

import static main.database.DatabaseConnector.*;

import main.database.DataList;
import main.database.DatabaseConnector;
import main.database.TableTuple;
import main.guiFrames.RefineTable;

import java.sql.SQLException;

import javax.swing.JComboBox;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author osherh osherh@campus.technion.ac.il
 * @since 2016-12-25
 */
public class GetCategoryTest {

	private RefineTable rt;

	public GetCategoryTest() {
		rt = new RefineTable();
		rt.addField("Year");
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
	}

	@AfterClass
	public static void disconnect() throws SQLException {
		clearDB();
		closeConnection();
	}

	@BeforeClass
	public static void connect() throws Exception {
		new DatabaseConnector();
	}

	@Before
	public void init() throws Exception {
		clearDB();
		DataList lin = new DataList();

		TableTuple t1 = new TableTuple("Austin Chumlee Russell", "01/09/2014", "driving without a licesnce");
		t1.addKeyWord("driving license");
		TableTuple t2 = new TableTuple("Austin Chumlee Russell", "01/09/2014", "driving without a licesnce");
		t2.addKeyWord("driving license");
		TableTuple t3 = new TableTuple("Chris Kattan", "02/10/2015", "attacking wife");
		t3.addKeyWord("attacking");
		TableTuple t4 = new TableTuple("Hugh Jackman", "02/12/2015", "drunk driving");
		t4.addKeyWord("drunk driving");
		TableTuple t5 = new TableTuple("Austin Chumlee Russell", "03/09/2013", "drunk driving");
		t5.addKeyWord("drunk driving");
		TableTuple t6 = new TableTuple("Ben Stiller", "02/12/2015", "driving without a licesnce");
		t6.addKeyWord("driving license");
		TableTuple t7 = new TableTuple("Emile Hirsch", "02/12/2015", "drunk driving");
		t7.addKeyWord("drunk driving");
		TableTuple t8 = new TableTuple("Chris Kattan", "01/23/2014", "attacking wife");
		t8.addKeyWord("attacking");
		TableTuple t9 = new TableTuple("Austin Chumlee Russell", "01/09/2014", "sexual assault");
		t9.addKeyWord("assault");
		TableTuple t10 = new TableTuple("Ben Stiller", "02/12/2015", "sexual assault");
		t10.addKeyWord("assault");
		lin.insert(t1);
		lin.insert(t2);
		lin.insert(t3);
		lin.insert(t4);
		lin.insert(t5);
		lin.insert(t6);
		lin.insert(t7);
		lin.insert(t8);
		lin.insert(t9);
		lin.insert(t10);
		addEvents(lin);
		addAllKeywords(lin);
	}

	@Test
	public void getAllNamesTest() throws SQLException {
		try {
			JComboBox<String> s = new JComboBox<String>();
			rt.getCategory(s, "Name");
			assertEquals("Austin Chumlee Russell", s.getItemAt(0));
			assertEquals("Ben Stiller", s.getItemAt(1));
			assertEquals("Chris Kattan", s.getItemAt(2));
			assertEquals("Emile Hirsch", s.getItemAt(3));
			assertEquals("Hugh Jackman", s.getItemAt(4));
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	@Test
	public void getAllYearsTest() throws SQLException {
		try {
			JComboBox<String> s = new JComboBox<String>();
			rt.getCategory(s, "Year");
			assertEquals("2015", s.getItemAt(0));
			assertEquals("2014", s.getItemAt(1));
			assertEquals("2013", s.getItemAt(2));
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	@Test
	public void getAllReasonsTest() throws SQLException {
		try {
			JComboBox<String> s = new JComboBox<String>();
			rt.getCategory(s, "Reason");
			assertEquals("assault", s.getItemAt(0));
			assertEquals("attacking", s.getItemAt(1));
			assertEquals("driving license", s.getItemAt(2));
			assertEquals("drunk driving", s.getItemAt(3));
		} catch (SQLException ¢) {
			throw ¢;
		}
	}
}