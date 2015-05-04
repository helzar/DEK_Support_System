package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.DiplomaInfo;

public class DiplomaInfoDao extends BaseDaoImpl<DiplomaInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public DiplomaInfo getById(Integer id) {
		Session session = this.getSession();
		DiplomaInfo object = null;
		
		Query query = session.createQuery("FROM DiplomaInfo as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<DiplomaInfo> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<DiplomaInfo> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM DiplomaInfo");
		@SuppressWarnings("unchecked")
		List<DiplomaInfo> list = query.list();

		closeSession(session);
		return list;
	}

}
