package infoeval.main.services;

import java.util.List;

/** 
 * @author geniashand
 * @since  06-05-2017
 * 
 */

public interface infoevalService {
	List<String> getBornIn(String place, int year); 
	//TODO: change String to an object of Entry 
	
	List<String> differentDeathPlace();

}
