package test.WikiDataTest;

import org.junit.Test;

import static org.junit.Assert.*;


import main.WikiData.Extractor;

/**
 * 
 * @author Netanel
 * @since 05-04-2017
 * 
 *
 */
public class ExtractorTest {
 @Test public void test1(){
	 Extractor extr= new Extractor();
	 extr.executeQuery();
	 assertEquals(extr.getResults().size(), 10000);
 }
	
}