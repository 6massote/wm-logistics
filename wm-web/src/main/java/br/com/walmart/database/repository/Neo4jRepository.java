package br.com.walmart.database.repository;

import java.sql.SQLException;

public interface Neo4jRepository {

	public abstract void resetNodesAndRelationships() throws SQLException;
	
}
