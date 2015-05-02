package com.nulp.dss.dao;

import java.util.List;

public interface BaseDao<T> extends AutoCloseable{

	T getById(Integer id);
	List<T> getAll();
	void delete(T object);
	void insert(T object);
	void update(T object);
	
	@Deprecated
	void openSession();
}
