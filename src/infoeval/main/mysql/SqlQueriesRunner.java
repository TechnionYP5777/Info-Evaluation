package infoeval.main.mysql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import infoeval.main.WikiData.Connector;

/**
 * 
 * @author osherh
 * @Since 12-05-2017
 * 
 *        This class runs SQL queries on mysql server and returns a list of
 *        table entry as results
 *
 */
public class SqlQueriesRunner {
	private Connector conn;
	private String wikiURL = "https://en.wikipedia.org/?curid=";

	public SqlQueriesRunner() throws Exception {
		conn = new Connector();
	}

	public List<TableEntry> getBornInPlaceBeforeYear(String place, String year) throws SQLException {
		int yearVal = Integer.parseInt(year);
		final String yearPlaceQuery = "SELECT basic_info.name,BirthDate,wikiPageID FROM basic_info,WikiID "
				+ "WHERE YEAR(BirthDate) < ? AND BirthPlace = ? "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		Object[] inp = new Object[] { yearVal, place };
		ResultSet rs = conn.runQuery(yearPlaceQuery, inp);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		while (rs.next()) {
			String name = rs.getString(1);
			Date birthDate = rs.getDate(2);
			String wikiPageID = rs.getString(3);
			Calendar cal1 = Calendar.getInstance();
			cal1.set(Calendar.YEAR, 1912);
			cal1.set(Calendar.MONTH, Calendar.JUNE);
			cal1.set(Calendar.DAY_OF_MONTH, 23);
			res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, (Date) cal1.getTime()));
		}
		conn.closeConnection();
		return res;
	}

	public List<TableEntry> getDifferentDeathPlace() throws SQLException {
		final String birthDeathPlaceQuery = "SELECT basic_info.name,BirthPlace,DeathPlace,wikiPageID "
				+ "FROM basic_info,WikiID WHERE DeathPlace IS NOT NULL AND BirthPlace != DeathPlace "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		ResultSet rs = conn.runQuery(birthDeathPlaceQuery);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		while (rs.next()) {
			String name = rs.getString(1), birthPlace = rs.getString(2), deathPlace = rs.getString(3),
					wikiPageID = rs.getString(4);
			Calendar cal1 = Calendar.getInstance();
			cal1.set(Calendar.YEAR, 1912);
			cal1.set(Calendar.MONTH, Calendar.JUNE);
			cal1.set(Calendar.DAY_OF_MONTH, 23);
			Date dummyDate = (Date) cal1.getTime();
			res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, dummyDate, dummyDate));
		}
		conn.closeConnection();
		return res;
	}
}
