package infoeval.main.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.sql.Date;

import infoeval.main.WikiData.Connector;

public class SqlQueriesRunner {
	private String query1;
	private String query2;
	private Connector conn;
	private String wikiURL = "https://en.wikipedia.org/?curid=";

	public SqlQueriesRunner() throws Exception {
		query1 = "SELECT basic_info.name,BirthDate,wikiPageID " + "FROM basic_info, WikiID "
				+ "WHERE YEAR(BirthDate) < ? " + "AND BirthPlace = ? "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name=basic_info.name LIMIT 1) ";
		query2 = "SELECT  basic_info.name,BirthPlace,DeathPlace,wikiPageID " + "FROM basic_info,WikiID "
				+ "WHERE DeathPlace IS NOT NULL " + "AND BirthPlace != DeathPlace "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name=basic_info.name LIMIT 1) ";
		conn = new Connector();
	}

	public List<TableEntry> runQuery(int queryNum, Optional<Object[]> params)
			throws SQLException, NoSuchElementException {
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		if (queryNum == 1)
			for (ResultSet rs = conn.runQuery(query1, params.get()); rs.next();) {
				String name = rs.getString(1);
				Date birthDate = rs.getDate(2);
				String wikiPageID = rs.getString(3);
				Calendar cal1 = Calendar.getInstance();
				cal1.set(Calendar.YEAR, 1912);
				cal1.set(Calendar.MONTH, Calendar.JUNE);
				cal1.set(Calendar.DAY_OF_MONTH, 23);
				res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, (Date) cal1.getTime()));
			}
		else {
			if (queryNum != 2)
				return null;
			for (ResultSet rs = conn.runQuery(query2); rs.next();) {
				String name = rs.getString(1), birthPlace = rs.getString(2), deathPlace = rs.getString(3),
						wikiPageID = rs.getString(4);
				Calendar cal1 = Calendar.getInstance();
				cal1.set(Calendar.YEAR, 1912);
				cal1.set(Calendar.MONTH, Calendar.JUNE);
				cal1.set(Calendar.DAY_OF_MONTH, 23);
				Date dummyDate = (Date) cal1.getTime();
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, dummyDate, dummyDate));
			}
		}
		return res;
	}
}
