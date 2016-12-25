package main.guiFrames;

import static main.database.MySQLConnector.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 * This class implements list refinements on the GUI event table
 *
 * @author Ward Mattar
 * @author osherh
 */
public class RefineTable {
	private final ArrayList<String> fields;

	public RefineTable() {
		fields = new ArrayList<>();
	}

	public ArrayList<String> getFields() {
		return fields;
	}

	public void addField(final String name) {
		if (name != null && !"".equals(name))
			fields.add(name);
	}

	public void removeField(final String name) {
		if (fieldExist(name))
			fields.remove(name);
	}

	public boolean fieldExist(final String name) {
		return name != null && !"".equals(name) && fields.contains(name);
	}

	/**
	 *
	 * accepts a DefaultTableModel and list of events as a SQL query result and
	 * fills them in table
	 *
	 * @throws SQLException
	 */
	private void fillEventsTable(final DefaultTableModel m, final ResultSet s) throws SQLException {
		if (m == null || s == null)
			return;
		removeAllEvents(m);
		for (final Object tempEvent[] = new Object[3]; s.next();) {
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
	public void sortBy(final DefaultTableModel m, final String fieldName) throws SQLException {
		if (m != null)
			if ("none".equals(fieldName)) {
				final ResultSet r = runQuery("SELECT * FROM celebs_arrests");
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
				} catch (final SQLException ¢) {
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
	public void filterBy(final DefaultTableModel m, final String fieldName, final String fieldValue)
			throws SQLException {
		if (m != null && fieldExist(fieldName) && fieldValue != null)
			try {
				ResultSet r;
				switch (fieldName) {
				case "Name":
					r = runSafeQuery(
							"SELECT * FROM celebs_arrests WHERE name LIKE CONCAT('%',?,'%') ORDER BY name, UNIX_TIMESTAMP(Arrest_Date) DESC, reason",
							fieldValue);
					fillEventsTable(m, r);
					r.close();
					break;
				case "Year":
					r = runSafeQuery(
							"SELECT * FROM celebs_arrests WHERE YEAR(arrest_date) = ? ORDER BY UNIX_TIMESTAMP(Arrest_Date) DESC, reason, name",
							fieldValue);
					fillEventsTable(m, r);
					r.close();
					break;
				case "Reason":
					r = runSafeQuery(
							"SELECT * FROM celebs_arrests WHERE reason LIKE CONCAT('%',?,'%') ORDER BY reason, name, UNIX_TIMESTAMP(Arrest_Date) DESC",
							fieldValue);
					fillEventsTable(m, r);
					r.close();
					break;
				}
			} catch (final SQLException ¢) {
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
	private void fillMenu(final JComboBox<String> s, final String categoryName, final ResultSet r) throws SQLException {
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
	public void getCategory(final JComboBox<String> s, final String categoryName) throws SQLException {
		if (s != null && fieldExist(categoryName))
			try {
				ResultSet r;
				switch (categoryName) {
				case "Year":
					r = runQuery(
							"SELECT DISTINCT YEAR(arrest_date) FROM celebs_arrests ORDER BY YEAR(arrest_date) DESC");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				case "Name":
					r = runQuery("SELECT DISTINCT name FROM celebs_arrests");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				case "Reason":
					r = runQuery("SELECT DISTINCT reason FROM celebs_arrests");
					fillMenu(s, categoryName, r);
					r.close();
					break;
				}
			} catch (final SQLException ¢) {
				throw ¢;
			}
	}

	/**
	 * fills a drop down menu list with a specific category values
	 *
	 * @throws SQLException
	 */
	public void getMostCommon(final JComboBox<String> s, final String fieldName, int k) throws SQLException {
		if (s != null && fieldExist(fieldName))
			fillMenu(s, fieldName,
					runSafeQuery("SELECT ? FROM celebs_arrests GROUP BY ? ORDER BY COUNT(?) DESC LIMIT ?",
							(new Object[] { fieldName, fieldName, fieldName, k })));
	}

	public void removeAllEvents(DefaultTableModel ¢) {
		while (¢.getRowCount() != 0)
			¢.removeRow(0);
	}
}
