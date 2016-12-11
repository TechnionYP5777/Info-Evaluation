package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * @author Osher Hajaj
 * @since 6/12/2016
 * */
public class MySQLConnector {
	private Connection conn;

    public MySQLConnector() throws Exception {
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/celebsArrests","root","mysqlpass");
        }catch(Exception e){
        	throw e;
        }
    }
    
    public void runQuery(String query) throws SQLException{
    	try(
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);){
			rs.close();
			statement.close();
		}catch(SQLException e){
    		throw e;
    	}
    }
    
    public void closeConnection(){
    	try{
    		if(conn!=null) conn.close();
    	}catch(SQLException e){}
    }
}