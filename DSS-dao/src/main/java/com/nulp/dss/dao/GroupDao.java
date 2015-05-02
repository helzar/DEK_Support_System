package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Group;
import com.nulp.dss.model.Student;

public class GroupDao extends BaseDaoImpl<Group> {


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


}
