package main.mysql;

/** 
 * @author osherh
 * @since  24-04-2017
 * 
 */
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class ResultSetToJsonConverter {
	public static JSONArray convert(ResultSet s) throws SQLException, JSONException {
		JSONArray json = new JSONArray();
		for (ResultSetMetaData rsmd = s.getMetaData(); s.next();) {
			int numColumns = rsmd.getColumnCount();
			JSONObject obj = new JSONObject();
			for (int i = 1; i <= numColumns; ++i) {
				rsmd.getColumnName(i);
				if (rsmd.getColumnType(i) == java.sql.Types.VARCHAR)
					json.put(obj);
				if (rsmd.getColumnType(i) == java.sql.Types.DATE)
					json.put(obj);
			}
		}
		return json;
	}
}