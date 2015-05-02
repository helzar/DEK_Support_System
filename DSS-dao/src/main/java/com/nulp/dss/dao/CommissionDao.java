package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Commission;

public class CommissionDao extends BaseDaoImpl<Commission> {

	@Override
	public Commission getById(Integer id) {
		Session session = this.getSession();
		Commission object = null;
		
//		Query query = session.createQuery("SELECT c FROM Commission c left join fetch c.persons WHERE c.id = :id");
		Query query = session.createQuery("SELECT c FROM Commission c WHERE c.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Commission> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Commission> getAll() {
		Session session = this.getSession();

		Query query = session.createQuery("FROM Commission");
		@SuppressWarnings("unchecked")
		List<Commission> list = query.list();

		closeSession(session);
		return list;
	}


}
