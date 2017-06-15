package infoeval.main.mysql;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import infoeval.main.WikiData.SqlTablesFiller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;

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
	private static final int LIMIT_NUM = 40;

	public SqlRunner() throws Exception {
		conn = new Connector();
		resultsSer = new QueryResultsSerializer();
		conn.runUpdate("CREATE TABLE IF NOT EXISTS serialized_query_results "
				+ "(serialized_id int(30) NOT NULL AUTO_INCREMENT, " + "query_identifier VARCHAR(100) NOT NULL, "
				+ "serialized_result LONGBLOB, " + "PRIMARY KEY (serialized_id))");
		logger.log(Level.INFO, "serialized reulst table created successfully");
	}

	public void clearSerializedQueries() throws Exception {
		conn.runUpdate("TRUNCATE TABLE serialized_query_results ");
		logger.log(Level.INFO, " CLEARED Serialized Reulst table successfully");
	}

	public void dropSerializedTable() throws Exception {
		conn.runUpdate("DROP TABLE serialized_query_results");
		logger.log(Level.INFO, "serialized results table dropped successfully");
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<TableEntry> getBornInPlaceBeforeYear(String place, String year)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { place, year };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE CONCAT('getBornInPlaceYear(',?,?,')')", inp);
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {
			final String beforeYearInPlace = "SELECT filtered_info.name, filtered_info.BirthPlace, filtered_info.BirthDate, filtered_info.photoLink, WikiID.wikiPageID "
					+ "FROM (SELECT * FROM basic_info WHERE BirthPlace LIKE CONCAT('%',?,'%') AND YEAR(BirthDate) < ?) AS filtered_info  "
					+ "LEFT JOIN WikiID " + "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;
			logger.log(Level.INFO, "Born in place before year query is being executed");
			rows = conn.runQuery(beforeYearInPlace, inp);
			String query_identifier = "getBornInPlaceYear" + "(" + place + year + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			// id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());

			Date birthDate = null;
			if (!"".equals(row.row.get(2).getKey())) {
				birthDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			}

			String photoLink = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, "", birthDate, sqlDate, "", "", "",
						photoLink, ""));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, "", birthDate, sqlDate, "", "", "",
						photoLink, ""));
			}
		}
		return res;
	}

	public ArrayList<TableEntry> getDifferentDeathPlace()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE 'differentDeathPlace()'");
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {
			final String birthDeathPlace = "SELECT filtered_info.name, filtered_info.BirthPlace, filtered_info.DeathPlace, filtered_info.photoLink, WikiID.wikiPageID "
					+ "FROM (SELECT * FROM basic_info WHERE DeathPlace != 'No Death Place' "
					+ " AND BirthPlace != DeathPlace) AS filtered_info  " + "LEFT JOIN WikiID "
					+ "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;
			logger.log(Level.INFO, "Different birth and death place query is being executed");
			rows = conn.runQuery(birthDeathPlace);
			String query_identifier = "getDifferentDeathPlace()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			// id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
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
			String photoLink = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, deathPlace, sqlDate, sqlDate, "", "",
						"", photoLink, ""));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, sqlDate, sqlDate, "", "", "",
						photoLink, ""));
			}
			++i;
		}
		return res;
	}

	public ArrayList<TableEntry> getSameOccupationCouples()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE 'getSameOccupationCouples()'");
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {
			final String sameOccupationCouples = "SELECT name,spouseName,occupation,spouseOccupation "
					+ "FROM basic_info "
					+ "WHERE spouseName != 'No Spouse Name' AND spouseOccupation != 'No Spouse Occupation' "
					+ "AND occupation = spouseOccupation " + "LIMIT " + LIMIT_NUM;
			logger.log(Level.INFO, "Same occupation couples query is being executed");
			rows = conn.runQuery(sameOccupationCouples);
			String query_identifier = "getSameOccupationCouples()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
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

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry("", newName, "", "", sqlDate, sqlDate, occupation, spouseName, spouseOoccupation,
						"", ""));
			} else {
				res.add(new TableEntry("", name, "", "", sqlDate, sqlDate, occupation, spouseName, spouseOoccupation,
						"", ""));
			}
		}
		return res;
	}

	public ArrayList<TableEntry> getSameBirthPlaceCouples()
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE 'getSameBirthPlaceCouples()'");
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {

			final String sameBirthPlaceCouples = ""
					+ "SELECT B1.name AS Name, basic_info.name AS SpouseName, B1.birthPlace AS BirthPlace "
					+ "FROM (SELECT * FROM basic_info " + "WHERE spouseName != 'No Spouse Name' "
					+ "AND birthPlace != 'No Birth Place') AS B1 " + "LEFT JOIN basic_info "
					+ "ON B1.spouseName = basic_info.name " + "AND basic_info.spouseName = B1.name "
					+ "AND B1.birthPlace = basic_info.birthPlace ";

			logger.log(Level.INFO, "same birth place couples query is being executed");
			rows = conn.runQuery(sameBirthPlaceCouples);
			String query_identifier = "getSameBirthPlaceCouples()";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			// id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
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

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry("", newName, birthPlace, "", sqlDate, sqlDate, "", spouseName, "", "", ""));
			} else {
				res.add(new TableEntry("", name, birthPlace, "", sqlDate, sqlDate, "", spouseName, "", "", ""));
			}
		}
		return res;
	}

	public ArrayList<TableEntry> getOccupationBetweenYears(String year1, String year2, String occupation)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { year1, year2, occupation };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE CONCAT('getOccupationBetweenYears(',?,?,?,')')", inp);
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {
			final String occupationBetweenYears = "SELECT filtered_info.name, filtered_info.birthDate, filtered_info.deathDate, filtered_info.photoLink, WikiID.wikiPageId "
					+ "FROM (SELECT * FROM basic_info " + "WHERE birthDate IS NOT NULL AND deathDate IS NOT NULL "
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? " + "AND occupation = ?) AS filtered_info "
					+ "LEFT JOIN WikiID " + "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;

			logger.log(Level.INFO, "occupation between years query is being executed");
			rows = conn.runQuery(occupationBetweenYears, inp);
			String query_identifier = "getOccupationBetweenYears(" + year1 + "," + year2 + "," + occupation + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			// id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
			@SuppressWarnings("unchecked")
			ArrayList<Row> rows2 = (ArrayList<Row>) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows.addAll(rows2);
		}
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rows) {
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey());
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			Date deathDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String photoLink = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry(wikiURL + wikiPageID, newName, "", "", birthDate, deathDate, occupation, "", "",
						photoLink, ""));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", "",
						photoLink, ""));
			}
		}
		return res;
	}

	public ArrayList<TableEntry> getSpouselessBetweenYears(String year1, String year2)
			throws SQLException, ClassNotFoundException, IOException, ParseException {
		Object[] inp = new Object[] { year1, year2 };
		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE CONCAT('getSpouselessBetweenYears','(',?,?,')')", inp);
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();
		if (id_result.isEmpty()) {
			final String spouselessBetweenYears = "SELECT filtered_info.name, filtered_info.birthDate, filtered_info.deathDate, filtered_info.occupation, filtered_info.photoLink, WikiID.wikiPageId "
					+ "FROM (SELECT * FROM basic_info WHERE birthDate IS NOT NULL AND deathDate IS NOT NULL "
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? " + "AND spouseName = 'No Spouse Name' "
					+ ") AS filtered_info " + "LEFT JOIN WikiID " + "ON WikiID.name = filtered_info.name " + "LIMIT "
					+ LIMIT_NUM;
			logger.log(Level.INFO, "spouseless between years query is being executed");
			rows = conn.runQuery(spouselessBetweenYears, inp);
			String query_identifier = "getSpouselessBetweenYears(" + year1 + "," + year2 + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, rows);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			// id_result.get(0).row.get(0).getValue().cast(id_result.get(0).row.get(0).getKey());
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
			String photoLink = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(5).getValue().cast(row.row.get(5).getKey());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry(wikiURL + wikiPageID, newName, "", "", birthDate, deathDate, occupation, "", "",
						photoLink, ""));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", "",
						photoLink, ""));
			}
		}
		return res;
	}

	public TableEntry getPersonalInfoFromDBpedia(int wikiPageID)
			throws ClassNotFoundException, SQLException, IOException, ParseException {
		Extractor ext = new Extractor(wikiPageID);
		logger.log(Level.INFO, "abstract extraction query is being executed");
		ext.executeQuery(QueryTypes.ABSTRACT_BY_WIKI_PAGE_ID);
		ResultSetRewindable results = ext.getResults();

		results.reset();
		QuerySolution solution = results.nextSolution();
		RDFNode overview = solution.get("abstract");
		String overviewStr = "No Abstract";
		if (overview != null)
			if (overview.isResource())
				overviewStr = (overview.asResource() + "").split("resource/")[1];
			else if (overview.isLiteral())
				overviewStr = (overview.asLiteral() + "").split("@")[0];

		logger.log(Level.INFO, "Basic Info By Wiki Page ID extraction query is being executed");
		ext.executeQuery(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID);
		ResultSetRewindable basicInfoByNameResults = ext.getResults();
		basicInfoByNameResults.reset();

		SqlTablesFiller filler = new SqlTablesFiller();
		TableEntry te = filler.getInfo(basicInfoByNameResults);
		filler.close();

		TableEntry result = new TableEntry(te);
		String name = result.getName();
		System.out.println(name);
		//String newName = name.replaceAll("_", " ");
		result.setName(name);

		result.setUrl("");

		result.setOverview(overviewStr);

		String photoLink = result.getPhotoLink();
		//photoLink.replaceAll("'", "\'");
		result.setPhotoLink(photoLink);

		return result;
	}

	@SuppressWarnings("unchecked")
	public TableEntry getPersonalInfo(int wikiPageID)
			throws ClassNotFoundException, SQLException, IOException, ParseException {
		Object[] inp = new Object[] { wikiPageID };
		ArrayList<Row> res = conn.runQuery("SELECT wikiPageID FROM WikiID WHERE wikiPageID = ?", inp);
		// String newName = WordUtils.capitalize(name).replaceAll(" ", "_");
		if (res.isEmpty()) {
			return getPersonalInfoFromDBpedia(wikiPageID);
		}

		ArrayList<Row> id_result = conn.runQuery("SELECT serialized_id " + "FROM serialized_query_results "
				+ "WHERE query_identifier LIKE CONCAT('getPersonalInfo','(',?,')')", inp);
		int serialized_id = -1;
		ArrayList<Row> rows = new ArrayList<>();

		String overviewStr = "";
		if (id_result.isEmpty()) {
			Extractor ext = new Extractor(wikiPageID);
			logger.log(Level.INFO, "abstract extraction query is being executed");
			ext.executeQuery(QueryTypes.ABSTRACT);
			ResultSetRewindable results = ext.getResults();

			results.reset();
			QuerySolution solution = results.nextSolution();
			RDFNode overview = solution.get("abstract");
			overviewStr = "No Abstract";
			if (overview != null)
				if (overview.isResource())
					overviewStr = (overview.asResource() + "").split("resource/")[1];
				else if (overview.isLiteral())
					overviewStr = (overview.asLiteral() + "").split("@")[0];

			final String personalInfoQuery = "SELECT filtered_info.*, WikiID.wikiPageId "
					+ "FROM (SELECT * FROM basic_info WHERE name = ?) AS filtered_info " + "LEFT JOIN WikiID "
					+ "ON WikiID.name = filtered_info.name " + "LIMIT 1";

			logger.log(Level.INFO, "personal info query is being executed");
			rows = conn.runQuery(personalInfoQuery, inp);

			Object[] toSerilaize = new Object[2];
			toSerilaize[0] = rows;
			toSerilaize[1] = overviewStr;

			String query_identifier = "getPersonalInfo(" + wikiPageID + ")";
			serialized_id = resultsSer.serializeQueryResults(conn, query_identifier, toSerilaize);
		} else {
			serialized_id = (int) id_result.get(0).row.get(0).getKey();
			Object[] output = (Object[]) resultsSer.deSerializeQueryResults(conn, serialized_id);
			rows = (ArrayList<Row>) output[0];
			overviewStr = (String) output[1];
		}
		Row res_row = rows.get(0);
		String name = (String) res_row.row.get(0).getValue().cast(res_row.row.get(0).getKey());
		String birthPlace = (String) res_row.row.get(1).getValue().cast(res_row.row.get(1).getKey());
		String deathPlace = (String) res_row.row.get(2).getValue().cast(res_row.row.get(2).getKey());

		Date birthDate = null;
		if (!"".equals(res_row.row.get(3).getKey())) {
			birthDate = (java.sql.Date) res_row.row.get(3).getValue().cast(res_row.row.get(3).getKey());
		}

		Date deathDate = null;
		if (!"".equals(res_row.row.get(4).getKey())) {
			deathDate = (java.sql.Date) res_row.row.get(4).getValue().cast(res_row.row.get(4).getKey());
		}

		String occupation = (String) res_row.row.get(5).getValue().cast(res_row.row.get(5).getKey());
		String spouseName = (String) res_row.row.get(6).getValue().cast(res_row.row.get(6).getKey());
		String spouseOccupation = (String) res_row.row.get(7).getValue().cast(res_row.row.get(7).getKey());
		String photoLink = (String) res_row.row.get(8).getValue().cast(res_row.row.get(8).getKey());
		photoLink.replaceAll("'", "\'");
		// String wikiPageID = (String)
		// res_row.row.get(9).getValue().cast(res_row.row.get(9).getKey());

		TableEntry te = new TableEntry("", name, birthPlace, deathPlace, birthDate, deathDate, occupation, spouseName,
				spouseOccupation, photoLink, overviewStr);
		return te;
	}

	public ArrayList<Row> runQuery(String s, Object[] inp) throws ClassNotFoundException, SQLException, IOException {
		if (inp == null)
			return conn.runQuery(s);
		return conn.runQuery(s, inp);
	}

	public Connector getConnector() {
		return conn;
	}
}
