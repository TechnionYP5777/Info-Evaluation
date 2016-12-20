package test.guiFrames;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.After;
import org.junit.AfterClass;

import main.guiFrames.RefineTable;
import javax.swing.table.DefaultTableModel;
import main.database.MySQLConnector;
import static main.database.MySQLConnector.*;

/**
 * Tests for main.guiFrames.RefineTable
 * 
 * @author osherh
 */
public class RefineTableFunctionalityTest {

	@Before
	public void emptyDB() throws SQLException{
		updateDB("DELETE FROM celebs_arrests");
	}
	
	@AfterClass
	public static void disconnect(){
		closeConnection();
	}
	
	public RefineTableFunctionalityTest() throws Exception{
		new MySQLConnector();
	}
	
	public void initRefineByName() throws Exception {
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','drunk driving');");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault');");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce');");
		updateDB("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges');");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','misdemeanor domestic violence charges');");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','speeding while driving');");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault');");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");
	}

	@Test
	public void refineByNameTest() throws Exception {
		initRefineByName();
		DefaultTableModel expectedTable = new DefaultTableModel(
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

		RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		assert rt.fieldExist("Date");
		DefaultTableModel outputTable = new DefaultTableModel(new Object[][] { { "" } },
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Name");
		for (int i = 0; i < 9; ++i)
			for (int j = 0; j < 3; ++j)
				assertEquals(outputTable.getValueAt(i, j), expectedTable.getValueAt(i, j));
	}

	public void initRefineByDate() throws Exception {
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','suspicion of driving under the influence and driving with an expired license')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','driving without a licesnce')");
		updateDB("INSERT INTO celebs_arrests values('Emile Hirsch','2015-02-12','assault charges')");
		updateDB("INSERT INTO celebs_arrests values('Brad Pitt','2014-01-09','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-02-12','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving')");
	}

	@Test
	public void refineByDateTest() throws Exception{
		initRefineByDate();
		DefaultTableModel expectedTable = new DefaultTableModel(new String[][] {					
			{"Emile Hirsch","2015-02-12","assault charges"},	
			{"Ben Stiller","2015-02-12","driving without a licesnce"},
			{"Ben Stiller","2015-02-12","sexual assault"},
			{"Chris Kattan","2015-02-10","suspicion of drunk driving"},	
			{"Chris Kattan","2014-01-23","suspicion of driving under the influence and driving with an expired license"},
			{"Austin Chumlee Russell","2014-01-09","sexual assault"},
			{"Brad Pitt","2014-01-09","sexual assault"},			
			{"Hugh Jackman","2014-01-09","sexual assault"},
			{"Austin Chumlee Russell","2013-03-09","sexual assault"}}, 
			new String[] { "Name", "Date", "Reason" });	
		RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		DefaultTableModel outputTable = new DefaultTableModel
		(new Object[][] {{""}},
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Date");	
		for(int i=0; i<9; ++i)
			for(int j=0;j<3;++j)
				assertEquals(outputTable.getValueAt(i,j),expectedTable.getValueAt(i,j));
	}

	
	public void initRefineByReason() throws Exception {
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2013-03-09','sexual assault')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-03-11','driving without a licesnce')");
		updateDB("INSERT INTO celebs_arrests values('Austin Chumlee Russell','2014-01-09','sexual assult')");
		updateDB("INSERT INTO celebs_arrests values('Hugh Jackman','2014-01-09','sexual assualt')");
		updateDB("INSERT INTO celebs_arrests values('Emile Hirsch','2014-02-11','theft')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2016-02-12','sexual assualt')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2014-01-09','sexual assult')");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2014-01-23','drunk driving')");
		updateDB("INSERT INTO celebs_arrests values('Ben Stiller','2015-08-12','driving without a licesnce')");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2013-05-10','theft')");
	}
	
	//TODO fix this test @osherh
	@Ignore
	@Test
	public void refineByReasonTest() throws Exception{
		initRefineByReason();
		DefaultTableModel expectedTable = new DefaultTableModel(new String[][] {					
			{"Ben Stiller","2015-08-12","driving without a licesnce"},
			{"Ben Stiller","2015-03-11","driving without a licesnce"},
			{"Chris Kattan","2014-01-23","drunk driving"},
			{"Austin Chumlee Russell","2014-01-09","sexual assault"},
			{"Austin Chumlee Russell","2013-03-09","sexual assault"},
			{"Ben Stiller","2016-02-12","sexual assault"},
			{"Ben Stiller","2014-01-09","sexual assault"},
			{"Hugh Jackman","2014-01-09","sexual assault"},
			{"Chris Kattan","2013-05-10","theft"},
			{"Emile Hirsch","2014-02-11","theft"}},
			new String[] { "Name", "Date", "Reason"});	
		RefineTable rt = new RefineTable();
		rt.addField("Name");
		rt.addField("Date");
		rt.addField("Reason");
		DefaultTableModel outputTable = new DefaultTableModel
		(new Object[][] {{""}},
				new String[] { "Name", "Date", "Reason" });
		rt.sortBy(outputTable, "Reason");	
		for(int i=0; i<10; ++i)
			for(int j=0;j<3;++j){
				System.out.println(i + "" + j);
				assertEquals(outputTable.getValueAt(i,j),expectedTable.getValueAt(i,j));
			}
	}
	
	//TODO: filterBy test
	//TODO: most common test
	
}