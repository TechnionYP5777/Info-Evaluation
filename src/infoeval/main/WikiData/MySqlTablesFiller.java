package infoeval.main.WikiData;

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
 * @since 17-05-2017
 *
 */
public class MySqlTablesFiller {
	private static final Logger logger = Logger.getLogger("MySqlTablesFiller".getClass().getName());
	private Connector connector;
	private String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
			"October", "November", "December" };

	public MySqlTablesFiller() throws Exception {
		connector = new Connector();
	}

	public void createTables() throws SQLException, ClassNotFoundException, IOException {
		connector.runUpdate("CREATE TABLE IF NOT EXISTS basic_info(Name VARCHAR(100) NOT NULL,"
				+ " BirthPlace VARCHAR(100) NULL,DeathPlace VARCHAR(100) NULL,BirthDate DATE NULL,"
				+ " DeathDate DATE NULL, occupation VARCHAR(100) NULL, spouseName VARCHAR(100) NULL,"
				+ " spouseOccupation VARCHAR(100) NULL ");
		logger.log(Level.INFO, "basic_info table created successfully");
		connector.runUpdate(
				"CREATE TABLE IF NOT EXISTS WikiID(Name VARCHAR(100) NOT NULL,wikiPageID VARCHAR(50) NOT NULL)");
		logger.log(Level.INFO, "WIKI_ID table created successfully");
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
		for (int i = 0; i < results.size(); ++i) {
			QuerySolution solution = results.nextSolution();

			String name = solution.getLiteral("name").getString(),
					spouseName = solution.getLiteral("sname").getString();

			RDFNode bPlace = solution.get("birth");
			String birthPlace = null;
			if (bPlace != null)
				if (bPlace.isResource())
					birthPlace = (bPlace.asResource() + "").split("resource/")[1];
				else if (bPlace.isLiteral())
					birthPlace = (bPlace.asLiteral() + "").split("@")[0];

			RDFNode dPlace = solution.get("death");
			String deathPlace = null;
			if (dPlace != null)
				if (dPlace.isResource())
					deathPlace = (dPlace.asResource() + "").split("resource/")[1];
				else if (dPlace.isLiteral())
					deathPlace = (dPlace.asLiteral() + "").split("@")[0];

			RDFNode occupation = solution.get("occup");
			String occup = null;
			if (occupation != null)
				if (occupation.isResource())
					occup = (occupation.asResource() + "").split("resource/")[1];
				else if (dPlace.isLiteral())
					occup = (occupation.asLiteral() + "").split("@")[0];

			RDFNode spOcuup = solution.get("spOccu");
			String spouseOccupation = null;
			if (spOcuup != null)
				if (spOcuup.isResource())
					spouseOccupation = (spOcuup.asResource() + "").split("resource/")[1];
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
				if (deathDate.contains("."))
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
			if (birthDate.contains("."))
				sqlBirthDate = null;
			else if (birthDate.split("-").length == 1 && birthDate.matches("[0-9]+") && birthDate.length() <= 4)
				sqlBirthDate = stringToSqlDate(birthDate, new SimpleDateFormat("yyyy"));
			else if (birthDate.matches("--[0-9][0-9][-][0-9][0-9]"))
				sqlDeathDate = stringToSqlDate(birthDate.substring(2, birthDate.length()),
						new SimpleDateFormat("MM-dd"));
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

			Object[] inp = new Object[8];
			inp[0] = name;
			inp[1] = birthPlace;
			inp[2] = deathPlace;
			inp[3] = sqlBirthDate;
			inp[4] = sqlDeathDate;
			inp[5] = occup;
			inp[6] = spouseName;
			inp[7] = spouseOccupation;
			connector.runUpdate("INSERT INTO basic_info VALUES(?,?,?,?,?,?,?,?)", inp);
		}
	}

	private java.sql.Date stringToSqlDate(String stringDate, SimpleDateFormat f) throws ParseException {
		return new java.sql.Date(f.parse(stringDate).getTime());
	}
}
