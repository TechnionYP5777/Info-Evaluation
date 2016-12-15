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
        Sentence sent = new Sentence("Justin Bieber is in the sky with diamonds on Jan. 26");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        assertEquals("Justin Bieber ",anal.AnalyzeSimple().getName());
        LocalDate date=anal.AnalyzeSimple().getRegularDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(1, date.getMonthValue());
        assertEquals(26, date.getDayOfMonth());
        assertEquals(anal.AnalyzeSimple().getDate(), "26/01/1970");
	}
	
	@Test
	public void test2(){		
		Sentence sent = new Sentence("Justin Bieber was arrested for drunk driving in Georgia on February 22, 2015 .");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Justin Bieber",tt.getName());
        assertEquals("drunk driving in Georgia",tt.getReason());
        assertEquals("22/02/2015",tt.getDate());
        
	}
	
	@Test
	public void test3(){		
		Sentence sent = new Sentence("Vin Diesel was arrested for furious driving on August 14, 2002 in California");
        AnalyzeParagragh anal = new AnalyzeParagragh(sent);
        TableTuple tt = anal.Analyze();
        assertEquals("Vin Diesel",tt.getName());
        assertEquals("driving furious in California",tt.getReason());
        assertEquals("14/08/2002",tt.getDate());
        
	}
	
	
}