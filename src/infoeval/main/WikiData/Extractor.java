package infoeval.main.WikiData;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecutionFactory;
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
	private ResultSetRewindable results;
	private Map<QueryTypes, ParameterizedSparqlString> queriesMap;
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
				+ " (SAMPLE( ?birthDate) as ?bDate)(SAMPLE( ?birthCity) as ?bCity)(SAMPLE( ?deathCity) as ?dCity) " + " WHERE {"
				+ " ?resource   a <http://dbpedia.org/ontology/Person>;  "
				+ " dbp:name ?pname; dbp:birthPlace ?birthPlace; "
				+ "dbp:birthDate ?birthDate. ?birthPlace a dbo:Country."
				+ " OPTIONAL{?resource dbo:occupation ?occupation}." + " OPTIONAL{?resource dbo:thumbnail ?photoLink}."
				+ " OPTIONAL{?resource dbo:spouse ?spouse. ?spouse dbp:name ?spName."
				+ " OPTIONAL{?spouse dbo:occupation ?spOccupation}.}. "
				+" OPTIONAL{?resource dbp:birthPlace ?birthCity. ?birthCity a dbo:City}."
				+" OPTIONAL{?resource dbp:deathPlace ?deathCity. ?deathCity a dbo:City}."
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
						+ " SELECT (SAMPLE (?name) AS ?pname) (SAMPLE (?photoLink) AS ?photo) (SAMPLE (?occupation) AS ?occup)"
						+ " (SAMPLE (?spouse) AS ?spouses) (SAMPLE (?spName) AS ?sname)"
						+ " (SAMPLE (?spOccupation) AS ?spOccu) (SAMPLE (?deathPlace) AS ?death)"
						+ " (SAMPLE(?birthPlace) AS ?birth) (SAMPLE(?deathDate) AS ?dDate) "
						+ " (SAMPLE( ?birthDate) AS ?bDate)(SAMPLE( ?birthCity) as ?bCity)(SAMPLE( ?deathCity) as ?dCity)   {   VALUES ?wikiPageID { " + wikiPageID
						+ "} ?res a dbo:Person.  ?res dbo:wikiPageID ?wikiPageID.  ?res rdfs:label ?name."
						+ " FILTER (lang(?name) = 'en') OPTIONAL{?res dbo:birthPlace ?birthPlace.?birthPlace a dbo:Country. }. "
						+ " OPTIONAL{?res dbo:birthDate ?birthDate}. OPTIONAL{?res dbo:occupation ?occupation}."
						+ " OPTIONAL{?res dbo:thumbnail ?photoLink}."
						+" OPTIONAL{?res dbo:birthPlace ?birthCity. ?birthCity a dbo:City}."
						+" OPTIONAL{?res dbo:deathPlace ?deathCity. ?deathCity a dbo:City}."
						+ " OPTIONAL{?res dbo:spouse ?spouse. ?spouse rdfs:label ?spName."
						+ " ?spouse dbo:occupation ?spOccupation}. "
						+ " OPTIONAL{?res dbo:deathDate ?deathDate.}OPTIONAL{ ?res dbo:deathPlace ?deathPlace. ?deathPlace a dbo:Country}. } ");
		abstractByWikiPageID = new ParameterizedSparqlString(
				" PREFIX  dbo:  <http://dbpedia.org/ontology/> SELECT ?abstract { VALUES ?wikiPageID { " + wikiPageID
						+ " } ?res a dbo:Person. ?res dbo:wikiPageID ?wikiPageID. ?res dbo:abstract ?abstract."
						+ " FILTER (lang(?abstract) = 'en') }");

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID, basicInfoByWikiPageID);
		queriesMap.put(QueryTypes.ABSTRACT_BY_WIKI_PAGE_ID, abstractByWikiPageID);
	}

	public void executeQuery(QueryTypes ¢) {
		results = ResultSetFactory.copyResults(QueryExecutionFactory
				.sparqlService("http://dbpedia.org/sparql", queriesMap.get(¢).asQuery()).execSelect());
	}

	public ResultSetRewindable getResults() {
		return results;
	}
}
