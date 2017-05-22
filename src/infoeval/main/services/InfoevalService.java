package infoeval.main.services;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
	
	List<TableEntry> differentDeathPlace();
	

}
