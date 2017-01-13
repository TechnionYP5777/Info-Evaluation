package test.database;
import org.junit.Test;
import main.database.ReasonPair;
import static org.junit.Assert.assertEquals;

/**
 * 
 * @author MosheEliasof
 * 
 */

public class ReasonPairTest {
	
	
	@Test
	public void test1(){
	double p=0.5;
	ReasonPair rp = new ReasonPair(p, "drunk driving");
	assertEquals(rp.getReason(),"drunk driving");
	assertEquals(rp.getProbability(),p,0);
	}

}
