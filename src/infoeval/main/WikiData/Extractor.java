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
	private ParameterizedSparqlString abstractQuery;
	private ParameterizedSparqlString basicInfoByNameQuery;
	private ParameterizedSparqlString basicInfoByBracketsNameQuery;
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
				+ " SELECT DISTINCT " + " ?name (SAMPLE (?photoLink) as ?photo) (SAMPLE (?occupation) as ?occup)"
				+ " (SAMPLE (?spouse) as ?spouses) (SAMPLE (?spName) as ?sname)"
				+ " (SAMPLE (?spOccupation) as ?spOccu) (SAMPLE (?deathPlace) as ?death)"
				+ " (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) "
				+ " (SAMPLE( ?birthDate) as ?bDate) " + " WHERE {"
				+ " ?resource   a <http://dbpedia.org/ontology/Person>;  "
				+ " dbp:name ?name; dbp:birthPlace ?birthPlace;dbp:birthDate ?birthDate."
				+ " OPTIONAL{?resource dbo:occupation ?occupation}." + " OPTIONAL{?resource dbo:thumbnail ?photoLink}."
				+ " OPTIONAL{?resource dbo:spouse ?spouse. ?spouse dbp:name ?spName."
				+ " ?spouse dbo:occupation ?spOccupation}. "
				+ " OPTIONAL{?resource dbp:deathDate ?deathDate. ?resource dbp:deathPlace ?deathPlace} "
				+ " FILTER (lang(?name) = 'en')  }GROUP BY ?name " + " ORDER BY DESC(?name)    LIMIT " + ENTRIES_NUM
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

	public Extractor(String name) {
		abstractQuery = new ParameterizedSparqlString(" PREFIX dbo: <http://dbpedia.org/ontology/>"

				+ " SELECT ?abstract WHERE { <http://dbpedia.org/resource/" + name + "> dbo:abstract ?abstract."
				+ "FILTER (lang(?abstract) = 'en')}");

		basicInfoByNameQuery = new ParameterizedSparqlString(
				" PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ " PREFIX  dbo: <http://dbpedia.org/ontology/>"
						+ " PREFIX  dbp: <http://dbpedia.org/property/>"
						+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX bif:<bif:>"
						+ " SELECT (SAMPLE (?photoLink) as ?photo) (SAMPLE (?occupation) as ?occup)"
						+ " (SAMPLE (?spouse) as ?spouses) (SAMPLE (?spName) as ?sname)"
						+ " (SAMPLE (?spOccupation) as ?spOccu) (SAMPLE (?deathPlace) as ?death)"
						+ " (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) "
						+ " (SAMPLE( ?birthDate) as ?bDate)  WHERE { ?resource a dbo:Person."
						+ " ?resource rdfs:label ?name. FILTER(bif:contains(?name,?nameVal))"
						+ " ?resource dbp:birthPlace ?birthPlace. ?resource dbp:birthDate ?birthDate."
						+ " OPTIONAL{?resource dbo:occupation ?occupation}."
						+ " OPTIONAL{?resource dbo:thumbnail ?photoLink}."
						+ " OPTIONAL{?resource dbo:spouse ?spouse. ?spouse dbp:name ?spName."
						+ " ?spouse dbo:occupation ?spOccupation}. "
						+ " OPTIONAL{?resource dbp:deathDate ?deathDate. ?resource dbp:deathPlace ?deathPlace} "
						+ " }");

		basicInfoByBracketsNameQuery = new ParameterizedSparqlString(
				" PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ " PREFIX  dbo: <http://dbpedia.org/ontology/>"
						+ " PREFIX  dbp: <http://dbpedia.org/property/>"
						+ " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ " SELECT ?uri (SAMPLE (?photoLink) as ?photo) (SAMPLE (?occupation) as ?occup)"
						+ " (SAMPLE (?spouse) as ?spouses) (SAMPLE (?spName) as ?sname)"
						+ " (SAMPLE (?spOccupation) as ?spOccu) (SAMPLE (?deathPlace) as ?death)"
						+ " (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) "
						+ " (SAMPLE( ?birthDate) as ?bDate)  { VALUES ?uri { <http://dbpedia.org/resource/" + name
						+ "> } ?uri dbp:birthPlace ?birthPlace. ?uri dbp:birthDate ?birthDate."
						+ " OPTIONAL{?uri dbo:occupation ?occupation}. OPTIONAL{?uri dbo:thumbnail ?photoLink}."
						+ " OPTIONAL{?uri dbo:spouse ?spouse. ?spouse dbp:name ?spName."
						+ " ?spouse dbo:occupation ?spOccupation}. "
						+ " OPTIONAL{?uri dbp:deathDate ?deathDate. ?uri dbp:deathPlace ?deathPlace}  } "
						+ "GROUP BY ?uri");

		// abstractQuery.setIri("name","http://dbpedia.org/resorce/"+name);
		basicInfoByNameQuery.setLiteral("nameVal", name);

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.ABSTRACT, abstractQuery);
		queriesMap.put(QueryTypes.BASIC_INFO_BY_NAME, basicInfoByNameQuery);
		queriesMap.put(QueryTypes.BASIC_INFO_BRACKETS_NAME, basicInfoByBracketsNameQuery);
	}

	public Extractor(int wikiPageID) {
		basicInfoByWikiPageID = new ParameterizedSparqlString(
				" PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ " PREFIX  dbo:  <http://dbpedia.org/ontology/>"
						+ " PREFIX  dbp:  <http://dbpedia.org/property/>"
						+ " PREFIX  rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ " SELECT ?name (SAMPLE (?photoLink) as ?photo) (SAMPLE (?occupation) as ?occup)"
						+ " (SAMPLE (?spouse) as ?spouses) (SAMPLE (?spName) as ?sname)"
						+ " (SAMPLE (?spOccupation) as ?spOccu) (SAMPLE (?deathPlace) as ?death)"
						+ " (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) "
						+ " (SAMPLE( ?birthDate) as ?bDate)  {  VALUES ?wikiPageID { " + wikiPageID
						+ " } ?res a dbo:Person. " + " ?res dbp:name ?name."
						+ " ?res dbo:wikiPageID ?wikiPageID. ?res dbp:birthPlace ?birthPlace. "
						+ " ?res dbp:birthDate ?birthDate. OPTIONAL{?res dbo:occupation ?occupation}."
						+ " OPTIONAL{?res dbo:thumbnail ?photoLink}."
						+ " OPTIONAL{?res dbo:spouse ?spouse. ?spouse dbp:name ?spName."
						+ " ?spouse dbo:occupation ?spOccupation}. "
						+ " OPTIONAL{?res dbp:deathDate ?deathDate. ?res dbp:deathPlace ?deathPlace}. } "
						+ " GROUP BY (?name)");
		abstractByWikiPageID = new ParameterizedSparqlString(
				" PREFIX  dbo:  <http://dbpedia.org/ontology/> SELECT ?abstract { VALUES ?wikiPageID { " + wikiPageID
						+ " } ?res a dbo:Person. ?res dbo:wikiPageID ?wikiPageID. ?res dbo:abstract ?abstract."
						+ " FILTER (lang(?abstract) = 'en') }");

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.BASIC_INFO_BY_WIKI_PAGE_ID, basicInfoByWikiPageID);
		queriesMap.put(QueryTypes.ABSTRACT_BY_WIKI_PAGE_ID, abstractByWikiPageID);
	}

	@SuppressWarnings("resource")
	public void executeQuery(QueryTypes ¢) {
		// this.results = ResultSetFactory.copyResults(
		// (new QueryEngineHTTP("http://dbpedia.org/sparql",
		// queriesMap.get(¢).asQuery())).execSelect());

		this.results = ResultSetFactory.copyResults(QueryExecutionFactory
				.sparqlService("http://dbpedia.org/sparql", queriesMap.get(¢).asQuery()).execSelect());
	}

	public ResultSetRewindable getResults() {
		return this.results;
	}
}
