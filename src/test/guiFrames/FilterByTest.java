/**
 * 
 */
package test.guiFrames;

import static main.database.MySQLConnector.clearDB;
import static main.database.MySQLConnector.closeConnection;
import static main.database.MySQLConnector.runUpdate;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.database.MySQLConnector;
import main.guiFrames.FilterType;
import main.guiFrames.RefineTable;

/**
 * @author osherh osherh@campus.technion.ac.il
 * @since 2016-12-25
 */
public class FilterByTest {

	private RefineTable rt;

	public FilterByTest() {
		rt = new RefineTable();
		rt.addField("Year");
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
	}

	@BeforeClass
	public static void connect() throws Exception {
		new MySQLConnector();
	}

	@AfterClass
	public static void disconnect() throws SQLException {
		clearDB();
		closeConnection();
	}

	@Before
	public void init() throws Exception {
		clearDB();
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault charges')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-03-11','driving without a licesnce')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2014-02-11','theft')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2016-02-12','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','drunk driving')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-08-12','driving without a licesnce')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2013-05-10','theft')");
	}

	@Test
	public void filterByAutocompleteTest() throws Exception {

	}

	@Test
	public void filterByNameTest() throws Exception {
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Ben Stiller", "2016-02-12", "sexual assault" },
						{ "Ben Stiller", "2015-08-12", "driving without a licesnce" },
						{ "Ben Stiller", "2015-03-11", "driving without a licesnce" },
						{ "Ben Stiller", "2014-01-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Name", "Ben Stiller", FilterType.CHOOSE_FROM_LIST);
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	// @Test
	public void filterByYearTest() throws Exception {
		final DefaultTableModel expectedTable = new DefaultTableModel(new String[][] {
				{ "Emile Hirsch", "2014-02-11", "theft" }, { "Chris Kattan", "2014-01-23", "drunk driving" },
				{ "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
				{ "Ben Stiller", "2014-01-09", "sexual assault" }, { "Hugh Jackman", "2014-01-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Year", "2014", FilterType.RIGHT_CLICK);
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	@Test
	public void filterByReasonTest() throws Exception {
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
						{ "Ben Stiller", "2016-02-12", "sexual assault" },
						{ "Ben Stiller", "2014-01-09", "sexual assault" },
						{ "Hugh Jackman", "2014-01-09", "sexual assault" },
						{ "Austin Chumlee Russell", "2013-03-09", "sexual assault charges" } },
				new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Reason", "assault", FilterType.AUTOCOMPLETE);
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}
}
