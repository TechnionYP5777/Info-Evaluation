package infoeval.test.WikiDataTest;



import static org.junit.Assert.*;



import org.junit.Test;

import infoeval.main.WikiData.Connector;
/**
 * 
 * @author Netanel
 * @author osherh
 * @since 19-04-2017
 *
 */
public class ConnectorTest {
	
	
	@Test
	public void test1() throws Exception  {
	Connector con= new Connector();
	assertNotNull(con .getConnection().toString());
	}
}