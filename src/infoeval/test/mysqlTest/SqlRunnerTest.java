package infoeval.test.mysqlTest;

import infoeval.main.mysql.SqlRunner;
import infoeval.main.mysql.Row;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 
 * @author Moshe
 * @author osherh
 * @Since 12-05-2017
 *
 */
public class SqlRunnerTest {
	static SqlRunner querun;

	/**
	 * [[SuppressWarningsSpartan]]
	 * 
	 * @throws Exception
	 */

	@BeforeClass
	public static void initRunner() throws Exception {
		querun = new SqlRunner();
	}

	@AfterClass
	public static void close() {
		querun.close();
	}
	
	@Test
	public void TestClearSerializedQueries() throws Exception{
		querun.clearSerializedQueries();
	}

	@Test
	public void getBornInPlaceBeforeYearTest() throws Exception {
		for (TableEntry te : querun.getBornInPlaceBeforeYear("Casablanca", "1954")) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assert bYear < 1954;
			assertEquals("Casablanca", birthPlace);
		}
	}

	@Ignore
	@Test
	public void getDifferentDeathPlaceTest() throws Exception {
		for (TableEntry ¢ : querun.getDifferentDeathPlace())
			assertNotEquals(¢.getBirthPlace(), ¢.getDeathPlace());
	}

	@Test
	public void getSameOccupationCouplesTest() throws Exception {
		for (TableEntry ¢ : querun.getSameOccupationCouples())
			assertEquals(¢.getOccupation(), ¢.getSpouseOccupation());
	}

	@Ignore
	@Test
	public void getSameBirthPlaceCouplesTest() throws Exception {
		for (TableEntry te : querun.getSameBirthPlaceCouples()) {
			String sName = te.getSpouseName(),
					checkBirthPlaceQuery = "SELECT birthPlace FROM basic_info WHERE name=? LIMIT 1";
			Object[] inp = new Object[1];
			inp[0] = sName;
			ArrayList<Row> res = querun.runQuery(checkBirthPlaceQuery, inp);
			Row row = res.get(0);
			assertEquals(te.getBirthPlace(), (String) row.row.get(0).getValue().cast(row.row.get(0).getKey()));
		}
	}

	@Test
	public void getOccupationBetweenYearsTest() throws Exception {
		for (TableEntry te : querun.getOccupationBetweenYears("1840", "1920", "Politician")) {
			assertEquals(te.getOccupation(), "Politician");
			java.sql.Date birthDate = te.getBirthDate();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assert bYear >= 1840;
			java.sql.Date deathDate = te.getDeathDate();
			formatYear.format(deathDate);
			assert Integer.parseInt(birthYear) <= 1920;
		}
	}

	@Test
	public void getSpouselessBetweenYearsTest() throws Exception {
		for (TableEntry ¢ : querun.getSpouselessBetweenYears("1900", "1980"))
			assertEquals(¢.getSpouseName(), "No Spouse Name");
	}
}
