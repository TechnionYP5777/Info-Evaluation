package infoeval.test.WikiDataTest;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.WikiData.Extractor;
import infoeval.main.WikiData.QueryTypes;
import infoeval.main.mysql.Row;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 */
public class ConnectorTest {
	private static final int ENTRIES_NUM = 10000;

	/*
	 * ATTENTION ! When you want to test this class , remove the @ignore
	 * attributes. I added it becasue connector tries to read from the
	 * config.xml file which won't be uploaded to GitHub and it causes travisCI
	 * to fail.
	 * 
	 * @Moshiko
	 */
	@Ignore
	@Test
	public void connectionTest() throws Exception {
		Connector conn = new Connector();
		Connection connection = conn.getConnection();
		assert connection != null;
		connection.close();
	}

	@Ignore
	@Test
	public void runQueryTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;

		ArrayList<Row> rows = conn.runQuery("SELECT photoLink FROM basic_info LIMIT 1");
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		String photo = (String) col.getValue().cast(col.getKey());

		int res = conn.runUpdate("INSERT INTO WikiID VALUES('osher','1234')");
		assert res == 1;
		res = conn.runUpdate("DELETE FROM WikiID WHERE name LIKE 'osher'");
		assert res == 1;

		conn.close();
	}

	@Ignore
	@Test
	public void runUpdateTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;

		Object[] inp = new Object[] { "Jessica" };
		ArrayList<Row> rows = conn
				.runQuery("SELECT photoLink FROM basic_info WHERE name LIKE CONCAT('%',?,'%') LIMIT 1", inp);
		Row row = rows.get(0);
		Entry<Object, Class> col = row.row.get(0);
		String photo = (String) col.getValue().cast(col.getKey());

		inp = new Object[] { "osher", "1234" };
		int res = conn.runUpdate("INSERT INTO WikiID VALUES(?,?)", inp);
		assert res == 1;

		Object[] name = new Object[] { "osher" };
		res = conn.runUpdate("DELETE FROM WikiID WHERE name LIKE ?", name);
		assert res == 1;

		conn.close();
	}
}