package com.nulp.dss.web.control;


import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.nulp.dss.dao.UserDao;
import com.nulp.dss.model.User;


@ManagedBean
@RequestScoped
public class AuthorizationBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	@Inject
	private UserDao userDao = new UserDao();
	private String login;
	private String pass;

	
	@PostConstruct
    public void init(){}
	
	
	public String getLogin() {
		return login;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public String getPass() {
		return pass;
	}


	public void setPass(String pass) {
		this.pass = pass;
	}
	

	private boolean validLoginAndPass(){

		if (login == null || login.isEmpty()){
			FacesMessage msg = new FacesMessage("Введіть логін.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}
		if (pass == null || pass.isEmpty()){
			FacesMessage msg = new FacesMessage("Введіть пароль.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}
		
		return true;
	}

	public void tryToLogin() {
		if (validLoginAndPass()){
			User user = userDao.getByLoginAndPass(login, pass);
			
			if (user == null){
				FacesMessage msg = new FacesMessage("Невірний логін або пароль.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			else{
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
				session.setAttribute("USER", user);

				try {
					facesContext.getExternalContext().redirect("index.xhtml");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
