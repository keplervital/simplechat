package com.huonix.simplechat.services;

import java.util.List;
import java.util.Optional;

/**
 * Interface for generic operations on a repository for a specific type.
 *
 * @author Kepler Vital
 */
public interface IService<T, ID> {
		
	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	List<T> findAll(); 
	
	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	Optional<T> getById(ID id);
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param model must not be {@literal null}.
	 * @return the entity added
	 * @throws Exception 
	 * @throws IllegalArgumentException if {@code model} is {@literal null}.
	 */
	T add(T model);
	
	/**
	 * Update a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param id must not be {@literal null}.
	 * @param model must not be {@literal null}.
	 * @return the entity updated
	 * @throws IllegalArgumentException if {@code model} is {@literal null}.
	 */
	T update(ID id, T model);
	
	/**
	 * Deletes a given entity.
	 *
	 * @param model must not be {@literal null}.
	 * @return {@literal true} if delete was successful, {@literal false} otherwise.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	boolean delete(T model);
	
	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	boolean existsById(ID id);
	
	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities
	 */
	long count();
	
	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	boolean deleteById(ID id);

}
