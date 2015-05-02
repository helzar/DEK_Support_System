package com.nulp.dss.control;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;

import com.nulp.dss.dao.ReviewerDao;
import com.nulp.dss.model.Reviewer;

@ManagedBean
@ViewScoped
public class ReviewerEditBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReviewerDao reviewerDao = new ReviewerDao();
	private List<Reviewer> reviewers;
	private Reviewer newReviewer;
	
	@PostConstruct
	public void init() {
		reviewers = reviewerDao.getAll();
		newReviewer = new Reviewer();
	}

	public List<Reviewer> getReviewers() {
		return reviewers;
	}

	public void setReviewers(List<Reviewer> reviewers) {
		this.reviewers = reviewers;
	}

	public Reviewer getNewReviewer() {
		return newReviewer;
	}

	public void setNewReviewer(Reviewer newReviewer) {
		this.newReviewer = newReviewer;
	}
	
	public void addReviewer(){

		reviewerDao.insert(newReviewer);
		reviewers.add(newReviewer);
		newReviewer = new Reviewer();
		
		FacesMessage msg = new FacesMessage("Рецензента додано.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void deleteReviewer(Reviewer reviewerToDelete) {
		
		reviewers.remove(reviewerToDelete);
		reviewerDao.delete(reviewerToDelete);
		
		FacesMessage msg = new FacesMessage("Рецензента видалено.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void onReviewerCellEdit(CellEditEvent event) {
		
		updateAllPersons();
		
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Значення змінено. З " + oldValue + " у " + newValue, "Змінено");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
	
	private void updateAllPersons(){
		for (Reviewer r: reviewers){
			reviewerDao.update(r);
		}
	}
}
