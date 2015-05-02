package com.nulp.dss.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.Student;

public class StudentDao extends BaseDaoImpl<Student> {

	@Override
	public Student getById(Integer id) {
		Session session = this.getSession();
		Student object = null;
		
		Query query = session.createQuery("FROM Student as e WHERE e.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Student> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}
		
		closeSession(session);
		return object;
	}

	@Override
	public List<Student> getAll() {
		Session session = this.getSession();

		Query query = session.createQuery("FROM Student");
		@SuppressWarnings("unchecked")
		List<Student> list = query.list();

		closeSession(session);
		return list;
	}

	@Override
	public void delete(Student object) {
		Session session = this.getSession();

		Query query = session.createQuery("DELETE FROM Student as e WHERE e.id = :id");
		query.setParameter("id", object.getId());
		query.executeUpdate();
		
		query = session.createQuery("DELETE FROM Diploma as e WHERE e.id = :id");
		query.setParameter("id", object.getId());
		query.executeUpdate();
		
		closeSession(session);
	}

}
