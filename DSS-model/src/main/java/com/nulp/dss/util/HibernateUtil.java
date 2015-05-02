package com.nulp.dss.util;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
			return sessionFactory;
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
	public static void lazyInitialize(Object fatherObject, Object collectionObject){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(fatherObject);
		Hibernate.initialize(collectionObject);
		
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		session.evict(collectionObject);
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		session.getTransaction().commit();
	}
	
	public static void lazyInitialize(Object fatherObject, Object collectionObject, Object includeObject){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(includeObject);
		session.update(fatherObject);
		Hibernate.initialize(collectionObject);
		
		session.getTransaction().commit();
	}
	
	/**
	 * IMPORTANT - after refresh(obj) call lazyInitialize(father, father.getObjs()), to refresh lazy collection what conteins this refreshed object
	 * @param hibernateObject
	 */
	public static void refresh(Object hibernateObject){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(hibernateObject);
		session.refresh(hibernateObject);

		session.getTransaction().commit();
	}
	
	public static boolean isLazyInitialize(Object collection){
		return Hibernate.isInitialized(collection);
	}

}