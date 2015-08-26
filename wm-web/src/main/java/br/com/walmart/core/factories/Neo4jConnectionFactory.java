package br.com.walmart.core.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Neo4jConnectionFactory {
	
	public static Statement getStatement() {
    	Connection connection = null;
    	Statement stmt = null;
    	
    	Properties connectionProps = new Properties();
	    connectionProps.put("user", "neo4j");
	    connectionProps.put("password", "clapme@466");
    	
    	// Querying
    	try
    	{	
    		Class.forName("org.neo4j.jdbc.Driver");
    		
    		// Connect
    		connection = DriverManager.getConnection("jdbc:neo4j://localhost:7474/", connectionProps);
    		stmt = connection.createStatement();
    	    
    	} catch(Exception e) {
    		e.printStackTrace();
    		
    		if (connection != null) {
    			try {
					connection.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
    		}
    	}
    	
    	return stmt;
    }
    
}
