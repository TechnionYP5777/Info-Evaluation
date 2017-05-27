package infoeval.main.mysql;

import infoeval.main.WikiData.Connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author osherh
 * @Since 25-05-2017 This class performs serialization of query results and
 *        deserialization to retrieve the obejct
 */

public class QueryResultsSerializer {
	private static final Logger logger = Logger.getLogger("QueryResultsSerializer".getClass().getName());
	private static final String SERIALIZE_RESULT = "INSERT INTO serialized_query_results(query_identifier, serialized_result) VALUES (?, ?)";
	private static final String DESERIALIZE_RESULT = "SELECT serialized_result FROM serialized_query_results WHERE serialized_id = ?";

	public long serializeQueryResults(Connector conn, String query_identifier, Object resultToSerialize)
			throws SQLException, ClassNotFoundException, IOException {

		Connection c = conn.getConnection();
		PreparedStatement pstmt = c.prepareStatement(SERIALIZE_RESULT);
		pstmt.setString(1, query_identifier);
		pstmt.setObject(2, resultToSerialize);
		pstmt.executeUpdate();

		ResultSet rs = pstmt.getGeneratedKeys();
		int $ = !rs.next() ? -1 : rs.getInt(1);
		rs.close();
		pstmt.close();
		c.close();
		
		logger.log(Level.INFO, "Query " + query_identifier + " has been serialized");
		return $;
	}

	public Object deSerializeQueryResults(Connector conn, long serialized_id)
			throws SQLException, IOException, ClassNotFoundException {

		Connection c = conn.getConnection();		
		PreparedStatement pstmt = c.prepareStatement(DESERIALIZE_RESULT);
		pstmt.setLong(1, serialized_id);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		byte[] buf = rs.getBytes(1);
		@SuppressWarnings("resource")
		ObjectInputStream objectIn = buf == null ? null : new ObjectInputStream(new ByteArrayInputStream(buf));
		Object $ = objectIn.readObject();
		rs.close();
		pstmt.close();
		c.close();

		logger.log(Level.INFO, "Query with Serialized_ID: " + serialized_id + " has been deserialized");
		return $;
	}
}