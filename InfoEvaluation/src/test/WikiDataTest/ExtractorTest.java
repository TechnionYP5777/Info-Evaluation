package WikiDataTest;

import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.ResultSetRewindable;
import org.junit.Test;

import static org.junit.Assert.*;


import WikiData.Extractor;

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
	 ResultSetRewindable rs= extr.getResults();
	ResultSetFormatter.out(rs);
	 assertEquals(rs.size(), 10000);
 }
	
}