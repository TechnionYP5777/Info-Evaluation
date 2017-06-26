package infoeval.main.WikiData;

import infoeval.main.WikiData.Connector;
import infoeval.main.mysql.TableEntry;

import java.io.IOException;
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
	private static String[] months = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };

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
				+ " spouseOccupation VARCHAR(100) NULL, photoLink VARCHAR(500) NULL, birthExpanded VARCHAR(100) NULL,"
				+ "deathExpanded VARCHAR(100) NULL)");
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
			System.out.println("Filling WikiID entry number " + i);
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
		for (int ¢ = 0; ¢ < results.size(); ++¢) {

			try {
				TableEntry te = getBasicInfo(results);

				Object[] inp = new Object[11];
				inp[0] = te.getName();
				inp[1] = te.getBirthPlace();
				inp[2] = te.getDeathPlace();
				inp[3] = te.getBirthDate();
				inp[4] = te.getDeathDate();
				inp[5] = te.getOccupation();
				inp[6] = te.getSpouseName();
				inp[7] = te.getSpouseOccupation();
				inp[8] = te.getPhotoLink();
				inp[9] = te.getBirthExpandedPlace();
				inp[10] = te.getDeathExpandedPlace();
				connector.runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?,?,?,?,?,?,?)", inp);
			} catch (Exception e) {
				System.out.println("Failed filling basicInfo entry number " + ¢);
			}
		}
	}

	public static TableEntry getBasicInfo(ResultSetRewindable r)
			throws ClassNotFoundException, SQLException, IOException, ParseException {

		QuerySolution solution = r.nextSolution();
		RDFNode spName = solution.get("sname");
		RDFNode name = solution.get("pname");
		String personalName = name == null || !name.isLiteral() ? "No Name" : name.asLiteral().getString() + "",
				spouseName = spName == null ? "No Spouse" : spName.asLiteral().getString() + "";
		// String personalName=solution.getLiteral("pname").getString();
		// String spouseName=solution.getLiteral("sname").getString();
		RDFNode bPlace = solution.get("birth");
		String birthPlace = "No Birth Place";
		if (bPlace != null)
			if (bPlace.isResource())
				birthPlace = (bPlace.asResource() + "").split("resource/")[1];
			else if (bPlace.isLiteral())
				birthPlace = (bPlace.asLiteral() + "").split("@")[0];
		birthPlace = birthPlace.replaceAll("_", " ");

		RDFNode bExp = solution.get("bExp");
		String birthExpanded = "No Birth Place";
		if (bExp != null)
			if (bExp.isResource())
				birthExpanded = (bExp.asResource() + "").split("resource/")[1];
			else if (bExp.isLiteral())
				birthExpanded = (bExp.asLiteral() + "").split("@")[0];
		birthExpanded = birthExpanded.replaceAll("_", " ");

		RDFNode dExp = solution.get("dExp");
		String deathExpanded = "No Death Place";
		if (dExp != null)
			if (dExp.isResource())
				deathExpanded = (dExp.asResource() + "").split("resource/")[1];
			else if (dExp.isLiteral())
				deathExpanded = (dExp.asLiteral() + "").split("@")[0];
		deathExpanded = deathExpanded.replaceAll("_", " ");

		RDFNode dPlace = solution.get("death");
		String deathPlace = "No Death Place";
		if (dPlace != null)
			if (dPlace.isResource())
				deathPlace = (dPlace.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				deathPlace = (dPlace.asLiteral() + "").split("@")[0];
		deathPlace = deathPlace.replaceAll("_", " ");

		RDFNode occupation = solution.get("occup");
		String occup = "No Occupation";
		if (occupation != null)
			if (occupation.isResource())
				occup = !(occupation.asResource() + "").contains("resource") ? "No Occupation"
						: (occupation.asResource() + "").split("resource/")[1];
			else if (occupation.isLiteral())
				occup = (occupation.asLiteral() + "").split("@")[0];
		occup = occup.replaceAll("_", " ");
		if (occup.contains(personalName)) {
			RDFNode occupationTitle = solution.get("occupTitle");
			occup = "No Occupation";
			if (occupationTitle.isLiteral()) {
				occup = (occupationTitle.asLiteral() + "").split("@")[0];
				occup = occup.replaceAll("_", " ");
			}
		}

		RDFNode spOcuup = solution.get("spOccu");
		String spouseOccupation = "No Spouse Occupation";
		if (spOcuup != null)
			if (spOcuup.isResource())
				spouseOccupation = !(spOcuup.asResource() + "").contains("resource") ? "No Spouse Occupation"
						: (spOcuup.asResource() + "").split("resource/")[1];
			else if (dPlace.isLiteral())
				spouseOccupation = (spOcuup.asLiteral() + "").split("@")[0];
		spouseOccupation = spouseOccupation.replaceAll("_", " ");

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
					try {
						sqlDeathDate = stringToSqlDate(parseDeathDate, new SimpleDateFormat("yyyy-MM"));
					} catch (Exception e) {
						throw e;
					}
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

		return new TableEntry("", personalName, birthPlace, deathPlace, sqlBirthDate, sqlDeathDate, occup, spouseName,
				spouseOccupation, (solution.get("photo") == null ? "No Photo" : solution.get("photo") + ""), "",
				birthExpanded, deathExpanded);

	}

	private static java.sql.Date stringToSqlDate(String stringDate, SimpleDateFormat f) throws ParseException {
		try {
			return new java.sql.Date(f.parse(stringDate).getTime());
		} catch (Exception e) {
			throw e;
		}
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
