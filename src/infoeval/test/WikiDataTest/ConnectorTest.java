package infoeval.test.WikiDataTest;
import static org.junit.Assert.*;



import org.junit.Test;

import infoeval.main.WikiData.Connector;

public class ConnectorTest{
	@Test public void test1() throws Exception{
		Connector conn=new Connector();
		assertNotNull(conn.getConnection());
	}
}