/**
 * 
 */
package test.guiFrames;

import static main.database.MySQLConnector.clearTable;
import static main.database.MySQLConnector.closeConnection;
import static main.database.MySQLConnector.runUpdate;

import main.database.MySQLConnector;
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
	public static void disconnect() {
		closeConnection();
	}

	@BeforeClass
	public static void connect() throws Exception {
		new MySQLConnector();
	}

	@Before
	public void init() throws Exception {
		clearTable();
		runUpdate(
				"INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','driving without a licesnce');");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','attacking wife');");
		runUpdate("INSERT INTO celebs_arrests values('Hugh Jackman','2015-02-12','drunk driving');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','drunk driving');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce');");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','drunk driving');");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','attacking wife');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault');");
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
			assertEquals("attacking wife", s.getItemAt(0));
			assertEquals("driving without a licesnce", s.getItemAt(1));
			assertEquals("drunk driving", s.getItemAt(2));
			assertEquals("sexual assault", s.getItemAt(3));
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

}
