package test.database;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import main.database.InteractiveTableTuple;
 
/**
 * 
 * @author Netanel Felcher
 *
 */
public class InteractiveTableTupleTest{
	
	@Test public void test0(){
		InteractiveTableTuple t=new InteractiveTableTuple();
		assertNull(t.getDate());
		assertNull(t.getName());
		assertNull(t.getRegularDate());
		assertTrue(t.getKeyWords().isEmpty());
		assertTrue(t.getReasons().isEmpty());
	}		
	@Test public void test1(){
		InteractiveTableTuple t=new InteractiveTableTuple("Justin Bieber", "12/02/2016", null);
		assertEquals(t.getDate(), "12/02/2016");
		assertEquals(t.getName(), "Justin Bieber");
		assertEquals((t.getRegularDate() + ""), "Fri Dec 02 00:00:00 IST 2016");
		assertTrue(t.getKeyWords().isEmpty());
	}		
}