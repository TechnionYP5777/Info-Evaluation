package main.guiFrames;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import main.database.MySQLConnector;

import java.util.ArrayList;

/**
 * This class implements list refinements on the GUI event table
 * 
 * @author Ward Mattar
 */
public class RefineTable {
	public RefineTable() {
		fields = new ArrayList<String>();
	}
	private ArrayList<String> fields;

	public ArrayList<String> getFields() {
		return fields;
	}

	public void addField(String name) {
		if (name != null && !"".equals(name))
			fields.add(name);
	}

	public void removeField(String name) {
		if (fieldExist(name))
			fields.remove(name);
	}

	public boolean fieldExist(String name) {
		return name != null && !"".equals(name) && fields.contains(name);
	}

	/**
	 *
	 * accepts a DefaultTableModel and list of events as a SQL query result and fills them
	 * in table
	 * 
	 * @throws SQLException
	 */
	public void fillEventsTable(DefaultTableModel m, ResultSet s) throws SQLException {
		if (m == null || s == null)
			return;
		for (int ¢ = 0; ¢ < m.getRowCount(); ++¢)
			m.removeRow(¢);
		for (Object tempEvent[] = new Object[3]; s.next();) {
			tempEvent[0] = s.getObject("name");
			tempEvent[1] = s.getObject("arrest_date");
			tempEvent[2] = s.getObject("reason");
			m.addRow(tempEvent);
		}
	}

	 /* accepts a JTable and sorts the content of the table according to the
	 * given field name
	 * @throws SQLException
	 */
	public void sortBy(MySQLConnector c, DefaultTableModel m, String fieldName) throws SQLException {
		if (m == null || !fieldExist(fieldName))
			return;
		try{
			ResultSet r;
			switch (fieldName) {
				case "Date":
					r = c.runQuery("SELECT * FROM celebs_arrests ORDER BY UNIX_TIMESTAMP(arrest_date) DESC");
					fillEventsTable(m,r); 
					r.close();
					break;
				case "Name":
					r = c.runQuery("SELECT * FROM celebs_arrests ORDER BY name");
					fillEventsTable(m,r); 					
					r.close();					
					break;
				case "Reason":
					r = c.runQuery("SELECT * FROM celebs_arrests ORDER BY reason");
					fillEventsTable(m,r); 					
					r.close();
					break;	
			}
		}catch(SQLException e){
			throw e;
		}
	}

	/**
	 * filters the content of the events table according to the given field name
	 * and value
	 * @throws SQLException 
	 */
	public void filterBy(MySQLConnector c, DefaultTableModel m, String fieldName, String fieldValue) throws SQLException {
		if (m != null && fieldExist(fieldName) && fieldValue != null)
			try{
				ResultSet r;
				switch (fieldName) {
					case "Date":
						r = c.runQuery("SELECT * FROM celebs_arrests WHERE (YEAR(arrest_date) = " + fieldValue + " ) ORDER BY arrest_date DESC");
						fillEventsTable(m,r); 
						r.close();
						break;
					case "Name":
						r = c.runQuery("SELECT * FROM celebs_arrests WHERE name LIKE \"%" + fieldValue + "%\" ORDER BY name");
						fillEventsTable(m,r); 					
						r.close();					
						break;
					case "Reason":
						r = c.runQuery("SELECT * FROM celebs_arrests WHERE reason LIKE \"%" + fieldValue + "%\" ORDER BY reason");
						fillEventsTable(m,r); 					
						r.close();
						break;	
				}
			}catch(SQLException e){
				throw e;
			}
	}



	/**
	 *
	 * accepts a ListArray<String> representing a drop menu fills them
	 * with the category option list
	 * @throws SQLException
	 */
	public void fillMenu(ArrayList<String> m, String categoryName, ResultSet s) throws SQLException {
		if (m == null || s == null)
			return;
		for (String temp = "N/A"; s.next();) {
			switch(categoryName) {
				case "Date":
					temp = s.getString("arrest_date");
					break;
				case "Name":
					temp = s.getString("name");					
					break;
				case "Reason":
					temp = s.getString("reason");
					break;	
			}
			m.add(new String(temp));
		}
	}
	
	/**
	 * fills a drop down menu list with a specific category values
	 * @throws SQLException
	 **/
	public void getCategory(MySQLConnector c, DefaultTableModel m, String categoryName) throws SQLException {
		if (m != null && fieldExist(categoryName))
			try{
				ResultSet r;
				switch (categoryName) {
					case "Date":
						r = c.runQuery("SELECT YEAR(arrest_date) FROM celebs_arrests ORDER BY arrest_date DESC, name, reason");
						fillEventsTable(m,r); 
						r.close();
						break;
					case "Name":
						r = c.runQuery("SELECT name FROM celebs_arrests ORDER BY name, arrest_date DESC, reason");
						fillEventsTable(m,r); 					
						r.close();					
						break;
					case "Reason":
						r = c.runQuery("SELECT reason FROM celebs_arrests ORDER BY reason, name, arrest_date DESC");
						fillEventsTable(m,r); 					
						r.close();
						break;	
				}
			}catch(SQLException e){
				throw e;
			}
	}
		
}