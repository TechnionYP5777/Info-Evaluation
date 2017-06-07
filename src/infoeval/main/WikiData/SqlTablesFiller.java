package infoeval.main.WikiData;

import infoeval.main.WikiData.Connector;
import infoeval.main.mysql.TableEntry;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.RDFNode;

/**
 * 
 * @author osherh
 * @author netanel
 * @author Moshe
 * @since 17-05-2017
 *
 */
public class SqlTablesFiller {
	private static final Logger logger = Logger.getLogger("SqlTablesFiller".getClass().getName());
	private Connector connector;
	private String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	public SqlTablesFiller() throws IOException, ClassNotFoundException, SQLException {
		connector = new Connector();
	}

	public void close() throws IOException, ClassNotFoundException, SQLException {
		connector.close();
	}

	public void createTables() throws SQLException, ClassNotFoundException, IOException {
		dropTables();
		connector.runUpdate("CREATE TABLE IF NOT EXISTS basic_info(Name VARCHAR(100) NOT NULL,"
				+ " BirthPlace VARCHAR(100) NULL,DeathPlace VARCHAR(100) NULL,BirthDate DATE NULL,"
				+ " DeathDate DATE NULL, occupation VARCHAR(100) NULL, spouseName VARCHAR(100) NULL,"
				+ " spouseOccupation VARCHAR(100) NULL, photoLink VARCHAR(500) NULL)");
		logger.log(Level.INFO, "basic_info table created successfully");
		connector.runUpdate(
				"CREATE TABLE IF NOT EXISTS WikiID(Name VARCHAR(100) NOT NULL,wikiPageID VARCHAR(50) NOT NULL)");
		logger.log(Level.INFO, "WIKI_ID table created successfully");
	}

	public void addIndexBasicInfo() throws SQLException, ClassNotFoundException, IOException {
		connector.runUpdate("ALTER TABLE basic_info ADD INDEX basicInfoIndex (Name,BirthDate,DeathDate,photoLink);\n");
	}

	public void addIndexWikiID() throws SQLException, ClassNotFoundException, IOException {
		connector.runUpdate("ALTER TABLE WikiID ADD INDEX wikiIdIndex (wikiPageID)");
	}

	public void fillWikiIdTable() throws SQLException, ClassNotFoundException, IOException {
		connector.clearWikiIdTable();
		Extractor ext = new Extractor();
		ext.executeQuery(QueryTypes.WIKI_ID);
		ResultSetRewindable results = ext.getResults();
		results.reset();
		for (int i = 0; i < results.size(); ++i) {
			QuerySolution solution = results.nextSolution();
			Object[] inp = new Object[2];
			inp[0] = solution.getLiteral("name").getString();
			inp[1] = solution.getLiteral("wikiPageID").getString();
			connector.runUpdate("INSERT INTO WikiID VALUES(?,?)", inp);
		}
	}

	public void fillBasicInfoTable() throws SQLException, ParseException, ClassNotFoundException, IOException {

		connector.clearBasicInfoTable();
		Extractor ext = new Extractor();
		ext.executeQuery(QueryTypes.BASIC_INFO);
		ResultSetRewindable results = ext.getResults();
		results.reset();
		for (int ¢ = 0; ¢ < results.size(); ++¢) 
			fillBasicInfoTable(results);
	}

