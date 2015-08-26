package br.com.walmart.core.repositories;

import org.neo4j.ogm.session.Session;

public interface Repository<T> {

	public abstract Iterable<T> findAll();

	public abstract T find(Long id);

    public abstract void delete(Long id);

    public abstract T createOrUpdate(T object);
    
    public abstract Session getSession();
    
}
