package test.Analyze;

import static org.junit.Assert.*;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.Test;

import main.Analyze.AnalyzeParagragh;
import main.database.TableTuple;
import edu.stanford.nlp.simple.Sentence;
/**
 * 
 * @author MosheEliasof 
 *
 */

public class AnalyzeParagraphTest { 
	@Test
	public void test1() {
        Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds on Jan. 26 1970");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        assertEquals("Justin Bieber ",anal.AnalyzeSimple().getName());
        LocalDate date=anal.AnalyzeSimple().getRegularDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(1, date.getMonthValue());
        assertEquals(26, date.getDayOfMonth());
        assertEquals(anal.AnalyzeSimple().getDate(), "01/26/1970");
	}
	
	@Test
	public void test2(){		
		Sentence sent = new Sentence("Justin Bieber was arrested for drunk driving in Georgia on February 22, 2015 .");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Justin Bieber",tt.getName());
        assertEquals("drunk driving in Georgia",tt.getReason());
        assertEquals("02/22/2015",tt.getDate());
        
	}
	
	@Test
	public void test3(){		
		Sentence sent = new Sentence("Vin Diesel was arrested for furious driving on August 14, 2002 in California");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Vin Diesel",tt.getName());
        assertEquals("driving furious in California",tt.getReason());
        assertEquals("08/14/2002",tt.getDate());
        
	}
	
	@Test
	public void test4(){
		Sentence sent = new Sentence("Axl Rose was arrested for consuming drugs on October 5th 2012 in NY during rock concert .");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Axl Rose",tt.getName());
        assertEquals("drugs consuming during concert",tt.getReason());
        assertEquals("10/05/2012",tt.getDate());
		
	}
	
	@Test
	public void test5(){
		Sentence sent = new Sentence("Axl Rose was arrested after driving drunk in New Zeland on March 1.");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Axl Rose",tt.getName());
        assertEquals("driving drunk in New Zeland",tt.getReason());
        assertEquals("03/01/2015",tt.getDate());
		
	}
	
	@Test
	public void test6() {
		Sentence sent = new Sentence("Tito Ortiz was arrested for driving under the influence after he drove his Porsche Panamera into the concrete median on Jan 6 on the 405 freeway in L.A. at 4am.\n");
		AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Tito Ortiz",tt.getName());
        assertEquals("driving under influence",tt.getReason());
        assertEquals("01/06/2015",tt.getDate());
	}
	
	@Test
	public void test8() {
		Sentence sent = new Sentence("David Cassidy was arrested for driving under the influence on Jan 10 after he allegedly took a breathalyzer test and was found to be over twice the legal limit.\n");
		AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("David Cassidy",tt.getName());
        assertEquals("driving under influence",tt.getReason());
        assertEquals("01/10/2015",tt.getDate());
	}
	
	@Test
	public void test9() {
		Sentence sent = new Sentence("Soulja Boy was arrested for possession of a loaded gun in Los Angeles on Jan 22.\n");
		AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Soulja Boy",tt.getName());
        assertEquals("possession of gun",tt.getReason());
        assertEquals("01/22/2015",tt.getDate());
	}
	
}