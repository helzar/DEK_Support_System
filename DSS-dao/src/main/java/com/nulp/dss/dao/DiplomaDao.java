package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.ProtectionDay;

public class DiplomaDao extends BaseDaoImpl<Diploma> {

	@Override
	public Diploma getById(Integer id) {
		Session session = this.getSession();
		Diploma object = null;
		
		Query query = session.createQuery("FROM Diploma as e WHERE e.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Diploma> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}
		
		closeSession(session);
		return object;
	}

	@Override
	public List<Diploma> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Diploma");
		@SuppressWarnings("unchecked")
		List<Diploma> list = query.list();

		closeSession(session);
		return list;
	}
	
	public void save(Diploma object){
		Session session = this.getSession();
		
		session.save(object);
		
		closeSession(session);
	}
	
	public List<Diploma> getAllFreeByGraduationId(Integer graduationId, ProtectionDay protectionDay){
		Session session = this.getSession();

		session.update(protectionDay);
		Query query = session.createQuery("SELECT d FROM Diploma as d WHERE d.protectionDay = null AND d.student.group.graduation.id = :id");
		query.setParameter("id", graduationId);
		@SuppressWarnings("unchecked")
		List<Diploma> list = query.list();

		closeSession(session);
		return list;
	}
	
}
