package infoeval.main.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import infoeval.main.WikiData.Connector;

/**
 * 
 * @author osherh
 * @Since 12-05-2017
 * 
 * This class runs SQL queries on mysql server and returns a list of table entry as results
 *
 */
public class SqlQueriesRunner {
	private String query1;
	private String query2; 
	private Connector conn;

	public SqlQueriesRunner() throws Exception{
		query1 = "SELECT * "
				+ "FROM basic_info "
				+ "WHERE YEAR(BirthDate) < ? "
				+ "AND BirthPlace = ? ";
		query2 = "SELECT * "
				+ "FROM basic_info "
				+ "WHERE BirthPlace != DeathPlace ";
		conn = new Connector();
	}
	//params has year and then place
	public List<TableEntry> runQuery(int queryNum, Object[] params) throws SQLException{
		if(1==queryNum){
			ResultSet rs = conn.runQuery(query1,params);
			ArrayList<TableEntry> res = new ArrayList<TableEntry>();
			while(rs.next()){
				TableEntry te = new TableEntry("",rs.getString(1),rs.getString(2),rs.getString(3),rs.getDate(4),rs.getDate(5));
				res.add(te);
			}
			return res;
		}	
		if(2==queryNum){
			ResultSet rs = conn.runQuery(query2);
			ArrayList<TableEntry> res = new ArrayList<TableEntry>();
			while(rs.next()){
				TableEntry te = new TableEntry("",rs.getString(1),rs.getString(2),rs.getString(3),rs.getDate(4),rs.getDate(5));
				res.add(te);
			}
			return res;
		}
		return null;
	}
}
