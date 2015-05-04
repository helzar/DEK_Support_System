package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Person;

public class PersonDao extends BaseDaoImpl<Person> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Person getById(Integer id) {
		Session session = this.getSession();
		Person object = null;
		
		Query query = session.createQuery("FROM Person as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Person> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Person> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Person");
		@SuppressWarnings("unchecked")
		List<Person> list = query.list();

		closeSession(session);
		return list;
	}

}
