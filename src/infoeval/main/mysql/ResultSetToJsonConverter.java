package infoeval.main.mysql;

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
		JSONArray $ = new JSONArray();
		for (ResultSetMetaData rsmd = s.getMetaData(); s.next();) {
			int numColumns = rsmd.getColumnCount();
			JSONObject obj = new JSONObject();
			for (int ¢ = 1; ¢ <= numColumns; ++¢) {
				rsmd.getColumnName(¢);
				if (rsmd.getColumnType(¢) == java.sql.Types.VARCHAR)
					$.put(obj);
				if (rsmd.getColumnType(¢) == java.sql.Types.DATE)
					$.put(obj);
			}
		}
		return $;
	}
}