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
import infoeval.main.services.InfoevalServiceImp;

public class WikiParsingTest {
	@Test
	public void test1() throws Exception {
		
		System.out.println((new WikiParsing("https://en.wikipedia.org/wiki/Shia_LaBeouf")).Parse("arrested"));
		
	}

	@Test
	public void test2() throws IOException {
		System.out.print((new WikiParsing("https://en.wikipedia.org/wiki/Justin_Timberlake")).Parse("awarded"));
	}
	@Test
	public void test3() throws IOException{
		WikiParsing wp=new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Williams");
		wp.Parse("refer");
		assert wp.isConflictedName();


	}
	@Test
	public void test4() throws IOException{
		WikiParsing wp=new WikiParsing("https://en.wikipedia.org/wiki/Michael_Jackson");
		wp.Parse("refer");
		assert !wp.isConflictedName();
		

	}
	@Test
	public void test5() throws IOException{
		WikiParsing wp=new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Williams");
		wp.Parse("refer");
		assert wp.getNames().size()==4;
		assert wp.getNames().contains("Michelle Williams (swimmer)");
		assert wp.getNames().contains("Michelle Williams (singer)");
		assert wp.getNames().contains("Michelle Williams (actress)");
		assert wp.getNames().contains("Michelle Ann Williams");
	}
	@Test
	public void test6() throws IOException{
		WikiParsing wp=new WikiParsing("https://en.wikipedia.org/wiki/Michelle_Ann_Williams");
		wp.Parse("refer");
		assert wp.getNames().isEmpty();
		

	}
}
	
