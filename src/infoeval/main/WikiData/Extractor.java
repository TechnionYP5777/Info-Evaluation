package infoeval.main.WikiData;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import java.util.Map;
import java.util.HashMap;
import infoeval.main.WikiData.QueryTypes;

/**
 * 
 * @author Netanel
 * @author osherh
 * @since 05-04-2017 This class extract the data from DBpedia and return results
 *        to be stored in mySql server
 *
 */
public class Extractor {

	private ParameterizedSparqlString basicInfoQuery;
	private ParameterizedSparqlString wikiIdQuery;
	private ParameterizedSparqlString basicInfoByWikiPageID;
	private ParameterizedSparqlString abstractByWikiPageID;
	private String checkIfPerson;
	private ResultSetRewindable results;
	private Map<QueryTypes, ParameterizedSparqlString> queriesMap;
	private Map<QueryTypes, String> queriesAskMap;
	private static final int ENTRIES_NUM = 10000;
	private static final int SKIP_NUM = 10000;

	/**
	 * [[SuppressWarningsSpartan]]
	 */
	public Extractor() {
		/*
		 * the query limits the number of results to 10,000, and the OFFSET
		 * means that we take every 10,000th entry
		 */
		basicInfoQuery = new ParameterizedSparqlString(" PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX  dbo: <http://dbpedia.org/ontology/>" + " PREFIX  dbp: <http://dbpedia.org/property/>"
				+ " SELECT DISTINCT " + " ?pname (SAMPLE (?photoLink) as ?photo) (SAMPLE (?occupation) as ?occup)"
				+ "  (SAMPLE (?spName) as ?sname)"
				+ " (SAMPLE (?spOccupation) as ?spOccu) (SAMPLE (?deathPlace) as ?death)"
				+ " (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) "
				+ " (SAMPLE( ?birthDate) as ?bDate)(SAMPLE( ?birthExpanded) as ?bExp)(SAMPLE( ?deathExpanded) as ?dExp) "
				+ " WHERE {" + " ?resource   a <http://dbpedia.org/ontology/Person>;  "
				+ " dbp:name ?pname; dbp:birthPlace ?birthPlace; "
				+ "dbp:birthDate ?birthDate. ?birthPlace a dbo:Country."
				+ " OPTIONAL{?resource dbo:occupation ?occupation}." + " OPTIONAL{?resource dbo:thumbnail ?photoLink}."
				+ " OPTIONAL{?resource dbo:spouse ?spouse. ?spouse dbp:name ?spName."
				+ " OPTIONAL{?spouse dbo:occupation ?spOccupation}.}. "
				+ " OPTIONAL{?resource dbp:birthPlace ?birthExpanded. }."
				+ " OPTIONAL{?resource dbp:deathPlace ?deathExpanded.}."
				+ " OPTIONAL{?resource dbp:deathDate ?deathDate. ?resource dbp:deathPlace ?deathPlace.?deathPlace a dbo:Country.} "
				+ " FILTER (lang(?pname) = 'en')  }GROUP BY ?pname " + " ORDER BY DESC(?pname)    LIMIT " + ENTRIES_NUM
				+ " OFFSET " + SKIP_NUM);

		wikiIdQuery = new ParameterizedSparqlString("PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX  dbo: <http://dbpedia.org/ontology/>" + "PREFIX  dbp: <http://dbpedia.org/property/> "
				+ "SELECT DISTINCT ?name ?wikiPageID " + "WHERE { "
				+ " ?resource a <http://dbpedia.org/ontology/Person>; dbp:name ?name;" + "  dbo:wikiPageID ?wikiPageID."
				+ " FILTER (lang(?name) = 'en')} ORDER BY DESC(?name) LIMIT " + ENTRIES_NUM + " OFFSET " + SKIP_NUM);

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.BASIC_INFO, basicInfoQuery);
		queriesMap.put(QueryTypes.WIKI_ID, wikiIdQuery);
	}

	public Extractor(int wikiPageID) {
		basicInfoByWikiPageID = new ParameterizedSparqlString(
				" PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ " PREFIX  dbo:  <http://dbpedia.org/ontology/>"
						+ " PREFIX  dbp:  <http://dbpedia.org/property/>"
						+ " PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ " SELECT DISTINCT ?pname (SAMPLE (?photoLink) AS ?photo) (SAMPLE (?occupation) AS ?occup)"
						+ " (SAMPLE (?occupationTitle) AS ?occupTitle) (SAMPLE (?spouse) AS ?spouses) (SAMPLE (?spName) AS ?sname)"
						+ " (SAMPLE (?spOccupation) AS ?spOccu) (SAMPLE (?deathPlace) AS ?death)"
						+ " (SAMPLE(?birthPlace) AS ?birth) (SAMPLE(?deathDate) AS ?dDate) "
						+ " (SAMPLE( ?birthDate) AS ?bDate)  (SAMPLE( ?birthExpanded) as ?bExp)(SAMPLE( ?deathExpanded) as ?dExp) "
						+ " {   VALUES ?wikiPageID { " + wikiPageID + "}" + " ?res a dbo:Person.  "
						+ " ?res dbo:wikiPageID ?wikiPageID." + " ?res rdfs:label ?pname."
						+ " FILTER (lang(?pname) = 'en') "
						+ " OPTIONAL{?res dbp:birthPlace ?birthPlace. ?birthPlace a dbo:Country}."
						+ " OPTIONAL{?res dbo:birthDate ?birthDate}." + " OPTIONAL{?res dbo:occupation ?occupation}."
						+ "  OPTIONAL{?res dbo:occupation ?occupation. ?occupation a dbo:PersonFunction . ?occupation dbo:title ?occupationTitle}."

						+ " OPTIONAL{?res dbo:thumbnail ?photoLink}."
						+ " OPTIONAL{?res dbp:birthPlace ?birthExpanded. }."
						+ " OPTIONAL{?res dbp:deathPlace ?deathExpanded. }."
						+ " OPTIONAL{?res dbo:spouse ?spouse. ?spouse rdfs:label ?spName. ?spouse dbo:occupation ?spOccupation. FILTER (lang(?spName) = 'en')}"

						+ " OPTIONAL{?res dbo:deathDate ?deathDate.}OPTIONAL{ ?res dbo:deathPlace ?deathPlace. ?deathPlace a dbo:Country}.} GROUP BY ?pname ");

		abstractByWikiPageID = new ParameterizedSparqlString(
				" PREFIX  dbo:  <http://dbpedia.org/ontology/> SELECT ?abstract { VALUES ?wikiPageID { " + wikiPageID
						+ " } ?res a dbo:Person. ?res dbo:wikiPageID ?wikiPageID. ?res dbo:abstract ?abstract."
						+ " FILTER (lang(?abstract) = 'en') }");

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID, basicInfoByWikiPageID);
		queriesMap.put(QueryTypes.ABSTRACT_BY_WIKI_PAGE_ID, abstractByWikiPageID);
	}

	public Extractor(String name) {
		checkIfPerson = " " + " PREFIX  dbo: <http://dbpedia.org/ontology/>"
				+ " PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" + " ASK { <http://dbpedia.org/resource/"
				+ name + "> rdf:type dbo:Person}";
		queriesAskMap = new HashMap<QueryTypes, String>();
		queriesAskMap.put(QueryTypes.CHECK_IF_PERSON, checkIfPerson);
	}

	public void executeSelectQuery(QueryTypes ¢) {
		results = ResultSetFactory.copyResults(QueryExecutionFactory
				.sparqlService("http://dbpedia.org/sparql", queriesMap.get(¢).asQuery()).execSelect());
	}

	public ResultSetRewindable getResults() {
		return results;
	}

	public boolean executeAskQuery(QueryTypes ¢) {
		Query query = QueryFactory.create(queriesAskMap.get(¢));
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		boolean $ = qexec.execAsk();
		qexec.close();
		return $;
	}
}
