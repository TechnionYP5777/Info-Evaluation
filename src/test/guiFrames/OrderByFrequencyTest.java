package test.guiFrames;

import static main.database.MySQLConnector.*;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import main.database.MySQLConnector;
import main.guiFrames.RefineTable;

/**
 * Tests for main.guiFrames.RefineTable
 *
 * @author osherh
 */
public class RefineTableFunctionalityTest {

	@Before
	public void emptyTable() throws SQLException {
		clearTable();
	}

	@AfterClass
	public static void disconnect() {
		closeConnection();
	}

	public RefineTableFunctionalityTest() throws Exception {
		new MySQLConnector();
	}

	public void initSortByName() throws Exception {
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','drunk driving');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault');");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce');");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges');");
		runUpdate(
				"INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','misdemeanor domestic violence charges');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','speeding while driving');");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault');");
		runUpdate(
				"INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");
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

		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		assert rt.fieldExist("Date");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Name");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	public void initSortByDate() throws Exception {
		runUpdate(
				"INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce')");
		runUpdate("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges')");
		runUpdate("INSERT INTO celebs_arrests values('Brad Pitt','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault')");
		runUpdate("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving')");
	}

	@Test
	public void sortByDateTest() throws Exception {
		initSortByDate();
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Emile Hirsch", "2015-02-12", "assault charges" },
						{ "Ben Stiller", "2015-02-12", "driving without a licesnce" },
						{ "Ben Stiller", "2015-02-12", "sexual assault" },
						{ "Chris Kattan", "2015-02-10", "suspicion of drunk driving" },
						{ "Chris Kattan", "2014-01-23",
								"suspicion of driving under the influence and driving with an expired license" },
						{ "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
						{ "Brad Pitt", "2014-01-09", "sexual assault" },
						{ "Hugh Jackman", "2014-01-09", "sexual assault" },
						{ "Austin Chumlee Russell", "2013-03-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Date");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	public void initSortByReason() throws Exception {
		runUpdate("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault')");
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
		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Reason");
		assertEquals(expectedTable.getValueAt(4, 2), outputTable.getValueAt(4, 2));
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	/*************************
	 * FilterBy Test
	 **********************************************/
	public void initFilter() throws Exception {
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
	public void filterByNameTest() throws Exception {
		initFilter();
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Ben Stiller", "2016-02-12", "sexual assault" },
						{ "Ben Stiller", "2015-08-12", "driving without a licesnce" },
						{ "Ben Stiller", "2015-03-11", "driving without a licesnce" },
						{ "Ben Stiller", "2014-01-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		rt.addField("Year");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Name", "Ben Stiller");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	@Test
	public void filterByYearTest() throws Exception {
		initFilter();
		final DefaultTableModel expectedTable = new DefaultTableModel(new String[][] {
				{ "Emile Hirsch", "2014-02-11", "theft" }, { "Chris Kattan", "2014-01-23", "drunk driving" },
				{ "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
				{ "Ben Stiller", "2014-01-09", "sexual assault" }, { "Hugh Jackman", "2014-01-09", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		rt.addField("Year");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Year", "2014");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}

	@Test
	public void filterByReasonTest() throws Exception {
		initFilter();
		final DefaultTableModel expectedTable = new DefaultTableModel(
				new String[][] { { "Austin Chumlee Russell", "2014-01-09", "sexual assault" },
						{ "Ben Stiller", "2016-02-12", "sexual assault" },
						{ "Ben Stiller", "2014-01-09", "sexual assault" },
						{ "Hugh Jackman", "2014-01-09", "sexual assault" },
						{ "Austin Chumlee Russell", "2013-03-09", "sexual assault charges" } },
				new String[] { "Name", "Date", "Reason" });
		final RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		rt.addField("Year");
		final DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.filterBy(outputTable, "Reason", "assault");
		for (int i = 0; i < outputTable.getRowCount(); ++i)
			for (int j = 0; j < outputTable.getColumnCount(); ++j)
				assertEquals(expectedTable.getValueAt(i, j), outputTable.getValueAt(i, j));
	}
}