	public void fillBasicInfoTable(ResultSetRewindable r)
			throws ClassNotFoundException, SQLException, IOException, ParseException {

		QuerySolution solution = r.nextSolution();
		String name = solution.getLiteral("name").getString();

		RDFNode sName = solution.get("sname");
		String spouseName = sName == null || !sName.isLiteral() ? "No Spouse" : sName.asLiteral().getString() + "";
		RDFNode bPlace = solution.get("birth");
		String birthPlace = "No Birth Place";
		if (bPlace != null)
			if (bPlace.isResource())
				birthPlace = (bPlace.asResource() + "").split("resource/")[1];
			else if (bPlace.isLiteral())
				birthPlace = (bPlace.asLiteral() + "").split("@")[0];
		RDFNode dPlace = solution.get("death");
		String deathPlace = "No Death Place";
		if (dPlace != null)
			if (dPlace.isResource())
				deathPlace = (dPlace.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				deathPlace = (dPlace.asLiteral() + "").split("@")[0];
		RDFNode occupation = solution.get("occup");
		String occup = "No Occupation";
		if (occupation != null)
			if (occupation.isResource())
				occup = !(occupation.asResource() + "").contains("resource") ? "No Occupation"
						: (occupation.asResource() + "").split("resource/")[1];
			else if (occupation.isLiteral())
				occup = (occupation.asLiteral() + "").split("@")[0];

		RDFNode spOcuup = solution.get("spOccu");
		String spouseOccupation = "No Spouse Occupation";
		if (spOcuup != null)
			if (spOcuup.isResource())
				spouseOccupation = !(spOcuup.asResource() + "").contains("resource") ? "No Spouse Occupation"
						: (spOcuup.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				spouseOccupation = (spOcuup.asLiteral() + "").split("@")[0];

		RDFNode bDate = solution.get("bDate");
		String birthDate = !bDate.isLiteral() ? null : bDate.asLiteral().getValue() + "";

		RDFNode dDate = solution.get("dDate");
		String deathDate = null;
		java.sql.Date sqlDeathDate = null;
		if (dDate == null)
			sqlDeathDate = null;
		else {
			deathDate = !dDate.isLiteral() ? null : dDate.asLiteral().getValue() + "";
			if (deathDate.contains(".") || deathDate.contains("c."))
				sqlDeathDate = null;
			else if (deathDate.split("-").length == 1 && deathDate.matches("[0-9]+") && deathDate.length() <= 4)
				sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy"));
			else if (deathDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
				sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy-MM-dd"));
			else if (deathDate.matches("--[0-9][0-9][-][0-9][0-9]"))
				sqlDeathDate = stringToSqlDate(deathDate.substring(2, deathDate.length()),
						new SimpleDateFormat("MM-dd"));
			int monthNum = 1;
			for (String month : months) {
				if (deathDate.equals(month)) {
					sqlDeathDate = stringToSqlDate(monthNum + "", new SimpleDateFormat("MM"));
					break;
				}
				if (deathDate.startsWith(month)) {
					String parseDeathDate = deathDate.split(" ")[1] + "-" + monthNum;
					sqlDeathDate = stringToSqlDate(parseDeathDate, new SimpleDateFormat("yyyy-MM"));
					break;
				}
				++monthNum;
			}

		}

		String photoLink = solution.get("photo") == null ? "No Photo" : solution.get("photo") + "";
		java.sql.Date sqlBirthDate = null;
		if (birthDate.contains(".") || birthDate.contains("c."))
			sqlBirthDate = null;
		else if (birthDate.split("-").length == 1 && birthDate.matches("[0-9]+") && birthDate.length() <= 4)
			sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy"));
		else if (birthDate.matches("--[0-9][0-9][-][0-9][0-9]"))
			sqlDeathDate = stringToSqlDate(birthDate.substring(2, birthDate.length()), new SimpleDateFormat("MM-dd"));
		else if (birthDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
			sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy-MM-dd"));
		int monthNum = 1;
		for (String month : months) {
			if (birthDate.equals(month)) {
				sqlBirthDate = stringToSqlDate(monthNum + "", new SimpleDateFormat("MM"));
				break;
			}
			if (birthDate.startsWith(month)) {
				String parseBirthDate = birthDate.split(" ")[1] + "-" + monthNum;
				sqlBirthDate = stringToSqlDate(parseBirthDate, new SimpleDateFormat("yyyy-MM"));
				break;
			}
			++monthNum;
		}

		Object[] inp = new Object[9];
		inp[0] = name;
		inp[1] = birthPlace;
		inp[2] = deathPlace;
		inp[3] = sqlBirthDate;
		inp[4] = sqlDeathDate;
		inp[5] = occup;
		inp[6] = spouseName;
		inp[7] = spouseOccupation;
		inp[8] = photoLink;
		// System.out.println(photoLink);

		connector.runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?,?,?,?,?)", inp);
	}

	public TableEntry getInfo(ResultSetRewindable r)
			throws ClassNotFoundException, SQLException, IOException, ParseException {

		QuerySolution solution = r.nextSolution();

		RDFNode sName = solution.get("sname");
		String spouseName = sName == null || !sName.isLiteral() ? "No Spouse" : sName.asLiteral().getString() + "";

		RDFNode bPlace = solution.get("birth");
		String birthPlace = "No Birth Place";
		if (bPlace != null)
			if (bPlace.isResource())
				birthPlace = (bPlace.asResource() + "").split("resource/")[1];
			else if (bPlace.isLiteral())
				birthPlace = (bPlace.asLiteral() + "").split("@")[0];

		RDFNode dPlace = solution.get("death");
		String deathPlace = "No Death Place";
		if (dPlace != null)
			if (dPlace.isResource())
				deathPlace = (dPlace.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				deathPlace = (dPlace.asLiteral() + "").split("@")[0];

		RDFNode occupation = solution.get("occup");
		String occup = "No Occupation";
		if (occupation != null)
			if (occupation.isResource())
				occup = !(occupation.asResource() + "").contains("resource") ? "No Occupation"
						: (occupation.asResource() + "").split("resource/")[1];
			else if (occupation.isLiteral())
				occup = (occupation.asLiteral() + "").split("@")[0];

		RDFNode spOcuup = solution.get("spOccu");
		String spouseOccupation = "No Spouse Occupation";
		if (spOcuup != null)
			if (spOcuup.isResource())
				spouseOccupation = !(spOcuup.asResource() + "").contains("resource") ? "No Spouse Occupation"
						: (spOcuup.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				spouseOccupation = (spOcuup.asLiteral() + "").split("@")[0];

		RDFNode bDate = solution.get("bDate");
		String birthDate = bDate == null ? "" : !bDate.isLiteral() ? null : bDate.asLiteral().getValue() + "";
		RDFNode dDate = solution.get("dDate");
		String deathDate = null;
		java.sql.Date sqlDeathDate = null;
		if (dDate == null)
			sqlDeathDate = null;
		else {
			deathDate = !dDate.isLiteral() ? null : dDate.asLiteral().getValue() + "";
			if (deathDate.contains(".") || deathDate.contains("c."))
				sqlDeathDate = null;
			else if (deathDate.split("-").length == 1 && deathDate.matches("[0-9]+") && deathDate.length() <= 4)
				sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy"));
			else if (deathDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
				sqlDeathDate = stringToSqlDate(deathDate, new SimpleDateFormat("yyyy-MM-dd"));
			else if (deathDate.matches("--[0-9][0-9][-][0-9][0-9]"))
				sqlDeathDate = stringToSqlDate(deathDate.substring(2, deathDate.length()),
						new SimpleDateFormat("MM-dd"));
			int monthNum = 1;
			for (String month : months) {
				if (deathDate.equals(month)) {
					sqlDeathDate = stringToSqlDate(monthNum + "", new SimpleDateFormat("MM"));
					break;
				}
				if (deathDate.startsWith(month)) {
					String parseDeathDate = deathDate.split(" ")[1] + "-" + monthNum;
					sqlDeathDate = stringToSqlDate(parseDeathDate, new SimpleDateFormat("yyyy-MM"));
					break;
				}
				++monthNum;
			}

		}

		java.sql.Date sqlBirthDate = null;
		if (birthDate.contains(".") || birthDate.contains("c."))
			sqlBirthDate = null;
		else if (birthDate.split("-").length == 1 && birthDate.matches("[0-9]+") && birthDate.length() <= 4)
			sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy"));
		else if (birthDate.matches("--[0-9][0-9][-][0-9][0-9]"))
			sqlDeathDate = stringToSqlDate(birthDate.substring(2, birthDate.length()), new SimpleDateFormat("MM-dd"));
		else if (birthDate.matches("[0-9][0-9][0-9][0-9][-][0-9][0-9][-][0-9][0-9]"))
			sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy-MM-dd"));
		int monthNum = 1;
		for (String month : months) {
			if (birthDate.equals(month)) {
				sqlBirthDate = stringToSqlDate(monthNum + "", new SimpleDateFormat("MM"));
				break;
			}
			if (birthDate.startsWith(month)) {
				String parseBirthDate = birthDate.split(" ")[1] + "-" + monthNum;
				sqlBirthDate = stringToSqlDate(parseBirthDate, new SimpleDateFormat("yyyy-MM"));
				break;
			}
			++monthNum;
		}

		return new TableEntry("", "", birthPlace, deathPlace, sqlBirthDate, sqlDeathDate, occup, spouseName,
				spouseOccupation, (solution.get("photo") == null ? "No Photo" : solution.get("photo") + ""), "");
	}

	private java.sql.Date stringToSqlDate(String stringDate, SimpleDateFormat f) throws ParseException {
		return new java.sql.Date(f.parse(stringDate).getTime());
	}

	public void dropTables() throws SQLException, ClassNotFoundException, IOException {
		connector.runUpdate("DROP TABLE basic_info");
		connector.runUpdate("DROP TABLE WikiID");
	}

	public void dropIndex() throws SQLException, ClassNotFoundException, IOException {
		connector.runUpdate("DROP INDEX basicInfoIndex ON basic_info");
		connector.runUpdate("DROP INDEX wikiIdIndex ON wikiID");
	}
}
