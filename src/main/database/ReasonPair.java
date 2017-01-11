package main.database;


/**
*
* @author MosheEliasof This class represents  a pair of reason and its probability to 
* be the actual reason.
*
*/
public class ReasonPair {
	private double probability;
	private String reason;
	
	public ReasonPair() {
		probability=0;
		reason = null;
	}
	
	public ReasonPair(double p, String reason){
		this.probability = p;
		this.reason = reason;
	}
	
	public double getProbability(){
		return this.probability;
	}
	
	public String getReason(){
		return this.reason;
	}

}
