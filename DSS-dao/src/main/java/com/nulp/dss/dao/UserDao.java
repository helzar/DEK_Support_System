package com.nulp.dss.dao;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.hibernate.Query;
import org.hibernate.Session;

import com.nulp.dss.model.User;

//@RequestScoped
public class UserDao extends BaseDaoImpl<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public User getById(Integer id) {
		Session session = this.getSession();
		User object = null;
		
		Query query = session.createQuery("FROM User as e WHERE e.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<User> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}
		
		closeSession(session);
		return object;
	}

	@Override
	public List<User> getAll() {
		Session session = this.getSession();

		Query query = session.createQuery("FROM User");
		@SuppressWarnings("unchecked")
		List<User> list = query.list();

		closeSession(session);
		return list;
	}
	
	public User getByLoginAndPass(String login, String password){
		Session session = this.getSession();
		User object = null;
		
		Query query = session.createQuery("FROM User as e WHERE e.login = :login AND e.password = :password");
		query.setParameter("login", login);
		query.setParameter("password", password);
		@SuppressWarnings("unchecked")
		List<User> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}
		
		closeSession(session);
		return object;
	}

}
