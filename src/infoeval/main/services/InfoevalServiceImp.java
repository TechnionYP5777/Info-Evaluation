package infoeval.main.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
import infoeval.main.Analysis.AnalyzeParagraph;
import infoeval.main.WikiData.WikiParsing;
import infoeval.main.mysql.SqlRunner;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
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

		SqlRunner runner = new SqlRunner();
		ArrayList<TableEntry> $ = runner.getBornInPlaceBeforeYear(place, year);
		logger.log(Level.INFO, "Born in place before year was called.\n Parameters:"+"Place:"+place+", Year:"+year);
		logger.log(Level.INFO, "list size:"+$.size());
		
		return $;
	}


	@Override
	@RequestMapping(path="Queries/Query1",method = RequestMethod.GET)
	public ArrayList<TableEntry> differentDeathPlace() throws Exception {
		logger.log(Level.INFO, "Born and died in different place was called");
		
		//ArrayList<TableEntry> $ = new ArrayList<>();
    	//
    	//java.sql.Date utilDate1 = java.sql.Date.valueOf( LocalDate.of(1912, 6, 23) );
    //	java.sql.Date utilDate2 = java.sql.Date.valueOf( LocalDate.of(1954, 6, 7) );
    	//
	//	TableEntry entry = new TableEntry(null, "Alan Turing", "Maida Vale", "Wilmslow", utilDate1, utilDate2,"","","");
		//$.add(entry);
//
		//utilDate1 = java.sql.Date.valueOf((LocalDate.of(1886, 10, 16)));
		//utilDate2 = java.sql.Date.valueOf(LocalDate.of(1973, 12, 1));
//
		//entry = new TableEntry(null, "David Ben-Gurion", "plonsk", "Ramat Gan", utilDate1, utilDate2,"","","");
		//$.add(entry);
		//return $;
	
	
	
	//This is the code that should really run. The code above is just a stub. We don't run the actual Java code to find real resultsbecause the server is too slow for now ...
		/*
		 * TODO: DO NOT REMOVE THE CODE BELOW !!!! @MOSHIKO
		 * 
		 */
		SqlRunner runner = new SqlRunner();
	ArrayList<TableEntry> $ = runner.getDifferentDeathPlace();
	logger.log(Level.INFO, "list size:"+$.size());
	return $;
	
	}
	
	
	@Override
	@RequestMapping(path="Queries/SameOccupationCouples",method = RequestMethod.GET)
	public ArrayList<TableEntry> getSameOccupationCouples() throws Exception {
		logger.log(Level.INFO, "Get SameOccupationCouples was called.\n ");
		//Parse user's input:
		
		return (new SqlRunner()).getSameOccupationCouples();
		
	}
	
	@Override
	@RequestMapping(path="Queries/Arrests",method = RequestMethod.GET)
	public LinkedList<String> getArrested(String name) throws Exception {
		logger.log(Level.INFO, "Get Arrests was called.\n Parameters:"+"Name:"+name);
		//Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/"+name));
		wiki.Parse("arrested");
		AnalyzeParagraph analyze = new AnalyzeParagraph(wiki.getParagraphs());
		analyze.AnalyzeArrestsQuery();
		//LinkedList<String> results = analyze.getInformation();
		return analyze.RefineResults(2);
	}
	
	@Override
	@RequestMapping(path="Queries/Awards",method = RequestMethod.GET)
	public LinkedList<String> getAwards(String name) throws Exception {
		logger.log(Level.INFO, "Get Awards was called.\n Parameters:"+"Name:"+name);
		//Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		
		WikiParsing wiki =  (new WikiParsing("https://en.wikipedia.org/wiki/"+name));
		wiki.Parse("won");
		AnalyzeParagraph analyze = new AnalyzeParagraph(wiki.getParagraphs());
		analyze.AnalyzeAwardsQuery();
		//LinkedList<String> results = analyze.getInformation();
		return analyze.RefineResults(2);
	}
	
	@Override
	@RequestMapping(path="Queries/PersonalInformation",method = RequestMethod.GET)
	public TableEntry getPersonal_Information(String name) throws Exception {
		logger.log(Level.INFO, "Get Arrests was called.\n Parameters:"+"Name:"+name);
		//Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		return (new SqlRunner()).getPersonalInfo(name);
		
	}
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}
	
	

}
