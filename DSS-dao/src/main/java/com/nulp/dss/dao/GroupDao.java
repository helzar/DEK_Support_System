package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Group;
import com.nulp.dss.model.Student;
import com.nulp.dss.util.HibernateUtil;

public class GroupDao extends BaseDaoImpl<Group> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public Group getById(Integer id) {
		Session session = this.getSession();
		Group object = null;
		
		Query query = session.createQuery("FROM Group as e WHERE e.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Group> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Group> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Group");
		@SuppressWarnings("unchecked")
		List<Group> list = query.list();

		closeSession(session);
		return list;
	}
	

	@Override
	public void delete(Group object) {
		Session session = this.getSession();

		Query query = session.createQuery("DELETE FROM Diploma as e WHERE e.id = :id");
		for (Student st: object.getStudents()){
			query.setParameter("id", st.getId());
			query.executeUpdate();
		}
		
		query = session.createQuery("DELETE FROM Group as e WHERE e.id = :id");
		query.setParameter("id", object.getId());
		query.executeUpdate();
		
		closeSession(session);
	}
	
	public Group getByStudentId(int studentId){
		Session session = this.getSession();
		Group group = null;
		
		Query query = session.createQuery(
				"SELECT G "
				+ "FROM Group as G, Student as St "
				+ "WHERE St.id = :student_id "
				+ "AND St member of G.students ");
		query.setParameter("student_id", studentId);
		@SuppressWarnings("unchecked")
		List<Group> list = query.list();
		if (list.size() > 0){
			group = list.get(0);
		}

		closeSession(session);
		return group;
	}

	public long getGroupCount(int groupId){
		Session session = this.getSession();

		Query query = session.createQuery(
				"SELECT count(*) "
				+ "FROM Group as G, Student as St "
				+ "WHERE G.id = :group_id "
				+ "AND St member of G.students "
				);
		query.setParameter("group_id", groupId);
		Long count = (Long)query.uniqueResult();

		closeSession(session);
		return count.longValue();
	}

	public Long countAllReviewdGroupStudents(Integer groupId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Query query = session.createQuery(
				"SELECT count(*) "
				+ "FROM Group as G, Student as St "
				+ "WHERE G.id = :group_id "
				+ "AND St member of G.students "
				+ "AND St.diploma.review.reviewer is not null"
				);
		query.setParameter("group_id", groupId);
		Long count = (Long)query.uniqueResult();

		session.getTransaction().commit();
		
		return count;
	}

}
