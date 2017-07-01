package infoeval.main.services;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
import infoeval.main.Analysis.AnalyzeParagraph;
import infoeval.main.WikiData.WikiParsing;
import infoeval.main.mysql.SqlRunner;

import org.jsoup.Jsoup;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
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
	private static AnalyzeParagraph analyze;
	private static final Logger logger =Logger.getLogger("InfoevalServiceImp".getClass().getName());
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
	public synchronized ArrayList<TableEntry> getBornInPlaceYear(String place, String year) throws Exception {
		ArrayList<TableEntry> $ = runner.getBornInPlaceBeforeYear(place, year);
		
		logger.log(Level.INFO, "Born in place before year was called.\n Parameters:Place:" + place + ", Year:" + year);
		logger.log(Level.INFO, "list size:" + $.size());

		return $;
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Query1", method = RequestMethod.GET)
	public synchronized ArrayList<TableEntry> differentDeathPlace() throws Exception {
		logger.log(Level.INFO, "Born and died in different place was called");
		ArrayList<TableEntry> $ = runner.getDifferentDeathPlace();
		logger.log(Level.INFO, "list size:" + $.size());
		return $;
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/SameOccupationCouples", method = RequestMethod.GET)
	public synchronized ArrayList<TableEntry> getSameOccupationCouples() throws Exception {
		logger.log(Level.INFO, "Get couples with same occupation");
		ArrayList<TableEntry> $ = runner.getSameOccupationCouples();
		logger.log(Level.INFO, "list size:" + $.size());
		return $;
		//return runner.getSameOccupationCouples();

	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/checkAmbiguities", method = RequestMethod.GET)
	public synchronized ArrayList<String> checkAmbiguities(String name) throws IOException {
		String UpdatedName = updteName(name);
		try {
			logger.log(Level.INFO, "Checking ambiguities in URL: "+"https://en.wikipedia.org/wiki/" + UpdatedName);

			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			// System.out.print("Trying to fetch from ,
			// https://en.wikipedia.org/wiki/" + UpdatedName);
			wiki.CheckAmbiguities();
			return !wiki.isConflictedName() ? null : wiki.getNames();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Problem in checking ambiguities");
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Arrests", method = RequestMethod.GET)
	public synchronized LinkedList<String> getArrested(String name) throws Exception {
		// Parse user's input:
		String UpdatedName = updteName(name);
		try {
			logger.log(Level.INFO, "Analyzing Arrests query from url: "+"https://en.wikipedia.org/wiki/" + UpdatedName);
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("arrested");
			new ArrayList<>();
			analyze.setParagraphsArrests(wiki.getParagraphs());
			analyze.clearArrestsInformation();
			analyze.ArrestsQuery();
			return analyze.getArrestsInformation();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Problem in arrests query");
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Awards", method = RequestMethod.GET)
	public synchronized LinkedList<String> getAwards(String name) throws Exception {
		// Parse user's input:
		try {
			String UpdatedName = updteName(name);
			logger.log(Level.INFO, "Analyzing Awards query from url: "+"https://en.wikipedia.org/wiki/" + UpdatedName);
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("won");
			wiki.isConflictedName();
			wiki.getNames();
			new ArrayList<>();
			analyze.setParagraphsAwards(wiki.getParagraphs());
			analyze.clearAwardsInformation();
			analyze.AwardsQuery();
			return analyze.getAwardsInformation();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Problem in awards query");
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Dynamic", method = RequestMethod.GET)
	public synchronized LinkedList<String> getDynamic(String name, String query) throws Exception {
		// Parse user's input:
		try {
			String UpdatedName = updteName(name);
			logger.log(Level.INFO, "Analyzing Dynamic query from url: "+"https://en.wikipedia.org/wiki/" + UpdatedName);
			analyze.dynamicQuery(UpdatedName, query);
			return analyze.getDynamicInformation();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Problem in dynamic query");
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/PersonalInformation", method = RequestMethod.GET)
	public synchronized TableEntry getPersonal_Information(String name) throws Exception {
		// Parse user's input:

		String pageId = "", UpdatedName = updteName(name);
		try {
			logger.log(Level.INFO, "Finding personal Info for:" + UpdatedName);
			pageId = (Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=" + UpdatedName
					+ "&prop=pageimages&format=xml&pithumbsize=350").get() + "").split("pageid=\"")[1].split("\"")[0];
		} catch (Exception e) {
			logger.log(Level.WARNING, "Problem in personal info fetching");
			throw e;
		}
		return runner.getPersonalInfo(Integer.parseInt(pageId));
	}

	public static String updteName(String name) {
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
