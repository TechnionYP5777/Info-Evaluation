package infoeval.test.WikiDataTest;

import java.io.IOException;

/**
 * 
 * @author moshiko
 * @since  05-04-2017
 * 
 * 
 */

import org.junit.Test;

import infoeval.main.WikiData.WikiParsing;

public class WikiParsingTest {
	@Test
	public void test1() throws Exception {
		assert new WikiParsing("https://en.wikipedia.org/wiki/Shia_LaBeouf").Parse("arrested").contains(
				"In February 2005, LaBeouf was arrested by police in Los Angeles and charged with assault with a deadly weapon after threatening his neighbor by driving into his car.");
	}

	@Test
	public void test2() throws IOException {
		assert new WikiParsing("https://en.wikipedia.org/wiki/Justin_Timberlake").Parse("awarded")
				.contains("In February 2008, Timberlake was awarded two Grammy Awards.");

	}

	@Test
	public void test3() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Williams");
		wp.Parse("refer");
		assert wp.isConflictedName();

	}

	@Test
	public void test4() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Michael_Jackson");
		wp.Parse("refer");
		assert !wp.isConflictedName();

	}

	@Test
	public void test5() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Williams");
		wp.Parse("refer");
		wp.CheckAmbiguities();
		assert wp.getNames().size() == 4;
		assert wp.getNames().contains("Michelle Williams (swimmer)");
		assert wp.getNames().contains("Michelle Williams (singer)");
		assert wp.getNames().contains("Michelle Williams (actress)");
		assert wp.getNames().contains("Michelle Ann Williams");
	}

	@Test
	public void test6() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Ann_Williams");
		wp.Parse("refer");
		wp.CheckAmbiguities();
		assert wp.getNames().isEmpty();

	}

	@Test
	public void test7() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Slash");
		wp.Parse("married");
		wp.CheckAmbiguities();
		assert wp.isConflictedName();

	}

	@Test
	public void test8() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Slash");
		wp.Parse("married");
		wp.CheckAmbiguities();
		assert wp.getNames().size() == 28;

	}

	@Test
	public void test9() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Agüero");
		wp.Parse("refer");
		wp.CheckAmbiguities();
		assert wp.getNames().size() == 13;

	}

	@Test
	public void test10() throws IOException {
		WikiParsing wp = new WikiParsing("https://en.wikipedia.org/wiki/Agnieszka");
		wp.Parse("refer");
		wp.CheckAmbiguities();
		assert wp.getNames().size() == 26;

	}

}
