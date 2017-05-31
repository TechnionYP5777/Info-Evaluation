package infoeval.test.mysqlTest;

import infoeval.main.mysql.QueryResultsSerializer;
import infoeval.main.mysql.Row;
import infoeval.main.mysql.SqlRunner;
import infoeval.main.WikiData.Connector;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * 
 * @author osherh
 * @Since 25-05-2017
 *
 */

/*
 * ATTENTION ! When you want to test this class , remove the @Ignore attributes.
 * I added it since the connector tries to read from the config.xml file which won't be uploaded to GitHub and it causes
 * travisCI to fail. 
 * like moshiko did in the connectorTest it's relevant here too
 * @osherh 
 */
public class QueryResultsSerializerTest {
	private QueryResultsSerializer resultsSer;
	private Connector connector;
	private SqlRunner runner;
	
	public QueryResultsSerializerTest() throws Exception {
		resultsSer = new QueryResultsSerializer();
		runner = new SqlRunner();
		connector = runner.getConnector();
	}

	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void serializeANDdeSerializeTest() throws Exception {

		
		int serialized_id = -1;
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
		serialized_id = resultsSer.serializeQueryResults(connector, query_identifier, rows);
		ArrayList<Row> result = (ArrayList<Row>) resultsSer.deSerializeQueryResults(connector, serialized_id);
		int i = 1;
		for (Row r : result) {
			int val = (int) r.row.get(0).getValue().cast(r.row.get(0).getKey());
			assertEquals(val, i);
			++i;
		}
		connector.close();
	}
}
