package br.com.walmart.core.repositories;

import org.neo4j.ogm.session.Session;

import br.com.walmart.core.entities.GraphEntity;
import br.com.walmart.core.factories.Neo4jSessionFactory;

public abstract class GenericRepository<T> implements Repository<T> {

	private static final int DEPTH_LIST = 0;
	private static final int DEPTH_ENTITY = 1;
	private Session session = Neo4jSessionFactory.getInstance().getNeo4jSession();

	public Iterable<T> findAll() {
		return session.loadAll(getEntityType(), DEPTH_LIST);
	}

	public T find(Long id) {
		return session.load(getEntityType(), id, DEPTH_ENTITY);
	}

	public void delete(Long id) {
		session.delete(session.load(getEntityType(), id));
	}

	public T createOrUpdate(T entity) {
		session.save(entity, DEPTH_ENTITY);
		return find(((GraphEntity) entity).getId());
	}
	
	public Session getSession() {
		return this.session;
	}
	
	public abstract Class<T> getEntityType();

}
