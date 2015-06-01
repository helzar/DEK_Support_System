package com.nulp.dss.web.conventer;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;

import com.nulp.dss.model.Diploma;
import com.nulp.dss.model.Student;

@FacesConverter ("myStudentsConverterForGrad")
public class StudentsConverterForGrad implements Converter {
	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
            	FacesContext facesContext = FacesContext.getCurrentInstance();
            	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

            	Map<String, Diploma> idAndStudMap = (Map<String, Diploma>)(session.getAttribute("freeDiplomas"));
            	Map<String, String> nameAndIdMap = (Map<String, String>)(session.getAttribute("freeDiplomaIds"));

            	return idAndStudMap.get(nameAndIdMap.get(value)).getStudent();
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
