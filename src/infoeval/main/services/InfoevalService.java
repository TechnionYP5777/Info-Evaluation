package infoeval.main.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import infoeval.main.mysql.TableEntry;

/** 
 * @author geniashand , Moshiko
 * @since  06-05-2017
 * 
 */

public interface InfoevalService {
	List<TableEntry> getBornInPlaceYear(String year,String place) throws  Exception; 
	//TODO: change String to an object of Entry 
	
	List<TableEntry> differentDeathPlace() throws Exception;

	LinkedList<String> getArrested(String name) throws Exception;
	

}
