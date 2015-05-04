package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.model.Reviewer;

public class DiplomaDao extends BaseDaoImpl<Diploma> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	public List<Diploma> getDiplomasByReviewerAndGraduation(Reviewer reviewer, Graduation graduation){
		Session session = this.getSession();

		session.update(reviewer);
		session.update(graduation);
		Query query = session.createQuery(
				"SELECT D "
				+ "FROM Diploma as D, Graduation as G "
				+ "WHERE D.review.reviewer.id = :reviewer_id "
				+ "AND G.id = :graduation_id "
				+ "AND D.student member of G.groups.students ");
		
		query.setParameter("graduation_id", graduation.getId());
		query.setParameter("reviewer_id", reviewer.getId());
		
		@SuppressWarnings("unchecked")
		List<Diploma> list = query.list();
		
		closeSession(session);
		return list;
	}
	
}
