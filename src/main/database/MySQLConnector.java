package main.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import main.database.DataList;

/**
 * set up the connection to MySQL DB and provide a function to run a query
 * 
 * @author osherh
 * @since 6/12/2016
 */
public class MySQLConnector {
	private static Connection conn;
	private boolean dbExists;

	public MySQLConnector() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "mysqlpass");
		try (ResultSet r = runQuery("SHOW DATABASES;");) {
			while (r.next())
				if ("events".equals(r.getString(1)))
					dbExists = true;
			if (!dbExists) {
				givePermissions();
				createDatabase();
			}
			runQuery("use events;");
		} catch (SQLException ¢) {
			throw ¢;
		}
	}

	private void createDatabase() throws SQLException {
		updateDB("CREATE database events");
		updateDB("CREATE TABLE celebs_arrests(" + "NAME VARCHAR(30) NOT NULL," + "ARREST_DATE DATE NOT NULL,"
				+ "REASON VARCHAR(150) NOT NULL," + "PRIMARY KEY (NAME,ARREST_DATE,REASON));");
	}

	private void givePermissions() throws SQLException {
		updateDB("CREATE USER root IDENTIFIED BY 'mysqlpass';");
		runQuery("grant all privileges on *.* to root@localhost;");
	}

	public static Connection getConnection() {
		return conn;
	}

	public static int updateDB(String query) throws SQLException {
		return conn.createStatement().executeUpdate(query);
	}

	public static ResultSet runQuery(String query) throws SQLException {
		return conn.createStatement().executeQuery(query);
	}

	private static java.sql.Date utilDateToSQLDateConvertor(java.util.Date utilDate) {
		return java.sql.Date.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(utilDate));
	}

	public static void addEvents(DataList lin) {
		for (TableTuple tuple : lin)
			try (PreparedStatement ps = conn.prepareStatement("INSERT INTO celebs_arrests values (?,?,?);");) {
				ps.setString(1, tuple.getName());
				ps.setDate(2, utilDateToSQLDateConvertor(tuple.getRegularDate()));
				ps.setString(3, tuple.getReason());
				ps.executeUpdate();
			} catch (SQLException ¢) {
				¢.printStackTrace();
			}
	}

	public static void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException ¢) {
			¢.printStackTrace();
		}
	}
}