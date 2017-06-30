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

	@Test
	public void TestClearSerializedQueries() throws Exception {
		querun.clearSerializedQueries();
	}

	@Test
	public void getBornInPlaceBeforeYearTest() throws Exception {
		for (TableEntry te : querun.getBornInPlaceBeforeYear("Japan", "1970")) {
			java.sql.Date birthDate = te.getBirthDate();
			String birthPlace = te.getBirthPlace();
			assert Integer.parseInt(new SimpleDateFormat("yyyy").format(birthDate)) < 1970;
			assert birthPlace.contains("Japan");

		}
	}

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
			Row row = querun.runQuery(checkBirthPlaceQuery, inp).get(0);
			assertEquals(te.getBirthPlace(), row.row.get(0).getValue().cast(row.row.get(0).getKey()));
		}
	}

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

	@Test
	public void getSpouselessBetweenYearsTest() throws Exception {
		for (TableEntry ¢ : querun.getSpouselessBetweenYears("1900", "1980"))
			assertEquals(¢.getSpouseName(), "No Spouse Name");
	}

	@Test
	public void getPersonalInfoTest() throws Exception {
		TableEntry te = querun.getPersonalInfo(Integer.parseInt((Jsoup
				.connect(
						"https://en.wikipedia.org/w/api.php?action=query&titles=Yuen_Ren_Chao&prop=pageimages&format=xml&pithumbsize=350")
				.get() + "").split("pageid=\"")[1].split("\"")[0]));
		assertEquals(te.getUrl(), "https://en.wikipedia.org/?curid=303324");
		assertEquals(te.getName(), "Yuen Ren Chao");
		assertEquals(te.getBirthPlace(), "Qing dynasty");
		assertEquals(te.getDeathPlace(), "United States");
		assertEquals(te.getBirthExpandedPlace(), "Qing dynasty");
		assertEquals(te.getDeathExpandedPlace(), "Cambridge, Massachusetts");
		assertEquals((te.getBirthDate() + ""), "1892-11-03");
		assertEquals(te.getDeathDate() + "", "1982-02-25");
		assertEquals(te.getOccupation(), "No Occupation");
		assertEquals(te.getSpouseName(), "No Spouse");
		assertEquals(te.getSpouseOccupation(), "No Spouse Occupation");
		assertEquals(te.getPhotoLink(), "http://commons.wikimedia.org/wiki/Special:FilePath/Zhao_Yuanren.jpg");
		System.out.println(te.getOverview());
		assertEquals(te.getOverview(),
				"Yuen Ren Chao (Chinese: 趙元任; pinyin: Zhào Yuánrèn; 3 November 1892 – 25 February 1982), was a Chinese-American linguist, educator, scholar, poet, and composer, best known for his contributions to the modern study of Chinese phonology and grammar. Chao was born and raised in China, then attended university in the United States, where he earned degrees from Cornell University and Harvard University. A naturally-gifted polyglot and linguist, Chao is best known for his Mandarin Primer, one of the most widely used Mandarin Chinese textbooks in the 20th century, and his Gwoyeu Romatzyh romanization scheme, which can, unlike pinyin and other romanization systems, transcribe Mandarin Chinese pronunciation without needing diacritics to indicate words' tone.");
	}

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