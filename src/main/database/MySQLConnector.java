package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * set up the connection to MySQL DB and provide a function to run a query  
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
    
    public int updateDB(String query) throws SQLException{
    	return conn.createStatement().executeUpdate(query);
    }    
    
    public ResultSet runQuery(String query) throws SQLException{
    	return conn.createStatement().executeQuery(query);
    }
    
    public void closeConnection(){
    	try{
    		if(conn!=null) conn.close();
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    }
}