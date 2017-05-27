package infoeval.main.mysql;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import infoeval.main.WikiData.Connector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author osherh , Moshe
 * @Since 12-05-2017 This class runs SQL queries on mysql server and returns a
 *        list of table entry as results [[SuppressWarningsSpartan]]
 */

public class SqlRunner {
	private Connector conn;
	QueryResultsSerializer resultsSer;
	private String wikiURL = "https://en.wikipedia.org/?curid=";
	private static final Logger logger = Logger.getLogger("SqlRunner".getClass().getName());

	public SqlRunner() throws Exception {
		conn = new Connector();
		resultsSer = new QueryResultsSerializer();
		conn.runUpdate("CREATE TABLE IF NOT EXISTS serialized_query_results "
				+ "(serialized_id int(30) NOT NULL AUTO_INCREMENT, " + "query_identifier VARCHAR(100) NOT NULL, "
				+ "serialized_result LONGBLOB, " + "PRIMARY KEY (serialized_id))");
		logger.log(Level.INFO, "serialized reulst table created successfully");
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// @RequestMapping("Queries/Query2_real")
	public ArrayList<TableEntry> getBornInPlaceBeforeYear(String place, String year)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { place,year };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE CONCAT('getBornInPlaceYear(',?,?,')')", inp);
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>() ; 
		if (id_result.isEmpty()) {
//			final String beforeYearInPlace = "SELECT SQL_CACHE filtered_info.name,filtered_info.BirthDate,wikiPageID "
//					+ "FROM basic_info,WikiID " + " WHERE BirthPlace = ? AND YEAR(BirthDate) < ?  "
//					+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
			final String beforeYearInPlace = "SELECT SQL_CACHE filtered_info.name,filtered_info.BirthDate,WikiID.wikiPageID "
					+"FROM (SELECT * FROM basic_info WHERE BirthPlace = ? AND YEAR(BirthDate) < ?) AS filtered_info  "
					+ "LEFT JOIN WikiID "
					+ "ON WikiID.name = filtered_info.name ;\n";
			logger.log(Level.INFO, "Born and died in different place is being executed");
			rows = conn.runQuery(beforeYearInPlace, inp);
			String query_identifier = "getBornInPlaceYear" + "(" + place + year + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} 
			else {
			Integer tmp_serialID =  (Integer) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			serialized_id = new Long(tmp_serialID);
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			String wikiPageID = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name, place, "", birthDate, sqlDate, "", "", ""));
		}
		return res;
	}

	public ArrayList<TableEntry> getDifferentDeathPlace()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE qeury_identifier = " + "getDifferentDeathPlace()");
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>();
		if (id_result.isEmpty()) {
			final String birthDeathPlace = "SELECT SQL_CACHE basic_info.name,BirthPlace,DeathPlace,wikiPageID "
					+ "FROM basic_info,WikiID " + "WHERE DeathPlace != 'No Death Place' "
					+ "AND BirthPlace != DeathPlace "
					+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
			logger.log(Level.INFO, "Different birth and death place is being executed");
			rows = conn.runQuery(birthDeathPlace);
			String query_identifier = "getDifferentDeathPlace()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (long) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		int i = 1;
		for (Row row : rows) {
			logger.log(Level.INFO, "record num " + i + " added");
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			String deathPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String wikiPageID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, sqlDate, sqlDate, "", "", ""));
			++i;
		}
		return res;
	}

	public ArrayList<TableEntry> getSameOccupationCouples()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE qeury_identifier = " + "getSameOccupationCouples()");
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>();
		if (id_result.isEmpty()) {
			final String sameOccupationCouples = "SELECT SQL_CACHE name,spouseName,occupation,spouseOccupation "
					+ "FROM basic_info "
					+ "WHERE spouseName != 'No Spouse Name' AND spouseOccupation != 'No Spouse Occupation' "
					+ "AND occupation = spouseOccupation";
			logger.log(Level.INFO, "Same occupation couples is being executed");
			rows = conn.runQuery(sameOccupationCouples);
			String query_identifier = "getSameOccupationCouples()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (long) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}

		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			String spouseName = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			String occupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String spouseOoccupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry("", name, "", "", sqlDate, sqlDate, occupation, spouseName, spouseOoccupation));
		}
		return res;
	}

	public ArrayList<TableEntry> getSameBirthPlaceCouples()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE qeury_identifier = " + "getSameBirthPlaceCouples()");
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>();
		if (id_result.isEmpty()) {
			final String sameBirthPlaceCouples = "SELECT SQL_CACHE B1.name AS Name,B2.name AS SpouseName,B1.birthPlace AS BirthPlace "
					+ "FROM basic_info B1, basic_info B2 "
					+ "WHERE B1.spouseName = B2.name AND B2.spouseName = B1.name "
					+ "AND B1.birthPlace = B2.birthPlace";
			logger.log(Level.INFO, "same birth place couples is being executed");
			rows = conn.runQuery(sameBirthPlaceCouples);
			String query_identifier = "getSameBirthPlaceCouples()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (long) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			String spouseName = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			String birthPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry("", name, birthPlace, "", sqlDate, sqlDate, "", spouseName, ""));
		}
		return res;
	}

	public ArrayList<TableEntry> getOccupationBetweenYears(String year1, String year2, String occupation)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { year1, year2, occupation };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE qeury_identifier LIKE CONCAT('getOccupationBetweenYears(',?,?,?,')')",inp);
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>();
		if (id_result.isEmpty()) {
			final String occupationBetweenYears = "SELECT SQL_CACHE name,birthDate,deathDate,wikiPageId "
					+ "FROM basic_info, WikiID " + "WHERE deathDate IS NOT NULL "
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? "
					+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
			logger.log(Level.INFO, "occupation between years is being executed");
			rows = conn.runQuery(occupationBetweenYears, inp);
			String query_identifier = "getOccupationBetweenYears(" + year1 + "," + year2 + "," + occupation + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (long) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			Date deathDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String wikiPageID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", ""));
		}
		return res;
	}

	public ArrayList<TableEntry> getSpouselessBetweenYears(String year1, String year2)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { year1, year2 };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE qeury_identifier LIKE CONCAT('getSpouselessBetweenYears','(',?,?,')')",inp);
		long serialized_id = -1;
		ArrayList<Row> rows=new ArrayList<>();
		if (id_result.isEmpty()) {
			final String spouselessBetweenYears = "SELECT SQL_CACHE name,birthDate,deathDate,occupation,wikiPageId "
					+ "FROM basic_info, WikiID " + "WHERE deathDate IS NOT NULL "
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? " + "AND spouseName = 'No Spouse Name' "
					+ "AND occupation = ? "
					+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
			logger.log(Level.INFO, "spouseless between years is being executed");
			rows = conn.runQuery(spouselessBetweenYears, inp);
			String query_identifier = "getSpouselessBetweenYears(" + year1 + "," + year2 + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (long) id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			Date deathDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String occupation = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			String wikiPageID = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", ""));
		}
		return res;
	}

	public ArrayList<Row> runQuery(String s, Object[] inp) throws ClassNotFoundException, SQLException, IOException {
		return conn.runQuery(s, inp);
	}
	
	public Connector getConnector(){
		return conn;
	}
}
