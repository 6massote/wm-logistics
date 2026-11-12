package br.com.walmart.database.repository;

public interface Neo4jRepository {

	void resetNodesAndRelationships();

	void createIndexes();

}
