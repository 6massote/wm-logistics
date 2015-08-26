package br.com.walmart.core.factories;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class Neo4jSessionFactory {

	static {
		System.setProperty("username", "neo4j");
		System.setProperty("password", "neo4j");
	}

	private final static SessionFactory sessionFactory = new SessionFactory("br.com.walmart.freight.entities.graph");
	private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

	public static Neo4jSessionFactory getInstance() {
		return factory;
	}

	private Neo4jSessionFactory() {
	}

	public Session getNeo4jSession() {
		return sessionFactory.openSession("http://localhost:7474");
	}

}
