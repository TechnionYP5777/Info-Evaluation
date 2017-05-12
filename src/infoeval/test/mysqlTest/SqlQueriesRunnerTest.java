package infoeval.test.mysqlTest;

import org.junit.Test;

import infoeval.main.mysql.SqlQueriesRunner;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 * 
 * @author osherh
 * @Since 12-05-2017
 *
 */
public class SqlQueriesRunnerTest {
	@Test
	public void query1Test() throws Exception{
		SqlQueriesRunner runner = new SqlQueriesRunner();
		Object[] inp = {1993,"London"};
		List<TableEntry> resList = runner.runQuery(1, inp);
		for(TableEntry te : resList){
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assertTrue(bYear < 1993);
			assertEquals(birthPlace,"London");
		}
	}
	
	@Test
	public void query2Test() throws Exception{
		SqlQueriesRunner runner = new SqlQueriesRunner();
		List<TableEntry> resList = runner.runQuery(2,null);
		for(TableEntry te : resList){
			String birthPlace = te.getBirthPlace();
			String deathPlace = te.getDeathPlace();
			assertNotEquals(birthPlace,deathPlace);
		}
	}
}
