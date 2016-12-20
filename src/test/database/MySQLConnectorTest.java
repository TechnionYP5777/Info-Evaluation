package test.database;

import static main.database.MySQLConnector.addEvents;
import static main.database.MySQLConnector.closeConnection;
import static main.database.MySQLConnector.getConnection;
import static main.database.MySQLConnector.runQuery;
import static main.database.MySQLConnector.updateDB;
import static org.junit.Assert.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import main.database.DataList;
import main.database.MySQLConnector;

/*
 * @author osherh
 * @since 6/12/2016
 * */
public class MySQLConnectorTest {
	public MySQLConnectorTest() throws Exception {
		new MySQLConnector();
	}

	@AfterClass
	public static void disconnect() {
		closeConnection();
	}

	@Before
	public void initializeDatabase() throws SQLException {
		updateDB("DELETE FROM celebs_arrests;");
		updateDB(
				"INSERT INTO celebs_arrests values('Justin Bieber','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
		updateDB("INSERT INTO celebs_arrests values('Flavor Flav','2014-01-09','speeding while driving');");
		updateDB("INSERT INTO celebs_arrests values('Soulja Boy','2014-01-22','possession of a loaded gun');");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");

		final DataList list = new DataList();
		list.insert("Suge Knight", "01/29/2015", "involved in a fatal hit run");
		list.insert("Emile Hirsch", "02/12/2015", "assualt charges");
		list.insert("Austin Chumlee Russell", "03/09/2016", "sexual assault");
		list.insert("Track Palin", "01/18/2016",
				"charges of fourth-degree assault and interfering with the report of a domestic violence crime");
		list.insert("Don McLean", "01/17/2015", "misdemeanor domestic violence charges");
		addEvents(list);
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
			final PreparedStatement ps = getConnection()
					.prepareStatement("SELECT Arrest_Date FROM celebs_arrests WHERE Name=?");
			ps.setString(1, names[i]);
			final ResultSet rs = ps.executeQuery();
			if (rs.next())
				assertEquals(rs.getDate("Arrest_Date") + "", expectedDates[i]);
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
		assertEquals(1, updateDB("DELETE FROM celebs_arrests WHERE name = 'Emile Hirsch'"));
		try (ResultSet rs = runQuery("SELECT COUNT(*) AS count FROM celebs_arrests WHERE name = 'Justin Bieber'");) {
			if (rs.next())
				assertEquals(1, rs.getInt("count"));
		} catch (final Exception ¢) {
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
