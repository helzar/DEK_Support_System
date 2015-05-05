package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.ProtectionDay;

public class ProtectionDayDao extends BaseDaoImpl<ProtectionDay> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ProtectionDay getById(Integer id) {
		Session session = this.getSession();
		ProtectionDay object = null;
		
		Query query = session.createQuery("FROM ProtectionDay as p WHERE p.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<ProtectionDay> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<ProtectionDay> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM ProtectionDay");
		@SuppressWarnings("unchecked")
		List<ProtectionDay> list = query.list();

		closeSession(session);
		return list;
	}
	
	public List<ProtectionDay> getOrderedGraduationDays(Graduation graduation){
		Session session = this.getSession();

		session.update(graduation);
		Query query = session.createQuery(
				"SELECT D "
				+ "FROM ProtectionDay as D, Graduation as G "
				+ "WHERE G.id = :graduation_id "
				+ "AND D member of G.protectionDays "
				+ "ORDER BY D.day ");
		
		query.setParameter("graduation_id", graduation.getId());

		@SuppressWarnings("unchecked")
		List<ProtectionDay> list = query.list();
		
		closeSession(session);
		return list;
	}

}
