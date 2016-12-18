package test.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.AfterClass;

import main.database.DataList;
import main.database.MySQLConnector;

import static main.database.MySQLConnector.*;

/*
 * @author osherh
 * @since 6/12/2016
 * */
public class MySQLConnectorTest {
	public MySQLConnectorTest() throws Exception{
		new MySQLConnector();
	}
	
	@AfterClass
	public static void closeDatabase() throws Exception {
		closeConnection();
	}
	 	
	@Before
	public void initializeDatabase() throws Exception {
		updateDB("DELETE FROM celebs_arrests;");	
		updateDB("INSERT INTO celebs_arrests values('Justin Bieber','2014-01-23','suspicion of driving under the influence and driving with an expired license');");
		updateDB("INSERT INTO celebs_arrests values('Flavor Flav','2014-01-09','speeding while driving');");
		updateDB("INSERT INTO celebs_arrests values('Soulja Boy','2014-01-22','possession of a loaded gun');");
		updateDB("INSERT INTO celebs_arrests values('Chris Kattan','2015-02-10','suspicion of drunk driving');");
	
		DataList list = new DataList();
		list.insert("Suge Knight","29/01/2015","involved in a fatal hit run");		
		list.insert("Emile Hirsch","12/02/2015","assualt charges");
		list.insert("Austin Chumlee Russell","9/03/2016","sexual assault");
		list.insert("Track Palin","18/01/2016","charges of fourth-degree assault and interfering with the report of a domestic violence crime");
		list.insert("Don McLean","17/01/2015","misdemeanor domestic violence charges");
		addEvents(list);
	}
	
	@Test
	public void manipulateDatabase() throws Exception {
		try(ResultSet rs = runQuery("SELECT * FROM celebs_arrests");){
			writeMetadata(rs);
			writeResultSet(rs);
		}catch(SQLException e){
			throw e;
		}
		int affectedLines = updateDB("DELETE FROM celebs_arrests WHERE name = 'Emile Hirsch'");
		Assert.assertEquals(1, affectedLines);			
		try(ResultSet rs = runQuery("SELECT COUNT(*) AS count FROM celebs_arrests WHERE name = 'Justin Bieber'");){
			if (rs.next()) Assert.assertEquals(1,rs.getInt("count"));
		}catch(Exception e){
			throw e;
		}	
	}
	
	private static void writeMetadata(ResultSet s) throws SQLException{
		System.out.println("The columns in table" + s.getMetaData().getTableName(1) + "are:");
		for (int ¢ = 1; ¢<=s.getMetaData().getColumnCount(); ++	¢) 
			System.out.println("Column " + 	¢ + ": "+ s.getMetaData().getColumnName(¢));
	}

	private static void writeResultSet(ResultSet s) throws SQLException{
		while(s.next()){
			String name = s.getString("name");
			String date = s.getString("arrest_date");
			String reason = s.getString("reason");
			System.out.println("Name: " + name);
			System.out.println("Date: " + date);
			System.out.println("Reason: " + reason);		
		}
	}
}

