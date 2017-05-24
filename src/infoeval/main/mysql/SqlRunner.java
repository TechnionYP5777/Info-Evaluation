package infoeval.main.mysql;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;



import infoeval.main.WikiData.Connector;

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
		//int yearVal = Integer.parseInt(year);
		final String yearPlaceQuery = "SELECT basic_info.name,BirthDate,wikiPageID FROM basic_info,WikiID "
				+ "WHERE YEAR(BirthDate) < ? AND BirthPlace = ? "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		Object[] inp = new Object[] { year, place };
		ArrayList<Row> rs = conn.runQuery(yearPlaceQuery, inp);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			Date birthDate = (java.sql.Date) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String wikiPageID = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name,place,"", birthDate,sqlDate ,"","",""));
		 }
		//TODO: delete
		//printing
		for (Row row : rs){
		    for (Entry<Object, Class> col: row.row){
		        System.out.print(" > " + ((col.getValue()).cast(col.getKey())));
		    }
		    System.out.println();
		}
		return res;
	}

	public ArrayList<TableEntry> getDifferentDeathPlace() throws SQLException, ClassNotFoundException, IOException, ParseException {
		logger.log(Level.INFO, "Born and died in different place is being executed");
		final String birthDeathPlaceQuery = "SELECT basic_info.name,BirthPlace,DeathPlace,wikiPageID "
				+ "FROM basic_info,WikiID WHERE DeathPlace IS NOT NULL AND BirthPlace != DeathPlace "
				+ "AND wikiPageID = (SELECT wikiPageID FROM WikiID WHERE WikiID.name = basic_info.name LIMIT 1)";
		ArrayList<Row> rs = conn.runQuery(birthDeathPlaceQuery);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			logger.log(Level.INFO, "One more added.");

			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			String birthPlace = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String deathPlace = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			String wikiPageID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry(wikiURL + wikiPageID, name, birthPlace, deathPlace,sqlDate ,sqlDate ,"","",""));
		 }
		logger.log(Level.INFO, "Finished");
		return res;
	}
	
	//TODO: I want to return their wiki pages ID's too. Got to design a solution for that 
	public ArrayList<TableEntry> getSameOccupationCouples() throws SQLException, ClassNotFoundException, IOException, ParseException {
		//TODO: fix query
		final String sameOccupationCouplesQuery = "SELECT B1.name AS Name,B2.name AS SpouseName,B1.occupation AS Occpation "
				+ "FROM basic_info B1, basic_info B2 "
				+ "WHERE B1.spouseName = B2.name AND B2.spouseName = B1.name "
				+ "AND B1.occupation = B2.occupation"; 
		ArrayList<Row> rs = conn.runQuery(sameOccupationCouplesQuery);
		ArrayList<TableEntry> res = new ArrayList<TableEntry>();
		for (Row row : rs){
			String name = (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()); 
			//String nameWikiID = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			String spouseName = (String) row.row.get(1).getValue().cast(row.row.get(1).getKey()); 
			//String spouseNameWikiID = (String) row.row.get(3).getValue().cast(row.row.get(3).getKey()); 		
			String occupation = (String) row.row.get(2).getValue().cast(row.row.get(2).getKey());
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			java.sql.Date sqlDate = new java.sql.Date(df.parse("1970-12-07").getTime());
			res.add(new TableEntry("", name, "", "",sqlDate ,sqlDate ,"","",occupation));
		}
		return res;
	}
	

}