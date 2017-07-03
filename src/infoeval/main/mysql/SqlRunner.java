package infoeval.main.mysql;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.text.ParseException;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import infoeval.main.WikiData.SqlTablesFiller;
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
	private static final int LIMIT_NUM = 40;

	public SqlRunner() throws Exception {
		conn = new Connector();
		resultsSer = new QueryResultsSerializer();
		conn.runUpdate("CREATE TABLE IF NOT EXISTS serialized_query_results "
				+ "(serialized_id int(30) NOT NULL AUTO_INCREMENT, " + "query_identifier VARCHAR(100) NOT NULL, "
				+ "serialized_result LONGBLOB, " + "PRIMARY KEY (serialized_id))");

	}

	public void clearSerializedQueries() throws Exception {
		conn.runUpdate("TRUNCATE TABLE serialized_query_results ");
	}

	public void dropSerializedTable() throws Exception {
		conn.runUpdate("DROP TABLE serialized_query_results");
	}

	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// e.printStackTrace();
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
			final String beforeYearInPlace = "SELECT filtered_info.name, filtered_info.BirthPlace, \n"
					+ "filtered_info.birthExpanded, filtered_info.BirthDate, filtered_info.deathPlace, \n"
					+ "filtered_info.deathExpanded, filtered_info.deathDate, filtered_info.occupation, \n"
					+ "filtered_info.photoLink, WikiID.wikiPageID "
					+ "FROM (SELECT * FROM basic_info WHERE BirthPlace LIKE CONCAT('%',?,'%') AND YEAR(BirthDate) < ?) AS filtered_info  "
					+ "LEFT JOIN WikiID " + "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;
			rows = conn.runQuery(beforeYearInPlace, inp);
			String query_identifier = "getBornInPlaceYear" + "(" + place + year + ")";
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
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			if (birthPlace.equals("No Birth Place"))
				birthPlace = "";
			String birthExpanded = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			if (birthExpanded.equals("No Birth Place"))
				birthExpanded = "";
			Date birthDate = null;
			if (!"".equals(row.row.get(3).getKey()))
				birthDate = (java.sql.Date) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			String deathPlace = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			if (deathPlace.equals("No Death Place"))
				deathPlace = "";
			String deathExpanded = (String) row.row.get(5).getValue().cast(row.row.get(5).getKey());
			if (deathExpanded.equals("No Death Place"))
				deathExpanded = "";
			Date deathDate = null;
			if (!"".equals(row.row.get(6).getKey()))
				deathDate = (java.sql.Date) row.row.get(6).getValue().cast(row.row.get(6).getKey());
			String occupation = (String) row.row.get(7).getValue().cast(row.row.get(7).getKey());
			if (occupation.equals("No Occupation"))
				occupation = "";
			String photoLink = (String) row.row.get(8).getValue().cast(row.row.get(8).getKey());
			if (photoLink.equals("No Photo"))
				photoLink = "";
			else
				photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(9).getValue().cast(row.row.get(9).getKey());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;

				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
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
			final String birthDeathPlace = "SELECT filtered_info.name," + " filtered_info.BirthPlace, \n"
					+ " filtered_info.birthExpanded," + " filtered_info.birthDate," + " filtered_info.DeathPlace, \n"
					+ " filtered_info.deathExpanded," + " filtered_info.deathDate," + " filtered_info.occupation, \n"
					+ " filtered_info.photoLink," + " WikiID.wikiPageID \n"
					+ "FROM (SELECT * FROM basic_info WHERE DeathPlace<>'No Death Place' \n"
					+ "AND BirthPlace<>DeathPlace) AS filtered_info " + "LEFT JOIN WikiID\n "
					+ "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;

			rows = conn.runQuery(birthDeathPlace);
			String query_identifier = "getDifferentDeathPlace()";
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
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			if (birthPlace.equals("No Birth Place"))
				birthPlace = "";
			String birthExpanded = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			if (birthExpanded.equals("No Birth Place"))
				birthExpanded = "";
			Date birthDate = null;
			if (!"".equals(row.row.get(3).getKey()))
				birthDate = (Date) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			String deathPlace = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			if (deathPlace.equals("No Death Place"))
				deathPlace = "";
			String deathExpanded = (String) row.row.get(5).getValue().cast(row.row.get(5).getKey());
			if (deathExpanded.equals("No Death Place"))
				deathExpanded = "";
			Date deathDate = null;
			if (!"".equals(row.row.get(6).getKey()))
				deathDate = (Date) row.row.get(6).getValue().cast(row.row.get(6).getKey());
			String occupation = (String) row.row.get(7).getValue().cast(row.row.get(7).getKey());
			if (occupation.equals("No Occupation"))
				occupation = "";
			String photoLink = (String) row.row.get(8).getValue().cast(row.row.get(8).getKey());
			if (photoLink.equals("No Photo"))
				photoLink = "";
			else
				photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(9).getValue().cast(row.row.get(9).getKey());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;

				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			}
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
			final String sameOccupationCouples = "SELECT name," + "spouseName," + "occupation," + "spouseOccupation \n"
					+ "FROM basic_info \n"
					+ "WHERE spouseName != 'No Spouse Name' AND spouseOccupation != 'No Spouse Occupation' \n"
					+ "AND occupation = spouseOccupation " + "LIMIT " + LIMIT_NUM;
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
			if (spouseName.equals("No Spouse"))
				spouseName = "";
			String occupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			if (occupation.equals("No Occupation"))
				occupation = "";
			String spouseOccupation = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			if (spouseOccupation.equals("No Spouse Occupation"))
				spouseOccupation = "";
			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;

				res.add(new TableEntry("", newName, "", "", null, null, occupation, spouseName, spouseOccupation, "",
						"", "", ""));
			} else {
				res.add(new TableEntry("", name, "", "", null, null, occupation, spouseName, spouseOccupation, "", "",
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
					+ "SELECT B1.name AS Name, basic_info.name AS SpouseName, B1.birthPlace AS BirthPlace \n"
					+ "FROM (SELECT * FROM basic_info " + "WHERE spouseName != 'No Spouse Name' \n"
					+ "AND birthPlace != 'No Birth Place') AS B1 " + "LEFT JOIN basic_info \n"
					+ "ON B1.spouseName = basic_info.name " + "AND basic_info.spouseName = B1.name \n"
					+ "AND B1.birthPlace = basic_info.birthPlace ";

			rows = conn.runQuery(sameBirthPlaceCouples);
			String query_identifier = "getSameBirthPlaceCouples()";
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
			String birthPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			
			if (name.contains(", ")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;

				res.add(new TableEntry("", newName, birthPlace, "", null, null, "", spouseName, "", "", "", "", ""));
			} else {
				res.add(new TableEntry("", name, birthPlace, "", null, null, "", spouseName, "", "", "", "", ""));
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
			final String occupationBetweenYears = "SELECT filtered_info.name, filtered_info.birthPlace, \n"
					+ " filtered_info.birthExpanded, filtered_info.birthDate, filtered_info.deathPlace, \n"
					+ " filtered_info.deathExpanded, filtered_info.deathDate, filtered_info.photoLink, \n"
					+ " WikiID.wikiPageID FROM (SELECT * FROM basic_info \n"
					+ "WHERE birthDate IS NOT NULL AND deathDate IS NOT NULL \n"
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? AND occupation = ?) AS filtered_info \n"
					+ "LEFT JOIN WikiID " + "ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;
			rows = conn.runQuery(occupationBetweenYears, inp);
			String query_identifier = "getOccupationBetweenYears(" + year1 + "," + year2 + "," + occupation + ")";
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
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			if (birthPlace.equals("No Birth Place"))
				birthPlace = "";
			String birthExpanded = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			if (birthExpanded.equals("No Birth Place"))
				birthExpanded = "";
			Date birthDate = null;
			if (!"".equals(row.row.get(3).getKey()))
				birthDate = (java.sql.Date) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			String deathPlace = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			if (deathPlace.equals("No Death Place"))
				deathPlace = "";
			String deathExpanded = (String) row.row.get(5).getValue().cast(row.row.get(5).getKey());
			if (deathExpanded.equals("No Death Place"))
				deathExpanded = "";
			Date deathDate = null;
			if (!"".equals(row.row.get(6).getKey()))
				deathDate = (java.sql.Date) row.row.get(6).getValue().cast(row.row.get(6).getKey());
			String photoLink = (String) row.row.get(7).getValue().cast(row.row.get(7).getKey());
			if (photoLink.equals("No Photo"))
				photoLink = "";
			else
				photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(8).getValue().cast(row.row.get(8).getKey());
			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;

				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
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
			final String spouselessBetweenYears = "SELECT filtered_info.name, filtered_info.birthPlace, \n"
					+ " filtered_info.birthExpanded, filtered_info.birthDate, filtered_info.deathPlace, \n"
					+ " filtered_info.deathExpanded, filtered_info.deathDate, filtered_info.occupation, \n"
					+ " filtered_info.photoLink, WikiID.wikiPageID \n"
					+ "FROM (SELECT * FROM basic_info WHERE birthDate IS NOT NULL AND deathDate IS NOT NULL \n"
					+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? AND spouseName = 'No Spouse Name' \n"
					+ ") AS filtered_info LEFT JOIN WikiID ON WikiID.name = filtered_info.name " + "LIMIT " + LIMIT_NUM;
			rows = conn.runQuery(spouselessBetweenYears, inp);
			String query_identifier = "getSpouselessBetweenYears(" + year1 + "," + year2 + ")";
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
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey());
			if (birthPlace.equals("No Birth Place"))
				birthPlace = "";
			String birthExpanded = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			if (birthExpanded.equals("No Birth Place"))
				birthExpanded = "";
			Date birthDate = null;
			if (!"".equals(row.row.get(3).getKey()))
				birthDate = (java.sql.Date) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			String deathPlace = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			if (deathPlace.equals("No Death Place"))
				deathPlace = "";
			String deathExpanded = (String) row.row.get(5).getValue().cast(row.row.get(5).getKey());
			if (deathExpanded.equals("No Death Place"))
				deathExpanded = "";
			Date deathDate = null;
			if (!"".equals(row.row.get(6).getKey()))
				deathDate = (java.sql.Date) row.row.get(6).getValue().cast(row.row.get(6).getKey());
			String occupation = (String) row.row.get(7).getValue().cast(row.row.get(7).getKey());
			if (occupation.equals("No Occupation"))
				occupation = "";
			String photoLink = (String) row.row.get(8).getValue().cast(row.row.get(8).getKey());
			if (photoLink.equals("No Photo"))
				photoLink = "";
			else
				photoLink.replaceAll("'", "\'");
			String wikiPageID = (String) row.row.get(9).getValue().cast(row.row.get(9).getKey());

			if (name.contains(",")) {
				String last = name.split(",")[0];
				String first = name.split(",")[1].substring(1);
				String newName = first + " " + last;
				res.add(new TableEntry(wikiURL + wikiPageID, newName, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			} else {
				res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace, birthDate, deathDate,
						occupation, "", "", photoLink, "", birthExpanded, deathExpanded));
			}
		}
		return res;
	}

	public TableEntry getPersonalInfo(int wikiPageID)
			throws ClassNotFoundException, SQLException, IOException, ParseException {
		Extractor ext = new Extractor(wikiPageID);
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

		ext.executeQuery(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID);
		ResultSetRewindable basicInfoByIdResults = ext.getResults();
		basicInfoByIdResults.reset();

		TableEntry te = SqlTablesFiller.getBasicInfo(basicInfoByIdResults);
		TableEntry result = new TableEntry(te);

		result.setUrl(wikiURL + wikiPageID);
		result.setOverview(overviewStr);

		String photoLink = result.getPhotoLink();
		photoLink.replaceAll("'", "\'");
		int containsQuesmark = photoLink.indexOf("?");
		if (containsQuesmark != -1) {
			photoLink = photoLink.substring(0, containsQuesmark);
		}
		result.setPhotoLink(photoLink);

		return result;
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
