package main.guiFrames;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import static main.database.MySQLConnector.*;

/**
 * This class implements list refinements on the GUI event table
 * 
 * @author Ward Mattar
 * @author osherh
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
	 * accepts a DefaultTableModel and list of events as a SQL query result and
	 * fills them in table
	 * 
	 * @throws SQLException
	 */
	private void fillEventsTable(DefaultTableModel m, ResultSet s) throws SQLException {
		if (m == null || s == null)
			return;
		while (m.getRowCount() != 0)
			m.removeRow(0);
		for (Object tempEvent[] = new Object[3]; s.next();) {
			tempEvent[0] = s.getString("Name");
			tempEvent[1] = s.getString("Arrest_Date");
			tempEvent[2] = s.getString("Reason");
			m.addRow(tempEvent);
		}
	}

	/**
	 * accepts a JTable and sorts the content of the table according to the
	 * given field name
	 * 
	 * @throws SQLException
	 */
	public void sortBy(DefaultTableModel m, String fieldName) throws SQLException {
		if (m != null)
			if ("none".equals(fieldName)) {
				ResultSet r = runQuery("SELECT * FROM celebs_arrests");
				fillEventsTable(m, r);
				r.close();
			} else {
				if (m == null || !fieldExist(fieldName))
					return;
				try {
					ResultSet r;
					switch (fieldName) {
					case "Date":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY UNIX_TIMESTAMP(arrest_date) DESC,reason,name");
						fillEventsTable(m, r);
						r.close();
						break;
					case "Name":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY name,UNIX_TIMESTAMP(arrest_date) DESC,reason");
						fillEventsTable(m, r);
						r.close();
						break;
					case "Reason":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY reason,name,UNIX_TIMESTAMP(arrest_date) DESC");
						fillEventsTable(m, r);
						r.close();
						break;
					}
				} catch (SQLException ¢) {
					throw ¢;
				}
			}
	}

	/**
	 * filters the content of the events table according to the given field name
	 * and value
	 * 
	 * @throws SQLException
	 */
	public void filterBy(DefaultTableModel m, String fieldName, String fieldValue) throws SQLException {
		if (m != null && fieldExist(fieldName) && fieldValue != null)
			try {
				ResultSet r;
				switch (fieldName) {
				case "Date":
					r = runQuery("SELECT * FROM celebs_arrests WHERE (YEAR(arrest_date) = " + fieldValue
							+ " ) ORDER BY arrest_date DESC");
					fillEventsTable(m, r);
					r.close();
					break;
				case "Name":
					r = runQuery("SELECT * FROM celebs_arrests WHERE name LIKE \"%" + fieldValue + "%\" ORDER BY name");
					fillEventsTable(m, r);
					r.close();
					break;
				case "Reason":
					r = runQuery(
							"SELECT * FROM celebs_arrests WHERE reason LIKE \"%" + fieldValue + "%\" ORDER BY reason");
					fillEventsTable(m, r);
					r.close();
					break;
				}
			} catch (SQLException ¢) {
				throw ¢;
			}
	}

	/**
	 *
	 * accepts a JComboBox <String> representing a drop menu and fills them with
	 * the category option list according to the categoryName
	 * 
	 * @throws SQLException
	 */
	private void fillMenu(JComboBox<String> s, String categoryName, ResultSet r) throws SQLException {
		if (s == null || r == null)
			return;
		s.removeAllItems();
		for (String temp = "N/A"; r.next();) {
			switch (categoryName) {
			case "Date":
				temp = r.getString("arrest_date");
				break;
			case "Name":
				temp = r.getString("name");
				break;
			case "Reason":
				temp = r.getString("reason");
				break;
			}
			s.addItem(String.valueOf(temp));
		}
	}

	/**
	 * fills a drop down menu list with a specific category values
	 * 
	 * @throws SQLException
	 */
	public void getCategory(JComboBox<String> s, String categoryName) throws SQLException {
		if (s != null && fieldExist(categoryName))
			try {
				ResultSet r;
				switch (categoryName) {
				case "Date":
					r = runQuery(
							"SELECT DISTINCT YEAR(arrest_date) FROM celebs_arrests ORDER BY arrest_date DESC, name, reason");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				case "Name":
					r = runQuery("SELECT DISTINCT name FROM celebs_arrests ORDER BY name, arrest_date DESC, reason");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				case "Reason":
					r = runQuery("SELECT DISTINCT reason FROM celebs_arrests ORDER BY reason, name, arrest_date DESC");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				}
			} catch (SQLException ¢) {
				throw ¢;
			}
	}
}
