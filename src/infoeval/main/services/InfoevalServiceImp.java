package infoeval.main.services;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
import infoeval.main.Analysis.AnalyzeParagraph;
import infoeval.main.WikiData.WikiParsing;
import infoeval.main.mysql.SqlRunner;

import org.jsoup.Jsoup;
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
	SqlRunner runner;

	public InfoevalServiceImp() throws Exception {
		analyze = new AnalyzeParagraph();
		// Pre-Loading the classifiers used by NLP and openIE to enhance
		// performance.
		analyze.LoadNLPClassifiers();
		analyze.LoadIEClassifiers();
		runner = new SqlRunner();
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Query2", method = RequestMethod.GET)
	public ArrayList<TableEntry> getBornInPlaceYear(String place, String year)
			throws Exception {

		ArrayList<TableEntry> $ = runner.getBornInPlaceBeforeYear(place, year);
		logger.log(Level.INFO, "Born in place before year was called.\n Parameters:Place:" + place + ", Year:" + year);
		logger.log(Level.INFO, "list size:" + $.size());

		return $;
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Query1", method = RequestMethod.GET)
	public ArrayList<TableEntry> differentDeathPlace() throws Exception {
		logger.log(Level.INFO, "Born and died in different place was called");
		ArrayList<TableEntry> $ = runner.getDifferentDeathPlace();
		logger.log(Level.INFO, "list size:" + $.size());
		return $;

	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/SameOccupationCouples", method = RequestMethod.GET)
	public ArrayList<TableEntry> getSameOccupationCouples() throws Exception {
		logger.log(Level.INFO, "Get SameOccupationCouples was called.\n ");
		// Parse user's input:

		return runner.getSameOccupationCouples();

	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/checkAmbiguities", method = RequestMethod.GET)
	public ArrayList<String> checkAmbiguities(String name) throws IOException {
		String UpdatedName = updteName(name);
		try {
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			System.out.print("Trying to fetch from ,  https://en.wikipedia.org/wiki/" + UpdatedName);
			wiki.CheckAmbiguities();
			return !wiki.isConflictedName() ? null : wiki.getNames();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Arrests", method = RequestMethod.GET)
	public LinkedList<String> getArrested(String name) throws Exception {
		logger.log(Level.INFO, "Get Arrests was called.\n Parameters:Name:" + name);
		// Parse user's input:
		String UpdatedName = updteName(name);
		try {
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("arrested");
			new ArrayList<>();
			analyze.setParagraphs(wiki.getParagraphs());
			analyze.clearArrestsInformation();
			analyze.AnalyzeArrestsQuery();
			return analyze.RefineResults(10, analyze.getArrestsInformation());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Awards", method = RequestMethod.GET)
	public LinkedList<String> getAwards(String name) throws Exception {
		logger.log(Level.INFO, "Get Awards was called.\n Parameters:Name:" + name);
		// Parse user's input:
		try {
			String UpdatedName = updteName(name);
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("won");
			wiki.isConflictedName();
			wiki.getNames();
			new ArrayList<>();
			System.out.println(wiki.getParagraphs().text());
			analyze.setParagraphs(wiki.getParagraphs());
			analyze.clearAwardsInformation();
			analyze.AwardsQuery();
			return analyze.RefineResults(10, analyze.getAwardsInformation());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Dynamic", method = RequestMethod.GET)
	public LinkedList<String> getDynamic(String name, String query) throws Exception {
		logger.log(Level.INFO, "Get dynamic query results was called.\n Parameters:Name:" + name + " Query:" + query);
		// Parse user's input:
		try {
			analyze.dynamicQuery(name, query);
			return analyze.getDynamicInformation();
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/PersonalInformation", method = RequestMethod.GET)
	public TableEntry getPersonal_Information(String name) throws Exception {
		logger.log(Level.INFO, "Get personal information was called.\n Parameters:Name:" + name);
		// Parse user's input:

		String pageId = "", UpdatedName = updteName(name);
		System.out.println(UpdatedName);
		try {
			pageId = (Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=" + UpdatedName
					+ "&prop=pageimages&format=xml&pithumbsize=350").get() + "").split("pageid=\"")[1].split("\"")[0];
		} catch (Exception e) {
			throw e;
		}
		return runner.getPersonalInfo(Integer.parseInt(pageId));
	}

	public String updteName(String name) {
		String[] parts = name.split(" ");
		String UpdatedName = "";
		for (String part : parts) {
			part = part.toLowerCase();
			if (!"(".equals(part.split("")[0]))
				part = part.replaceFirst(part.split("")[0], part.split("")[0].toUpperCase());
			UpdatedName += part + "_";

		}

		return UpdatedName = UpdatedName.substring(0, UpdatedName.length() - 1);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}

}
