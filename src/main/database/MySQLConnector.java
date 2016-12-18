package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import main.database.DataList;

/*
 * set up the connection to MySQL DB and provide a function to run a query  
 * @author osherh
 * @since 6/12/2016
 * */
public class MySQLConnector {
	private static Connection conn;

	public MySQLConnector() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "mysqlpass");
		createDatabaseAndTable();
		givePermissions();
	}

	private void createDatabaseAndTable() throws SQLException {
		updateDB("CREATE database events");
		runQuery("use events;");
		updateDB("CREATE TABLE celebs_arrests(" + "NAME VARCHAR(30) NOT NULL," + "ARREST_DATE DATE NOT NULL,"
				+ "REASON VARCHAR(150) NOT NULL," + "PRIMARY KEY (NAME,ARREST_DATE,REASON));");
	}

	private void givePermissions() throws SQLException {
		updateDB("CREATE USER root IDENTIFIED BY 'mysqlpass';");
		runQuery("grant usage on *.* to root@localhost IDENTIFIED BY 'mysqlpass';");
		runQuery("grant all privileges on events.* to root@localhost;");
	}

	/**
	 * This function updates the DB using INSERT/DELETE/UPDATE or DLL Statements
	 */
	public static int updateDB(String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public static ResultSet runQuery(String query) throws SQLException {
		return conn.prepareStatement(query).executeQuery(query);
	}

	public static void insertListToDB(DataList lin) {
		for (TableTuple tuple : lin)
			try (PreparedStatement ps = conn.prepareStatement("INSERT INTO celebs_arrests values (?,?,?);");) {
				ps.setString(1, tuple.getName());
				ps.setDate(2, new Date(tuple.getRegularDate().getTime()));
				ps.setString(3, tuple.getReason());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closeConnection() {
		try {
			updateDB("DROP database events");
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}