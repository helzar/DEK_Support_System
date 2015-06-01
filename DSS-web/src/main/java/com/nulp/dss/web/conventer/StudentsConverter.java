package com.nulp.dss.web.conventer;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

import com.nulp.dss.model.Student;

@FacesConverter ("myStudentsConverter")
public class StudentsConverter implements Converter {

	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	FacesContext facesContext = FacesContext.getCurrentInstance();
            	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
          
            	Map<String, Student> idAndStudMap = (Map<String, Student>)(session.getAttribute("freeStudentsForNewReviews"));
            	Map<String, String> nameAndIdMap = (Map<String, String>)(session.getAttribute("freeStudentsForNewReviewsWithId"));

            	return idAndStudMap.get(nameAndIdMap.get(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null){
            return ((Student)object).displayFullName();
        }
        else {
            return null;
        }
    } 
	
}
