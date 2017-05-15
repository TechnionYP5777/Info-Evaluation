package infoeval.main.services;

import java.util.List;

import infoeval.main.mysql.TableEntry;

/** 
 * @author geniashand , Moshiko
 * @since  06-05-2017
 * 
 */

public interface InfoevalService {
	List<TableEntry> getBornInPlaceYear(int year,String place); 
	//TODO: change String to an object of Entry 
	
	List<TableEntry> differentDeathPlace();
	

}
