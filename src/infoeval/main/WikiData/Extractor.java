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
 * @since 05-04-2017 This class extract the data from DBpedia and stores it in
 *        mySql server
 *
 */
public class Extractor {

	private ParameterizedSparqlString basicInfoQuery;
	private ParameterizedSparqlString wikiIdQuery;
	private ResultSetRewindable results;
	private Map<QueryTypes, ParameterizedSparqlString> queriesMap;
	private static final int ENTRIES_NUM = 10000;
	private static final int SKIP_NUM = 10000;	
	
	public Extractor() {
		basicInfoQuery = new ParameterizedSparqlString("PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ " PREFIX  dbo: <http://dbpedia.org/ontology/> PREFIX  dbp: <http://dbpedia.org/property/>"
				+ " SELECT DISTINCT  ?name (SAMPLE (?occupation) as ?occup)(SAMPLE (?spouse) as ?spouses)(SAMPLE (?spName) as ?sname)(SAMPLE (?spOccupation) as ?spOccu)(SAMPLE (?deathPlace) as ?death)  (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) (SAMPLE( ?birthDate) as ?bDate) "
				+ " WHERE {"
				+ "  ?resource   a <http://dbpedia.org/ontology/Person>;    dbp:name ?name; dbp:birthPlace ?birthPlace;dbp:birthDate ?birthDate.OPTIONAL{?resource dbo:occupation ?occupation}.OPTIONAL{?resource dbo:spouse ?spouse. ?spouse dbp:name ?spName. ?spouse dbo:occupation ?spOccupation}. OPTIONAL{?resource dbp:deathDate ?deathDate. ?resource dbp:deathPlace ?deathPlace} "
				+ " FILTER (lang(?name) = 'en')  }GROUP BY ?name "
				+ " ORDER BY DESC(?name)    LIMIT "+ENTRIES_NUM+" OFFSET "+ENTRIES_NUM);

		wikiIdQuery = new ParameterizedSparqlString(
				"PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX  dbo: <http://dbpedia.org/ontology/>"
						+ " PREFIX  dbp: <http://dbpedia.org/property/> SELECT DISTINCT ?name ?wikiPageID WHERE { "
						+ " ?resource a <http://dbpedia.org/ontology/Person>; dbp:name ?name; dbo:wikiPageID ?wikiPageID."
						+ " FILTER (lang(?name) = 'en')} ORDER BY DESC(?name) LIMIT "+ENTRIES_NUM+" OFFSET "+SKIP_NUM);

		queriesMap = new HashMap<QueryTypes, ParameterizedSparqlString>();
		queriesMap.put(QueryTypes.BASIC_INFO, basicInfoQuery);
		queriesMap.put(QueryTypes.WIKI_ID, wikiIdQuery);
	}
	/*
	 * the query limits the number of results to 10,000, and the OFFSET means
	 * that we take every 10,000th entry
	 */

	public void executeQuery(QueryTypes t) {
		this.results = ResultSetFactory.copyResults(QueryExecutionFactory
				.sparqlService("http://dbpedia.org/sparql", queriesMap.get(t).asQuery()).execSelect());
	}

	public ResultSetRewindable getResults() {
		return this.results;
	}
}
