
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

public class ResultSetConverter {
  public static JSONArray convert( ResultSet s )
    throws SQLException, JSONException{
    JSONArray json = new JSONArray();
    ResultSetMetaData rsmd = s.getMetaData();

    while(s.next()) {
      int numColumns = rsmd.getColumnCount();
      JSONObject obj = new JSONObject();

      for (int i=1; i<numColumns+1; i++) {
        String column_name = rsmd.getColumnName(i);

      if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
         obj.json.put(obj);
      }
      if(rsmd.getColumnType(i)==java.sql.Types.Date){
          obj.json.put(obj);
     }
      
    return json;
  }