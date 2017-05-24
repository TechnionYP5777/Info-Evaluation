package infoeval.main.mysql;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import infoeval.main.WikiData.Connector;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * @author osherh , Moshe
 * @Since 12-05-2017This class runs SQL queries on mysql server and returns a list of table entry as results
 * [[SuppressWarningsSpartan]]
 */

public class SqlRunner {
	private Connector conn;
	private String wikiURL = "https://en.wikipedia.org/?curid=";
	private static final Logger logger = Logger.getLogger("SqlRunner".getClass().getName());
	
	public SqlRunner() throws Exception {
		conn = new Connector();
	}
	
	public void close(){
		try{
			conn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
		
	//@RequestMapping("Queries/Query2_real")
	public ArrayList<TableEntry> getBornInPlaceBeforeYear(String place, String year)
			throws SQLException, ClassNotFoundException, IOException,ParseException {
		logger.log(Level.INFO, "Born and died in different place is being executed");
		final String beforeYearInPlace = "SELECT SQL_CACHE basic_info.name,BirthDate,wikiPageID FROM basic_info,WikiID "
				+ "WHERE YEAR(BirthDate) < ? AND BirthPlace = ? "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		Object[] inp = new Object[] { year, place };
		ArrayList<Row> rs = conn.runQuery(beforeYearInPlace, inp);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String wikiPageID = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name, place, "", birthDate, sqlDate,"","",""));
		 }
		return res;
	}

	public ArrayList<TableEntry> getDifferentDeathPlace() throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "Different birth and death place is being executed");
		final String birthDeathPlace = "SELECT SQL_CACHE basic_info.name,BirthPlace,DeathPlace,wikiPageID "
				+ "FROM basic_info,WikiID "
				+ "WHERE DeathPlace != 'No Death Place' "
				+ "AND BirthPlace != DeathPlace "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		ArrayList<Row> rs = conn.runQuery(birthDeathPlace);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		int i=1;
		for (Row row : rs){
			logger.log(Level.INFO, "record num " + i + " added");
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String deathPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String wikiPageID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace,sqlDate ,sqlDate ,"","",""));
			++i;
		}
		return res;
	}
	
	public ArrayList<TableEntry> getSameOccupationCouples() throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "Same occupation couples is being executed");
		final String sameOccupationCouples = "SELECT SQL_CACHE name,spouseName,occupation,spouseOccupation "
				+ "FROM basic_info "
				+ "WHERE spouseName != 'No Spouse Name' AND spouseOccupation != 'No Spouse Occupation' "
				+ "AND occupation = spouseOccupation"; 		
		ArrayList<Row> rs = conn.runQuery(sameOccupationCouples); 
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			String spouseName = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String occupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String spouseOoccupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry("", name, "", "",sqlDate ,sqlDate ,occupation, spouseName,spouseOoccupation));
		}
		return res;
	}
	
	public ArrayList<TableEntry> getSameBirthPlaceCouples() throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "same birth place couples is being executed");
		final String sameBirthPlaceCouples = "SELECT SQL_CACHE B1.name AS Name,B2.name AS SpouseName,B1.birthPlace AS BirthPlace "
				+ "FROM basic_info B1, basic_info B2 "
				+ "WHERE B1.spouseName = B2.name AND B2.spouseName = B1.name "
				+ "AND B1.birthPlace = B2.birthPlace"; 
		ArrayList<Row> rs = conn.runQuery(sameBirthPlaceCouples); 
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			String spouseName = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String birthPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry("", name, birthPlace, "", sqlDate, sqlDate, "", spouseName, ""));
		}
		return res;
	}

	public ArrayList<TableEntry> getOccupationBetweenYears(String year1, String year2, String occupation) throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "occupation between years is being executed");
		final String occupationBetweenYears = "SELECT SQL_CACHE name,birthDate,deathDate,wikiPageId "
				+ "FROM basic_info, WikiID "
				+ "WHERE deathDate IS NOT NULL "
				+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)"; 
		Object[] inp = new Object[] { year1, year2, occupation };
		ArrayList<Row> rs = conn.runQuery(occupationBetweenYears,inp); 
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			Date deathDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey()); 
			String wikiPageID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", ""));
		}
		return res;
	}

	public ArrayList<TableEntry> getSpouselessBetweenYears(String year1, String year2) throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "spouseless between years is being executed");
		final String spouselessBetweenYears = "SELECT SQL_CACHE name,birthDate,deathDate,occupation,wikiPageId "
				+ "FROM basic_info, WikiID "
				+ "WHERE deathDate IS NOT NULL "
				+ "AND YEAR(birthDate) >= ? AND YEAR(deathDate) <= ? "
				+ "AND spouseName = 'No Spouse Name' "
				+ "AND occupation = ? "				
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)"; 
		Object[] inp = new Object[] { year1, year2 };
		ArrayList<Row> rs = conn.runQuery(spouselessBetweenYears,inp); 
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			Date deathDate = (java.sql.Date) row.row.get(2).getValue().cast(row.row.get(2).getKey()); 
			String occupation = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey()); 			
			String wikiPageID = (String) row.row.get(4).getValue().cast(row.row.get(4).getKey());
			res.add(new TableEntry(wikiURL + wikiPageID, name, "", "", birthDate, deathDate, occupation, "", ""));
		}
		return res;
	}	
	
	public ArrayList<Row> runQuery(String s,Object[] inp) throws ClassNotFoundException, SQLException, IOException{
		return conn.runQuery(s,inp);
	}
}
