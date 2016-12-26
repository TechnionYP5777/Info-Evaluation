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
				final ResultSet r = runQuery(
						"SELECT * FROM celebs_arrests ORDER BY Name,UNIX_TIMESTAMP(Arrest_Date) DESC,Reason");
				fillEventsTable(m, r);
				r.close();
			} else {
				if (!fieldExist(fieldName))
					return;
				try {
					ResultSet r;
					switch (fieldName) {
					case "Date":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY UNIX_TIMESTAMP(Arrest_Date) DESC,Name, Reason");
						fillEventsTable(m, r);
						r.close();
						break;
					case "Name":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY Name,UNIX_TIMESTAMP(Arrest_Date) DESC,Reason");
						fillEventsTable(m, r);
						r.close();
						break;
					case "Reason":
						r = runQuery(
								"SELECT * FROM celebs_arrests ORDER BY Reason,Name,UNIX_TIMESTAMP(Arrest_Date) DESC");
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
							"SELECT * FROM celebs_arrests WHERE name LIKE CONCAT('%',?,'%') ORDER BY Name, UNIX_TIMESTAMP(Arrest_Date) DESC, Reason",
							fieldValue);
					fillEventsTable(m, r);
					r.close();
					break;
				case "Year":
					r = runSafeQuery(
							"SELECT * FROM celebs_arrests WHERE YEAR(arrest_date) = ? ORDER BY UNIX_TIMESTAMP(Arrest_Date) DESC, Name, Reason",
							fieldValue);
					fillEventsTable(m, r);
					r.close();
					break;
				case "Reason":
					r = runSafeQuery(
							"SELECT * FROM celebs_arrests WHERE reason LIKE CONCAT('%',?,'%') ORDER BY Reason, Name, UNIX_TIMESTAMP(Arrest_Date) DESC",
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
	private void fillMenu(final JComboBox<String> s, final ResultSet r) throws SQLException {
		if (s == null || r == null)
			return;
		s.removeAllItems();
		while (r.next())
			s.addItem(String.valueOf(r.getString(1)));
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
							"SELECT DISTINCT YEAR(Arrest_Date) FROM celebs_arrests ORDER BY YEAR(Arrest_Date) DESC");
					fillMenu(s, r);
					r.close();
					break;
				case "Name":
					r = runQuery("SELECT DISTINCT Name FROM celebs_arrests ORDER BY Name");
					fillMenu(s, r);
					r.close();
					break;
				case "Reason":
					r = runQuery("SELECT DISTINCT Reason FROM celebs_arrests ORDER BY Reason");
					fillMenu(s, r);
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
			fillMenu(s, runSafeQuery("SELECT ? FROM celebs_arrests GROUP BY ? ORDER BY COUNT(?) DESC LIMIT ?",
					(new Object[] { fieldName, fieldName, fieldName, k })));
	}

	public void removeAllEvents(DefaultTableModel ¢) {
		while (¢.getRowCount() != 0)
			¢.removeRow(0);
	}
}
