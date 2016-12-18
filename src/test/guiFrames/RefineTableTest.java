package test.guiFrames;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import org.junit.Test;
import main.guiFrames.RefineTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import main.database.MySQLConnector;
import org.junit.Before;
import org.junit.Ignore;

import junit.framework.Assert;
import main.guiFrames.RefineTable;

/**
 * Tests for main.guiFrames.RefineTable
 * 
 *  @author ward
 *	@author osherh
 */
public class RefineTableTest {
	/**
	 * adds the field names of even table
	 */

	private MySQLConnector c; 

	private static void addEventFieldNames(RefineTable ¢) {
		¢.addField("Name");
		¢.addField("Date");
		¢.addField("Reason");
	}

	@Test
	public void testAddFieldNullSafe() {
		(new RefineTable()).addField(null);
	}

	@Test
	public void testAddField() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		ArrayList<String> f = new ArrayList<>();
		f.add("Name");
		f.add("Date");
		f.add("Reason");
		assertEquals(t.getFields(), f);
	}

	@Test
	public void testRemoveFieldNullSafe() {
		(new RefineTable()).removeField(null);
	}

	@Test
	public void testRemoveField() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		t.removeField("Name");
		t.removeField("Date");
		t.removeField("Reason");
		assertEquals(t.getFields(), new ArrayList<>());
	}

	@Test
	public void testFieldExistsNullSafe() {
		assertFalse(new RefineTable().fieldExist(null));
	}

	@Test
	public void testFieldExists() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		assertTrue(t.fieldExist("Name"));
		assertTrue(t.fieldExist("Reason"));
		t.removeField("Name");
		t.removeField("Reason");
		assertFalse(t.fieldExist("Name"));
		assertFalse(t.fieldExist("Reason"));

		assertTrue(t.fieldExist("Date"));
	}
	
	@Before
	public void init() throws Exception{
		try{
			c = new MySQLConnector();
		}catch(Exception e){
			throw e;
		}
			c.updateDB("DELETE FROM arrests");
			c.updateDB("INSERT INTO arrests values('Justin Bieber','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
			c.updateDB("INSERT INTO arrests values('Emile Hirsch','2015-02-12','assault charges');");
			c.updateDB("INSERT INTO arrests values('Don McLean','2015-01-17','misdemeanor domestic violence charges');");
			c.updateDB("INSERT INTO arrests values('Flavor Flav','2014-01-09','speeding while driving');");
			c.updateDB("INSERT INTO arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");
			c.updateDB("INSERT INTO arrests values('Austin Chumlee Russell','2016-03-9','sexual assault');");
	}
	
	//TODO: osherh - fix this test
	@Ignore
	@Test
	public void refineByNameTest() throws SQLException,Exception{
		DefaultTableModel expectedTable = new DefaultTableModel
				(new Object[][] {
					{"Justin Bieber","2014-01-23","suspicion of driving under the influence and driving with an expired license"},
					{"Flavor Flav","2014-01-09","speeding while driving"},									
					{"Emile Hirsch","2015-02-12","assault charges"},
					{"Don McLean","2015-01-17","misdemeanor domestic violence charges"},
					{"Chris Kattan","2015-02-10","suspicion of drunk driving"},
					{"Austin Chumlee Russell","2016-03-9","sexual assault"}},
				new String[] { "Name", "Date", "Reason" });	
		RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		Assert.assertTrue(rt.fieldExist("DATE"));
		DefaultTableModel outputTable = new DefaultTableModel
		(new Object[][] {{""}},
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(c, outputTable, "Name");	
		for(int i=0; i<6; ++i)
			for(int j=0;j<3;++j)
				Assert.assertEquals(outputTable.getValueAt(i,j),expectedTable.getValueAt(i,j));
		c.closeConnection();
	}
}