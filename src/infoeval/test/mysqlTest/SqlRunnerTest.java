package infoeval.test.mysqlTest;

import infoeval.main.mysql.SqlRunner;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 
 * @author Moshe
 * @author osherh
 * @Since 12-05-2017
 *
 */
public class SqlRunnerTest {
	SqlRunner querun;
	
	/**
	 *
	 * @throws Exception 
	 */
	

	
	@Test
	public void getBornInPlaceBeforeYearTest() throws Exception {
		SqlRunner querun = new SqlRunner();
		List<TableEntry> lst = (querun.getBornInPlaceBeforeYear("Frankfurt", "1900"));
		for (TableEntry te : lst) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			int bYear = Integer.parseInt(birthYear);
			assert bYear < 1900;
			assertEquals("Frankfurt",birthPlace);
		}
		querun.close();
	}

	
	
//	@Test
//	public void getDifferentDeathPlaceTest() throws Exception {
//		SqlRunner querun = new SqlRunner();
//		List<TableEntry> lst = (querun.getDifferentDeathPlace());
//		for (TableEntry ¢ : lst) {
//			assertNotEquals(¢.getBirthPlace(), ¢.getDeathPlace());
//			System.out.println("Birth place:"+¢.getBirthPlace()+" Death place:"+¢.getDeathPlace());
//		}
//		querun.close();
//	}
	
/*	@Ignore
	@Test
	public getSameOccupationCouplesTest() throws Exception{
		SqlRunner querun = new SqlRunner();
		List<TableEntry> lst = querun.getSameOccupationCouples();
		for (TableEntry te : lst) {
			
			//java.sql.Date birthDate = te.getBirthDate();
			//String birthPlace = te.getBirthPlace();
			//SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			//String birthYear = formatYear.format(birthDate);
			//int bYear = Integer.parseInt(birthYear);
			//assert bYear < 1954;
			//assertEquals("Casablanca",birthPlace);
		}
	querun.close();
	}*/
}

