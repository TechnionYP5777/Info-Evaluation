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
			tempEvent[0] = s.getObject("Name");
			tempEvent[1] = s.getObject("Date");
			tempEvent[2] = s.getObject("Reason");
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
		@SuppressWarnings("unused")
		ArrayList<String> events;
		try{
			ResultSet r;
			switch (fieldName) {
				case "Date":
					r = c.runQuery("SELECT * FROM celebsArrests ORDER BY UNIX_TIMESTAMP(date) DESC");
					fillEventsTable(m,r); 
					r.close();
					break;
				case "Name":
					r = c.runQuery("SELECT * FROM celebsArrests ORDER BY name DESC");
					fillEventsTable(m,r); 					
					r.close();					
					break;
				case "Reason":
					r = c.runQuery("SELECT * FROM celebsArrests ORDER BY reason");
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
	 */
	public void filterBy(DefaultTableModel m, String fieldName, String fieldValue) {
		if (m != null && fieldExist(fieldName) && fieldValue != null)
			switch (fieldName) {
			case "Date":
				break;
			case "Name":
				break;
			case "Reason":
				break;
			}

	}

	public RefineTable() {
		fields = new ArrayList<String>();
	}
}