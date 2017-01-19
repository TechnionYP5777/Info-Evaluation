package test.database;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import main.database.DataList;
import main.database.MySQLConnector;
import main.database.TableTuple;

import static main.database.MySQLConnector.*;

/*
 * @author osherh
 * @since 6/12/2016
 * */
public class MySQLConnectorTest {
	public MySQLConnectorTest() throws Exception {
		new MySQLConnector();
	}

	@AfterClass
	public static void disconnect() throws SQLException {
		clearDB();
		closeConnection();
	}

	@Before
	public void initializeDatabase() throws SQLException {
		clearDB();
		TableTuple t1 = new TableTuple("Justin Bieber", "01/23/2014",
				"suspicion of driving under the influence and driving with an expired license");
		t1.addKeyWord("driving");
		t1.addKeyWord("influence");
		t1.addKeyWord("expired license");
		TableTuple t2 = new TableTuple("Columbus Short", "02/14/2014", "physically attacking his wife");
		t2.addKeyWord("wife attacking");
		TableTuple t3 = new TableTuple("Soulja Boy", "01/22/2014", "possession of a loaded gun");
		t3.addKeyWord("gun possession");
		TableTuple t4 = new TableTuple("Chris Kattan", "02/10/2015", "suspicion of drunk driving");
		t4.addKeyWord("drunk driving");
		TableTuple t5 = new TableTuple("Suge Knight", "01/29/2015", "involved in a fatal hit run");
		t5.addKeyWord("hit and run");
		TableTuple t6 = new TableTuple("Emile Hirsch", "02/12/2015", "assault charges");
		t6.addKeyWord("assault");
		TableTuple t7 = new TableTuple("Austin Chumlee Russell", "03/09/2016", "sexual assault");
		t7.addKeyWord("assault");
		TableTuple t8 = new TableTuple("Track Palin", "01/18/2016",
				"charges of fourth-degree assault and interfering with the report of a domestic violence crime");
		t8.addKeyWord("assault");
		t8.addKeyWord("violence");
		TableTuple t9 = new TableTuple("Don McLean", "01/17/2015", "misdemeanor domestic violence charges");
		t9.addKeyWord("violence");
		DataList dl = new DataList();
		dl.insert(t1);
		dl.insert(t2);
		dl.insert(t3);
		dl.insert(t4);
		dl.insert(t5);
		dl.insert(t6);
		dl.insert(t7);
		dl.insert(t8);
		dl.insert(t9);
		addEvents(dl);
		addAllKeywords(dl);
	}

	@Test
	public void keywordsTest() throws SQLException {
		String str = "";
		ResultSet rs = runQuery("SELECT COUNT(*) FROM keywords_table WHERE Keyword='violence'");
		if (rs.next())
			assertEquals(1, rs.getInt(1));
		rs = runQuery("SELECT COUNT(*) FROM keywords_table WHERE Keyword='assault'");
		if (rs.next())
			assertEquals(1, rs.getInt(1));
		rs = runQuery("SELECT COUNT(*) FROM keywords_table");
		if (rs.next())
			assertEquals(9, rs.getInt(1));
		ResultSet r = runQuery("SELECT keywords FROM celebs_arrests WHERE name='Justin Bieber'");
		if (r.next())
			str = r.getString("keywords");
		if (r.next())
			assertEquals("driving influence expired license" + " ", str);
		rs.close();
		r.close();
	}

	@Test
	public void checkDatesTest() throws SQLException {
		final String[] expectedDates = new String[5];
		expectedDates[0] = "2015-01-29";
		expectedDates[1] = "2015-02-12";
		expectedDates[2] = "2016-03-09";
		expectedDates[3] = "2016-01-18";
		expectedDates[4] = "2015-01-17";

		final String[] names = new String[5];
		names[0] = "Suge Knight";
		names[1] = "Emile Hirsch";
		names[2] = "Austin Chumlee Russell";
		names[3] = "Track Palin";
		names[4] = "Don McLean";

		for (int i = 0; i < 5; ++i) {
			ResultSet rs = runQuery("SELECT Arrest_Date FROM celebs_arrests WHERE Name=?", names[i]);
			if (rs.next() && rs.next())
				assert rs.getString("Arrest_Date").equals(expectedDates[i]);
		}
	}

	@Test
	public void manipulateDatabase() throws SQLException {
		try (ResultSet rs = runQuery("SELECT * FROM celebs_arrests");) {
			writeMetadata(rs);
			writeResultSet(rs);
		} catch (final SQLException ¢) {
			throw ¢;
		}
		assertEquals(1, runUpdate("DELETE FROM celebs_arrests WHERE name = 'Emile Hirsch'"));
		try (ResultSet rs = runQuery("SELECT COUNT(*) AS count FROM celebs_arrests WHERE name = 'Justin Bieber'");) {
			if (rs.next())
				assertEquals(1, rs.getInt("count"));
		} catch (final Exception ¢) {
			throw ¢;
		}
	}

	@Test
	public void multipleParamsQueryTest() throws SQLException {
		try (ResultSet rs = runQuery("SELECT Name From celebs_arrests WHERE Reason LIKE CONCAT('%',?,'%')"
				+ "AND YEAR(Arrest_Date) = ? ORDER BY Name", new Object[] { "drunk driving", 2014 });) {
			if (rs.next())
				assertEquals("Justin Bieber", rs.getString(1));
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	private static void writeMetadata(final ResultSet s) throws SQLException {
		System.out.println("The columns in table" + s.getMetaData().getTableName(1) + "are:");
		for (int ¢ = 1; ¢ <= s.getMetaData().getColumnCount(); ++¢)
			System.out.println("Column " + ¢ + ": " + s.getMetaData().getColumnName(¢));
	}

	private static void writeResultSet(final ResultSet s) throws SQLException {

		while (s.next()) {

			final String name = s.getString("name");
			final String date = s.getString("arrest_date");
			final String reason = s.getString("reason");
			System.out.println("Name: " + name);
			System.out.println("Date: " + date);
			System.out.println("Reason: " + reason);
		}
	}
}
