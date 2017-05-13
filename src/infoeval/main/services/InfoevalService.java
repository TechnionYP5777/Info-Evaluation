package infoeval.main.services;

import java.util.List;

import infoeval.main.mysql.TableEntry;

/** 
 * @author geniashand , Moshiko
 * @since  06-05-2017
 * 
 */

public interface InfoevalService {
	List<TableEntry> getBornInPlaceYear(); 
	
	List<TableEntry> differentDeathPlace();
	

}
