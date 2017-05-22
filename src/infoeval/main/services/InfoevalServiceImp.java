package infoeval.main.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
import infoeval.main.mysql.SqlRunner;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author geniashand , Moshiko
 * @Since 06-05-2017
 * 
 *        This class is the functions of the web services
 *
 */

@EnableAutoConfiguration
@RestController
public class InfoevalServiceImp implements InfoevalService {
	private static final Logger logger = Logger.getLogger("InfoevalServiceImp".getClass().getName());

	@Override
	@RequestMapping(path="Queries/Query2",method = RequestMethod.GET)
	public ArrayList<TableEntry> getBornInPlaceYear(String place ,String year) throws Exception {
//		List<TableEntry> $ = new ArrayList<TableEntry>();
//		java.sql.Date utilDate1 = java.sql.Date.valueOf( LocalDate.of(1912, 6, 23) );
//		TableEntry entry = new TableEntry(null, "Yevgenia Shandalov", "Maida Vale", null, utilDate1, null,"","","");
//		$.add(entry);
//		
//		utilDate1 = java.sql.Date.valueOf((LocalDate.of(1886, 10, 16)));
//		entry = new TableEntry(null, "Gavriel Shandalov", "plonsk", null, utilDate1, null,"","","");
//		$.add(entry);
//		
//		return $;
		SqlRunner runner = new SqlRunner();
		ArrayList<TableEntry> lst = runner.getBornInPlaceBeforeYear(place, year);
		logger.log(Level.INFO, "Born in place before year was called.\n Parameters:"+"Place:"+place+", Year:"+year);
		logger.log(Level.INFO, "list size:"+lst.size());
		
		return lst;
	}

	@Override
	@RequestMapping("Queries/Query1")
	public ArrayList<TableEntry> differentDeathPlace() {
		ArrayList<TableEntry> $ = new ArrayList<>();
    	
    	java.sql.Date utilDate1 = java.sql.Date.valueOf( LocalDate.of(1912, 6, 23) );
    	java.sql.Date utilDate2 = java.sql.Date.valueOf( LocalDate.of(1954, 6, 7) );
    	
		TableEntry entry = new TableEntry(null, "Alan Turing", "Maida Vale", "Wilmslow", utilDate1, utilDate2,"","","");
		$.add(entry);

		utilDate1 = java.sql.Date.valueOf((LocalDate.of(1886, 10, 16)));
		utilDate2 = java.sql.Date.valueOf(LocalDate.of(1973, 12, 1));

		entry = new TableEntry(null, "David Ben-Gurion", "plonsk", "Ramat Gan", utilDate1, utilDate2,"","","");
		$.add(entry);
		return $;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}

}
