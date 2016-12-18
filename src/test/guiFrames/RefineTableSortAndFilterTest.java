package test.guiFrames;

import junit.framework.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Test;
import main.guiFrames.RefineTable;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import org.junit.Before;
import org.junit.Ignore;
import static main.database.MySQLConnector.*;

/**
 * Tests for main.guiFrames.RefineTable
 * 
 * @author ward
 * @author osherh
 */
public class RefineTableSortAndFilterTest {

	@Before
	public void init() throws SQLException {
		updateDB("DELETE FROM celebs_arrests");
		updateDB(
				"INSERT INTO celebs_arrests values('Justin Bieber','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
		updateDB("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges');");
		updateDB(
				"INSERT INTO celebs_arrests values('Don McLean','2015-01-17','misdemeanor domestic violence charges');");
		updateDB("INSERT INTO celebs_arrests values('Flavor Flav','2014-01-09','speeding while driving');");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2016-03-9','sexual assault');");
	}

	// TODO: osherh - fix this test
	@Ignore
	@Test
	public void refineByNameTest() throws SQLException, Exception {
		DefaultTableModel expectedTable = new DefaultTableModel(
				new Object[][] {
						{ "Justin Bieber", "2014-01-23",
								"suspicion of driving under the influence and driving with an expired license" },
						{ "Flavor Flav", "2014-01-09", "speeding while driving" },
						{ "Emile Hirsch", "2015-02-12", "assault charges" },
						{ "Don McLean", "2015-01-17", "misdemeanor domestic violence charges" },
						{ "Chris Kattan", "2015-02-10", "suspicion of drunk driving" },
						{ "Austin Chumlee Russell", "2016-03-9", "sexual assault" } },
				new String[] { "Name", "Date", "Reason" });
		RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		assert (rt.fieldExist("Date"));
		DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Name");
		for (int i = 0; i < 6; ++i)
			for (int j = 0; j < 3; ++j)
				Assert.assertEquals(outputTable.getValueAt(i, j), expectedTable.getValueAt(i, j));
		closeConnection();
	}
}