package WikiData;

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

	private  ParameterizedSparqlString qs = new ParameterizedSparqlString ( "prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>\n" + "\n" + "select * where {\n"
			+ " ?p a <http://dbpedia.org/ontology/Person> . " + "} LIMIT 10000 OFFSET 10000");
	private ResultSetRewindable results;
	
		/*
		 * the query limits the number of results to 100,000, and the OFFSET means that we take
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
