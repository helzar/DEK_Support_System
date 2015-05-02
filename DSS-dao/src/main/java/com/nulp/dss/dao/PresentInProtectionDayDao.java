package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.PresentInProtectionDay;

public class PresentInProtectionDayDao extends BaseDaoImpl<PresentInProtectionDay> {
	
	@Override
	public PresentInProtectionDay getById(Integer id) {
		Session session = this.getSession();
		PresentInProtectionDay object = null;
		
		Query query = session.createQuery("FROM PresentInProtectionDay as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<PresentInProtectionDay> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<PresentInProtectionDay> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM PresentInProtectionDay");
		@SuppressWarnings("unchecked")
		List<PresentInProtectionDay> list = query.list();

		closeSession(session);
		return list;
	}
	
}
