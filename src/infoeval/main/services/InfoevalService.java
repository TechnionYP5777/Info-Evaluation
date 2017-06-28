package infoeval.main.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import infoeval.main.mysql.TableEntry;

/**
 * @author geniashand , Moshiko
 * @since 06-05-2017
 * 
 */

public interface InfoevalService {
	List<TableEntry> getBornInPlaceYear(String year, String place) throws Exception;
	// TODO: change String to an object of Entry

	List<TableEntry> differentDeathPlace() throws Exception;

	LinkedList<String> getArrested(String name) throws Exception;

	TableEntry getPersonal_Information(String name) throws Exception;

	LinkedList<String> getAwards(String name) throws Exception;

	ArrayList<TableEntry> getSameOccupationCouples() throws Exception;

	LinkedList<String> getDynamic(String name, String query) throws Exception;

	ArrayList<String> checkAmbiguities(String name) throws IOException;

}
