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
	ReasonPair rp = new ReasonPair();
	assertEquals(rp.getReason(),null);
	assertEquals(rp.getProbability(),0,0);
	}
	@Test
	public void test2(){
	double p=0.5;
	ReasonPair rp = new ReasonPair(p, "drunk driving");
	assertEquals(rp.getReason(),"drunk driving");
	assertEquals(rp.getProbability(),p,0);
	}
	@Test
	public void test3(){
	ReasonPair rp = new ReasonPair();
	assertEquals(rp.getReason(),null);
	assertEquals(rp.getProbability(),0,0);
	rp.setProbability(0.333);
	assertEquals(rp.getProbability(),0.333,0);
	rp.setReason("was a very bad boy");
	assertEquals(rp.getReason(),"was a very bad boy");
	}
}
