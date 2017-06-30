package infoeval.test.WikiDataTest;

import org.junit.Test;
import infoeval.main.WikiData.Connector;
import infoeval.main.mysql.Row;
import static org.junit.Assert.assertNotNull;
import java.sql.Connection;
import java.util.Map.Entry;

/**
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 */
public class ConnectorTest {

	@Test
	public void connectionTest() throws Exception {
		Connection connection = new Connector().getConnection();
		assert connection != null;
		connection.close();
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void runQueryTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		Row row = conn.runQuery("SELECT photoLink FROM basic_info LIMIT 1").get(0);
		Entry<Object, Class> col = row.row.get(0);
		assertNotNull((String) col.getValue().cast(col.getKey()));
		int res = conn.runUpdate("INSERT INTO WikiID VALUES('osher','1234')");
		assert res == 1;
		res = conn.runUpdate("DELETE FROM WikiID WHERE name LIKE 'osher'");
		assert res == 1;
		conn.close();
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void runUpdateTest() throws Exception {
		Connector conn = new Connector();
		assert conn.getConnection() != null;
		Object[] inp = new Object[] { "Jessica" };
		Row row = conn.runQuery("SELECT photoLink FROM basic_info WHERE name LIKE CONCAT('%',?,'%') LIMIT 1", inp)
				.get(0);
		Entry<Object, Class> col = row.row.get(0);
		assertNotNull((String) col.getValue().cast(col.getKey()));
		inp = new Object[] { "osher", "1234" };
		int res = conn.runUpdate("INSERT INTO WikiID VALUES(?,?)", inp);
		assert res == 1;
		res = conn.runUpdate("DELETE FROM WikiID WHERE name LIKE ?", new Object[] { "osher" });
		assert res == 1;
		conn.close();
	}
}