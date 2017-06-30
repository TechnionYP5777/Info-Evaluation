package infoeval.test.mysqlTest;

import infoeval.main.mysql.SqlRunner;
import infoeval.main.mysql.Row;
import infoeval.main.mysql.TableEntry;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.BeforeClass;
import org.jsoup.Jsoup;
import org.junit.AfterClass;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 
 * @author Moshe
 * @author osherh
 * @Since 12-05-2017
 *
 */

/*
 * ATTENTION ! When you want to test this class , remove the @Ignore attributes.
 * I added it since the connector tries to read from the config.xml file which
 * won't be uploaded to GitHub and it causes travisCI to fail. like moshiko did
 * in the connectorTest it's relevant here too
 * 
 * @osherh
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

//	@Ignore
	@Test
	public void TestClearSerializedQueries() throws Exception {
		querun.clearSerializedQueries();
	}

	@Ignore
	@Test
	public void getBornInPlaceBeforeYearTest() throws Exception {
		for (TableEntry te : querun.getBornInPlaceBeforeYear("Japan", "1970")) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			assert Integer.parseInt(new SimpleDateFormat("yyyy").format(birthDate)) < 1970;
			assert birthPlace.contains("Japan");

		}
	}

	@Ignore
	@Test
	public void getDifferentDeathPlaceTest() throws Exception {
		for (TableEntry ¢ : querun.getDifferentDeathPlace())
			assertNotEquals(¢.getBirthPlace(), ¢.getDeathPlace());
	}

	@Ignore
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
			Row row = querun.runQuery(checkBirthPlaceQuery, inp).get(0);
			assertEquals(te.getBirthPlace(), row.row.get(0).getValue().cast(row.row.get(0).getKey()));
		}
	}

	@Ignore
	@Test
	public void getOccupationBetweenYearsTest() throws Exception {
		for (TableEntry te : querun.getOccupationBetweenYears("1840", "1920", "Politician")) {
			assertEquals(te.getOccupation(), "Politician");
			java.sql.Date birthDate = te.getBirthDate();
			SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
			String birthYear = formatYear.format(birthDate);
			assert Integer.parseInt(birthYear) >= 1840;
			formatYear.format(te.getDeathDate());
			assert Integer.parseInt(birthYear) <= 1920;
		}
	}

	@Ignore
	@Test
	public void getSpouselessBetweenYearsTest() throws Exception {
		for (TableEntry ¢ : querun.getSpouselessBetweenYears("1900", "1980"))
			assertEquals(¢.getSpouseName(), "No Spouse Name");
	}

//	@Ignore
	@Test
	public void getPersonalInfoTest() throws Exception {
		TableEntry te = querun.getPersonalInfo(Integer.parseInt((Jsoup
				.connect(
						"https://en.wikipedia.org/w/api.php?action=query&titles=Yuen_Ren_Chao&prop=pageimages&format=xml&pithumbsize=350")
				.get() + "").split("pageid=\"")[1].split("\"")[0]));

		System.out.println("Name is " + te.getName());
		
		System.out.println("Birth Place is " + te.getBirthPlace());

		System.out.println("Birth Place is " + te.getDeathPlace());

		Date birthDate = te.getBirthDate();
		if (birthDate != null)
			System.out.println("Birth Date is " + birthDate);

		Date deathDate = te.getDeathDate();
		if (deathDate != null)
			System.out.println("Death Date is " + deathDate);

		System.out.println("Occupation is " + te.getOccupation());

		System.out.println("Spouse Name is " + te.getSpouseName());

		System.out.println("Spouse Occupation is " + te.getSpouseOccupation());

		System.out.println("PhotoLink is " + te.getPhotoLink());

		System.out.println("Abstract is " + te.getOverview());

	}

	@Ignore
	@Test
	public void getPersonalInfoNotInDBTest() throws Exception {
		TableEntry te = querun.getPersonalInfo(

				Integer.parseInt((Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=Angela_Merkel"

						+ "&prop=pageimages&format=xml&pithumbsize=350").get() + "").split("pageid=\"")[1]
								.split("\"")[0]));

		assertEquals(te.getUrl(), "https://en.wikipedia.org/?curid=72671");
		assertEquals(te.getName(), "Angela Merkel");
		assertEquals(te.getBirthPlace(), "West Germany");
		assertEquals(te.getDeathPlace(), "No Death Place");
		assertEquals(te.getBirthExpandedPlace(), "West Germany");
		assertEquals(te.getDeathExpandedPlace(), "No Death Place");
		assertEquals((te.getBirthDate() + ""), "1954-07-17");
		assertEquals(te.getDeathDate(), null);
		assertEquals(te.getOccupation(), "No Occupation");
		assertEquals(te.getSpouseName(), "No Spouse");
		assertEquals(te.getSpouseOccupation(), "No Spouse Occupation");
		assertEquals(te.getPhotoLink(),
				"http://commons.wikimedia.org/wiki/Special:FilePath/Angela_Merkel_Juli_2010_-_3zu4.jpg");
		assertEquals(te.getOverview().split(" ")[1], "Dorothea");

	}
}