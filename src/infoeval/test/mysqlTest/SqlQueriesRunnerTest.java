package infoeval.test.mysqlTest;

import org.junit.Test;

import infoeval.main.mysql.SqlQueriesRunner;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;


/**
 * 
 * @author osherh
 * @Since 12-05-2017
 *
 */
public class SqlQueriesRunnerTest {
	@Test
	public void query1Test() throws Exception{
		for (TableEntry te : (new SqlQueriesRunner()).runQuery(1, (new Object[] { 1993, "London" }))) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assertTrue(bYear < 1993);
			assertEquals(birthPlace, "London");
		}
	}
	
	@Test
	public void query2Test() throws Exception{
		for (TableEntry ¢ : (new SqlQueriesRunner()).runQuery(2, null))
			assertNotEquals(¢.getBirthPlace(), ¢.getDeathPlace());
	}
}
