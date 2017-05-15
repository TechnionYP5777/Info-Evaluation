package infoeval.test.mysqlTest;

import org.junit.Test;

import infoeval.main.mysql.SqlQueriesRunner;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Optional;

public class SqlQueriesRunnerTest {
	@Test
	public void query1Test() throws Exception {
		for (TableEntry te : (new SqlQueriesRunner()).runQuery(1, Optional.of(new Object[] { 1993, "London" }))) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assert bYear < 1993;
			assertEquals(birthPlace, "London");
		}
	}

	@Test
	public void query2Test() throws Exception {
		for (TableEntry te : (new SqlQueriesRunner()).runQuery(2, null))
			assertNotEquals(te.getBirthPlace(), te.getDeathPlace());
	}

}
