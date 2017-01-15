package test.database;
import static org.junit.Assert.*;

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
}