package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Review;

public class ReviewDao extends BaseDaoImpl<Review> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Review getById(Integer id) {
		Session session = this.getSession();
		Review object = null;
		
		Query query = session.createQuery("FROM Review as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Review> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Review> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Review");
		@SuppressWarnings("unchecked")
		List<Review> list = query.list();

		closeSession(session);
		return list;
	}

}
