/**
 * 
 */
package test.guiFrames;

import static main.database.MySQLConnector.clearTable;
import static main.database.MySQLConnector.closeConnection;
import static main.database.MySQLConnector.runUpdate;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.database.MySQLConnector;
import main.guiFrames.RefineTable;

/**
 * @author osherh osherh@campus.technion.ac.il
 * @since 2016-12-25
 */
public class SortByTest {

	private RefineTable rt;

	public SortByTest() {
		rt = new RefineTable();
		rt.addField("Year");
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
	}

	@AfterClass
	public static void disconnect() throws SQLException {
		clearTable();
		closeConnection();
	}

	@BeforeClass
	public static void connect() throws Exception {
		new MySQLConnector();
	}

	public void initSortByName() throws Exception {
		clearTable();
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','drunk driving','drunk driving');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault','sexual assault');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce','driving');");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges','assault');");
		runUpdate(
				"INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','misdemeanor domestic violence charges','violence');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','speeding while driving','driving');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault','assault');");
		runUpdate(
				"INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license','driving influecne license');");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving','drunk driving');");
	}

	@Test
	public void sortByNameTest() throws Exception {
		initSortByName();
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Austin Chumlee Russell", "2014-01-09", "misdemeanor domestic violence charges" },
						{ "Austin Chumlee Russell", "2014-01-09", "speeding while driving" },
						{ "Austin Chumlee Russell", "2013-03-09", "sexual assault" },
						{ "Ben Stiller", "2015-02-12", "driving without a licesnce" },
						{ "Ben Stiller", "2015-02-12", "drunk driving" },
						{ "Ben Stiller", "2015-02-12", "sexual assault" },
						{ "Chris Kattan", "2015-02-10", "suspicion of drunk driving" },
						{ "Chris Kattan", "2014-01-23",
								"suspicion of driving under the influence and driving with an expired license" },
						{ "Emile Hirsch", "2015-02-12", "assault charges" } },
				new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Name");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	public void initSortByDate() throws Exception {
		clearTable();
		runUpdate(
				"INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license','drunk driving influence license')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce','driving licence')");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Brad Pitt','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving','drunk driving')");
	}

	@Test
	public void sortByDateTest() throws Exception {
		initSortByDate();
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Ben Stiller", "2015-02-12", "driving without a licesnce" },
						{ "Ben Stiller", "2015-02-12", "sexual assault" },
						{ "Emile Hirsch", "2015-02-12", "assault charges" },
						{ "Chris Kattan", "2015-02-10", "suspicion of drunk driving" },
						{ "Chris Kattan", "2014-01-23",
								"suspicion of driving under the influence and driving with an expired license" },
						{ "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
						{ "Brad Pitt", "2014-01-09", "sexual assault" },
						{ "Hugh Jackman", "2014-01-09", "sexual assault" },
						{ "Austin Chumlee Russell", "2013-03-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Date");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	public void initSortByReason() throws Exception {
		clearTable();
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-03-11','driving without a licesnce','driving license')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2014-02-11','theft','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2016-02-12','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2014-01-09','sexual assault','assault')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','drunk driving','drunk driving')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-08-12','driving without a licesnce','driving license')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2013-05-10','theft','theft')");
	}

	@Test
	public void sortByReasonTest() throws Exception {
		initSortByReason();
		final DefaultTableModel expectedTable = new DefaultTableModel(new String[][] {
				{ "Ben Stiller", "2015-08-12", "driving without a licesnce" },
				{ "Ben Stiller", "2015-03-11", "driving without a licesnce" },
				{ "Chris Kattan", "2014-01-23", "drunk driving" },
				{ "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
				{ "Austin Chumlee Russell", "2013-03-09", "sexual assault" },
				{ "Ben Stiller", "2016-02-12", "sexual assault" }, { "Ben Stiller", "2014-01-09", "sexual assault" },
				{ "Hugh Jackman", "2014-01-09", "sexual assault" }, { "Chris Kattan", "2013-05-10", "theft" },
				{ "Emile Hirsch", "2014-02-11", "theft" } }, new String[] { "Name", "Date", "Reason" });
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Reason");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}
}
