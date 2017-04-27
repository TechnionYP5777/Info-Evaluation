package infoeval.main.WikiData;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;

/**
 * 
 * @author Netanel
 * @since 05-04-2017
 * This class extract the data from DBpedia and stores it in mySql server
 *
 */
public class Extractor {

	private  ParameterizedSparqlString qs = new ParameterizedSparqlString ("PREFIX  rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
+" PREFIX  dbo: <http://dbpedia.org/ontology/>"
+" PREFIX  dbp: <http://dbpedia.org/property/>"

+"SELECT DISTINCT  ?name (SAMPLE (?deathPlace) as ?death)  (SAMPLE(?birthPlace) as ?birth) (SAMPLE(?deathDate) as ?dDate) (SAMPLE( ?birthDate) as ?bDate) "
+"WHERE {"
  +"  ?resource   a <http://dbpedia.org/ontology/Person>;    dbp:name ?name; dbp:birthPlace ?birthPlace;dbp:birthDate ?birthDate. OPTIONAL{?resource dbp:deathDate ?deathDate. ?resource dbp:deathPlace ?deathPlace} " 
   +" FILTER (lang(?name) = 'en')  }GROUP BY ?name "
+" ORDER BY DESC(?name)    LIMIT 10000 offset 10000 " );
	private ResultSetRewindable results;
	
		/*
		 * the query limits the number of results to 10,000, and the OFFSET means that we take
		 * every 10,000th entry
		 */
	
	
	public void executeQuery(){
		
		this.results = ResultSetFactory.copyResults(
				QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", qs.asQuery()).execSelect());
	}
	public ResultSetRewindable getResults(){
		return this.results;
		
	}
}
