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

		return runner.getBornInPlaceBeforeYear(place, year);
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/Query1", method = RequestMethod.GET)
	public synchronized ArrayList<TableEntry> differentDeathPlace() throws Exception {
		return runner.getDifferentDeathPlace();

	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/SameOccupationCouples", method = RequestMethod.GET)
	public synchronized ArrayList<TableEntry> getSameOccupationCouples() throws Exception {
		// Parse user's input:

		return runner.getSameOccupationCouples();

	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/checkAmbiguities", method = RequestMethod.GET)
	public synchronized ArrayList<String> checkAmbiguities(String name) throws IOException {
		String UpdatedName = updteName(name);
		try {
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			// System.out.print("Trying to fetch from ,
			// https://en.wikipedia.org/wiki/" + UpdatedName);
			wiki.CheckAmbiguities();
			return !wiki.isConflictedName() ? null : wiki.getNames();
		} catch (Exception e) {
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
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("arrested");
			new ArrayList<>();
			analyze.setParagraphsArrests(wiki.getParagraphs());
			analyze.clearArrestsInformation();
			analyze.ArrestsQuery();
			return analyze.RefineResults(10, analyze.getArrestsInformation());
		} catch (Exception e) {
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
			WikiParsing wiki = (new WikiParsing("https://en.wikipedia.org/wiki/" + UpdatedName));
			wiki.Parse("won");
			wiki.isConflictedName();
			wiki.getNames();
			new ArrayList<>();
			// System.out.println(wiki.getParagraphs().text());
			analyze.setParagraphsAwards(wiki.getParagraphs());
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
	public synchronized LinkedList<String> getDynamic(String name, String query) throws Exception {
		// Parse user's input:
		try {
			analyze.dynamicQuery(updteName(name), query);
			return analyze.getDynamicInformation();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@CrossOrigin
	@RequestMapping(path = "Queries/PersonalInformation", method = RequestMethod.GET)
	public synchronized TableEntry getPersonal_Information(String name) throws Exception {
		// Parse user's input:

		String pageId = "", UpdatedName = updteName(name);
		// System.out.println(UpdatedName);

		try {
			pageId = (Jsoup.connect("https://en.wikipedia.org/w/api.php?action=query&titles=" + UpdatedName
					+ "&prop=pageimages&format=xml&pithumbsize=350").get() + "").split("pageid=\"")[1].split("\"")[0];
		} catch (Exception e) {
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
