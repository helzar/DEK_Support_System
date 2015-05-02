package com.nulp.dss.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.nulp.dss.model.Commission;
import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Graduation;
import com.nulp.dss.model.Person;
import com.nulp.dss.model.PresentInProtectionDay;
import com.nulp.dss.model.ProtectionDay;
import com.nulp.dss.util.HibernateUtil;

public class GraduationDao extends BaseDaoImpl<Graduation> {
	
	@Override
	public Graduation getById(Integer id) {
		Session session = this.getSession();
		Graduation object = null;
		
		Query query = session.createQuery("FROM Graduation as g WHERE g.id = :id");
		query.setParameter("id", id);
		@SuppressWarnings("unchecked")
		List<Graduation> list = query.list();
		if (list.size() > 0){
			object = list.get(0);
		}

		closeSession(session);
		return object;
	}

	@Override
	public List<Graduation> getAll() {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Graduation");
		@SuppressWarnings("unchecked")
		List<Graduation> list = query.list();

		closeSession(session);
		return list;
	}
	
	public List<Graduation> getByYear(Integer year) {
		Session session = this.getSession();
		
		//@SuppressWarnings("unchecked")
		//List<Commission> list = session.createCriteria(Commission.class).list();
		Query query = session.createQuery("FROM Graduation g WHERE year(g.year) = :year");
		query.setParameter("year", year);
		@SuppressWarnings("unchecked")
		List<Graduation> list = query.list();

		closeSession(session);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getYearsList(){
		Session session = this.getSession();
		List<Integer> list;
		
		SQLQuery query = session.createSQLQuery("SELECT DISTINCT Year(g.year) AS 'year' FROM graduation g;");
		list = query.list();
		
		closeSession(session);
		return list; 
	}

	public void insertObj(Object object) {
		Session session = this.getSession();
		
		session.save(object);
		
		closeSession(session);
	}
	
	public void loadForProtectionDay(Graduation graduation, ProtectionDay protectionDay){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(graduation);
		session.update(protectionDay);
		Commission commission = graduation.getCommission();
		
		protectionDay.getPresentInProtectionDaySet().size();
		protectionDay.getDiplomas().size();
		
		for (Person person: commission.getPersons()){
			if (!conteins(protectionDay.getPresentInProtectionDaySet(), person)){
				loadPersonPresentInProtectionDay(person, session, protectionDay);
			}
		}
		if (commission.getHead() != null && !conteins(protectionDay.getPresentInProtectionDaySet(), commission.getHead())){
			loadPersonPresentInProtectionDay(commission.getHead(), session, protectionDay);
		}
		if (commission.getSecretary() != null && !conteins(protectionDay.getPresentInProtectionDaySet(), commission.getSecretary())){
			loadPersonPresentInProtectionDay(commission.getSecretary(), session, protectionDay);
		}
			
		for (PresentInProtectionDay presentInProtectionDay: protectionDay.getPresentInProtectionDaySet()){
			presentInProtectionDay.getQuestions().size();
		}
		
		session.getTransaction().commit();
	}
	
	private boolean conteins(Collection<PresentInProtectionDay> persons, Person person){
		for (PresentInProtectionDay localInProtectionDay: persons){
			if (localInProtectionDay.getPerson().getId().equals(person.getId())){
				return true;
			}
		}
		return false;
	}
	
	private void loadPersonPresentInProtectionDay(Person person, Session session, ProtectionDay protectionDay){
		PresentInProtectionDay newPresentInProtectionDay = new PresentInProtectionDay();
		newPresentInProtectionDay.setPerson(person);
		newPresentInProtectionDay.setProtectionDay(protectionDay);
		newPresentInProtectionDay.setIsPresent(false);
		session.save(newPresentInProtectionDay);
		protectionDay.getPresentInProtectionDaySet().add(newPresentInProtectionDay);
	}
	
	public void addDiplomaToProtectionDay(Diploma diploma, ProtectionDay protectionDay){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(diploma);
		session.update(protectionDay);
		
		diploma.setProtectionDay(protectionDay);
		protectionDay.getDiplomas().add(diploma);
		
		session.getTransaction().commit();
	}
	
	public void deleteDiplomaFromProtectionDay(Diploma diploma, ProtectionDay protectionDay){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(diploma);
		session.update(protectionDay);
		
		diploma.setProtectionDay(null);
		protectionDay.getDiplomas().remove(diploma);
		
		session.getTransaction().commit();
	}
	
	public void loadGraduationAndCommissionAndProtectionDays(Graduation graduation){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		session.update(graduation);
		graduation.getCommission().getPersons().size();
		graduation.getProtectionDays().size();
		
		session.getTransaction().commit();
	}
	
}
