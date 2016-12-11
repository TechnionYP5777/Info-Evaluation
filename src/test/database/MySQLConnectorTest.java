package test.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import org.junit.Test;

/*
 * @author Osher Hajaj
 * @since 6/12/2016
 * */
public class MySQLConnectorTest {
	private Connection conn;
	private Statement statement;
	private ResultSet rs;
	
	@Test
	public void readDatabase() throws Exception {
		try{

			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/celebsArrests";
			String user = "root";
			String pass = "mysqlpass";
			conn = DriverManager.getConnection(url,user,pass);
			statement = conn.createStatement();
			rs = statement.executeQuery("use celebsArrests");
			rs = statement.executeQuery("SELECT * FROM arrests");
			writeMetadata(rs);
			writeResultSet(rs);
			int affected = statement.executeUpdate("DELETE FROM arrests WHERE name = 'Soulja Boy'");
			Assert.assertEquals(0, affected);			
			rs = statement.executeQuery("SELECT COUNT(*) AS count FROM arrests WHERE name = 'Justin Bieber'");
			if (rs.next()) Assert.assertEquals(1,rs.getInt("count"));
		}catch(Exception e){
			throw e;
		}finally{
			close();
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

	private void close(){
		try{
			if(conn!=null) conn.close();
			if(statement!=null) statement.close();
			if(rs!=null) rs.close();
		}catch(Exception e){}
	}
}

