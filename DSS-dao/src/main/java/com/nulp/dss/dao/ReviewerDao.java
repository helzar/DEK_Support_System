package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Group;
import com.nulp.dss.model.Reviewer;

public class ReviewerDao extends BaseDaoImpl<Reviewer> {

	@Override
	public Reviewer getById(Integer id) {
		Session session = this.getSession();
		Reviewer object = null;
		
		Query query = session.createQuery("FROM Reviewer as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Reviewer> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Reviewer> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Reviewer");
		@SuppressWarnings("unchecked")
		List<Reviewer> list = query.list();

		closeSession(session);
		return list;
	}
	
	/**
	 * WARN! It will update group!!!
	 * @param group
	 * @return
	 */
	public List<Reviewer> getAllWithGroup(Group group) {
		Session session = this.getSession();

		session.update(group);
		
		Query query = session.createQuery("FROM Reviewer");
		@SuppressWarnings("unchecked")
		List<Reviewer> list = query.list();

		closeSession(session);
		return list;
	}

}
