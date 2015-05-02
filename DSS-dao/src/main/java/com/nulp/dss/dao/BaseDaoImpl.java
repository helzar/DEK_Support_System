package com.nulp.dss.dao;

import org.hibernate.Session;

import com.nulp.dss.util.HibernateUtil;

public abstract class BaseDaoImpl<T> implements BaseDao<T> {

	
	private Session innerSession;

	@Override
	public void delete(T object) {
		Session session = this.getSession();
		session.delete(object);
		closeSession(session);
	}

	@Override
	public void insert(T object) {
		Session session = this.getSession();
		session.save(object);
		closeSession(session);
	}

	@Override
	public void update(T object) {
		Session session = this.getSession();
		session.update(object);
		closeSession(session);
	}
	
	@Override
	@Deprecated
	public void openSession() {
		this.close();
		innerSession = HibernateUtil.getSessionFactory().getCurrentSession();
		innerSession.beginTransaction();
	}

	@Override
	public void close(){
		if (innerSession != null && innerSession.isOpen()){
			innerSession.getTransaction().commit();
			innerSession = null;
		}
	}
	
	protected Session getSession(){
		if (innerSession != null && innerSession.isOpen()){
			return innerSession;
		}
		else{
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			return session;
		}
	}
	
	protected void closeSession(Session session){
		if (!session.equals(innerSession)){
			session.getTransaction().commit();
		}
	}

}
