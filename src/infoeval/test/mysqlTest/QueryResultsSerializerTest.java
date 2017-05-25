package infoeval.test.mysqlTest;

import infoeval.main.mysql.QueryResultsSerializer;
import infoeval.main.mysql.Row;
import infoeval.main.mysql.SqlRunner;
import infoeval.main.WikiData.Connector;

import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * 
 * @author osherh
 * @Since 12-05-2017
 *
 */
public class QueryResultsSerializerTest {
	private QueryResultsSerializer resultsSer;
	private Connector connector;
	private SqlRunner runner;
	
	public QueryResultsSerializerTest() throws Exception {
		resultsSer = new QueryResultsSerializer();
		connector = new Connector();
		runner = new SqlRunner();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void serializeANDdeSerializeTest() throws Exception {
		runner.close();
		
		long serialized_id = -1;
		String query_identifier = "testQuery";
		ArrayList<Row> rows = new ArrayList<Row>();
		Row row1 = new Row();
		row1.add(1, "integer");
		Row row2 = new Row();
		row2.add(2, "integer");
		Row row3 = new Row();
		row3.add(3, "integer");
		rows.add(row1);
		rows.add(row2);
		rows.add(row3);
		Connection connection = connector.getConnection();
		serialized_id = resultsSer.serializeQueryResults(connection, query_identifier, rows);
		connection.close();
		connection = connector.getConnection();
		ArrayList<Row> result = (ArrayList<Row>) resultsSer.deSerializeQueryResults(connection, serialized_id);
		connection.close();
		int i = 1;
		for (Row r : result) {
			int val = (int) r.row.get(0).getValue().cast(r.row.get(0).getKey());
			assertEquals(val, i);
			++i;
		}
		connector.close();
	}
}
