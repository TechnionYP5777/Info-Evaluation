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
	public void test1() throws IOException {

		System.out.print((new WikiParsing("https://en.wikipedia.org/wiki/Shia_LaBeouf")).Parse("arrested"));

	}

	@Test
	public void test2() throws IOException {
		System.out.print((new WikiParsing("https://en.wikipedia.org/wiki/Justin_Timberlake")).Parse("awarded"));
	}

}
