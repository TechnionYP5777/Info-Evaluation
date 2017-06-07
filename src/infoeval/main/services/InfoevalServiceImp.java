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
	private static final AnalyzeParagraph analyze = new AnalyzeParagraph();

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
		//AnalyzeParagraph analyze = new AnalyzeParagraph(wiki.getParagraphs());
		analyze.setParagraphs(wiki.getParagraphs());
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
		//AnalyzeParagraph analyze = new AnalyzeParagraph();
		analyze.setParagraphs(wiki.getParagraphs());
		analyze.AnalyzeAwardsQuery();
		return analyze.RefineResults(2);
	}
	
	@Override
	@RequestMapping(path="Queries/PersonalInformation",method = RequestMethod.GET)
	public TableEntry getPersonal_Information(String name) throws Exception {
		logger.log(Level.INFO, "Get personal information was called.\n Parameters:"+"Name:"+name);
		//Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		return (new SqlRunner()).getPersonalInfo(name);
		
	}
	
	
	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}
	
	

}
