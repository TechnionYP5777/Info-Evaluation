package infoeval.test.mysqlTest;



import org.junit.Test;

import infoeval.main.mysql.SqlQueriesRunner;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Moshe
 * @Since 12-05-2017
 *
 */
public class sqlRunnerTest {
	/**
	 * [[SuppressWarningsSpartan]]
	 */
	@Test
	public void getBornInPlaceBeforeYearTest() throws Exception {
		SqlQueriesRunner querun = new SqlQueriesRunner();
		List<TableEntry> lst = (querun.getBornInPlaceBeforeYear("Casablanca", "1954"));
		for (TableEntry te : lst) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assert bYear < 1954;
			assertEquals("Casablanca",birthPlace);
		}
		querun.close();
	}

//	@Test
//	public void getDifferentDeathPlaceTest() throws Exception {
//		for (TableEntry ¢ : (new SqlQueriesRunner()).getDifferentDeathPlace())
//			assertNotEquals(¢.getBirthPlace(), ¢.getDeathPlace());
//	}
}

