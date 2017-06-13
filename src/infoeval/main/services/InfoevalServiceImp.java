package infoeval.main.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
import infoeval.main.Analysis.AnalyzeParagraph;
import infoeval.main.WikiData.WikiParsing;
import infoeval.main.mysql.SqlRunner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
//import org.springframework.stereotype.*;
//import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
	private static AnalyzeParagraph analyze;

	public InfoevalServiceImp() throws IOException {
		analyze = new AnalyzeParagraph();
		//Pre-Loading the classifiers used by NLP and openIE to enhance performance.
		analyze.LoadNLPClassifiers();
		//analyze.LoadIEClassifiers();
	}

	@Override
	@RequestMapping(path = "Queries/Query2", method = RequestMethod.GET)
	public ArrayList<TableEntry> getBornInPlaceYear(String place, String year) throws Exception {

		SqlRunner runner = new SqlRunner();
		ArrayList<TableEntry> $ = runner.getBornInPlaceBeforeYear(place, year);
		logger.log(Level.INFO,
				"Born in place before year was called.\n Parameters:" + "Place:" + place + ", Year:" + year);
		logger.log(Level.INFO, "list size:" + $.size());

		return $;
	}

	@Override
	@RequestMapping(path = "Queries/Query1", method = RequestMethod.GET)
	public ArrayList<TableEntry> differentDeathPlace() throws Exception {
		logger.log(Level.INFO, "Born and died in different place was called");
		SqlRunner runner = new SqlRunner();
		ArrayList<TableEntry> $ = runner.getDifferentDeathPlace();
		logger.log(Level.INFO, "list size:" + $.size());
		return $;

	}

	@Override
	@RequestMapping(path = "Queries/SameOccupationCouples", method = RequestMethod.GET)
	public ArrayList<TableEntry> getSameOccupationCouples() throws Exception {
		logger.log(Level.INFO, "Get SameOccupationCouples was called.\n ");
		// Parse user's input:

		return (new SqlRunner()).getSameOccupationCouples();

	}

	@Override
	@RequestMapping(path = "Queries/Arrests", method = RequestMethod.GET)
	public LinkedList<String> getArrested(String name) throws Exception {
		logger.log(Level.INFO, "Get Arrests was called.\n Parameters:" + "Name:" + name);
		// Parse user's input:
		name = name.trim().replaceAll(" ", "_");

		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + name));
		wiki.Parse("arrested");
		ArrayList<String> names= new ArrayList<>();
		boolean flag=wiki.isConflictedName();
		if(flag){
			names=wiki.getNames();
		}
		analyze.setParagraphs(wiki.getParagraphs());
		analyze.AnalyzeArrestsQuery();
		return analyze.RefineResults(10);
	}

	@Override
	@RequestMapping(path = "Queries/Awards", method = RequestMethod.GET)
	public LinkedList<String> getAwards(String name) throws Exception {
		logger.log(Level.INFO, "Get Awards was called.\n Parameters:" + "Name:" + name);
		// Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + name));
		wiki.Parse("won");
		ArrayList<String> names= new ArrayList<>();
		boolean flag=wiki.isConflictedName();
		if(flag){
			names=wiki.getNames();
		}
		analyze.setParagraphs(wiki.getParagraphs());
		analyze.AwardsQuery();
		return analyze.RefineResults(10);
	}
	
	@Override
	@RequestMapping(path = "Queries/Dynamic", method = RequestMethod.GET)
	public LinkedList<String> getDynamic(String name,String query) throws Exception {
		logger.log(Level.INFO, "Get dynamic query results was called.\n Parameters:" + "Name:" + name+ " Query:"+query);
		// Parse user's input:
		analyze.dynamicQuery(name, query);
		return analyze.RefineResults(10);
	}

	@Override
	@RequestMapping(path = "Queries/PersonalInformation", method = RequestMethod.GET)
	public TableEntry getPersonal_Information(String name) throws Exception {
		logger.log(Level.INFO, "Get personal information was called.\n Parameters:" + "Name:" + name);
		// Parse user's input:
		name = name.trim().replaceAll(" ", "_");
		Document doc = Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles="+name+"&prop=pageimages&format=xml&pithumbsize=350").get();
		String pageId=doc.toString().split("pageid=\"")[1].split("\"")[0];
		return (new SqlRunner()).getPersonalInfo(name);

	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}

}
