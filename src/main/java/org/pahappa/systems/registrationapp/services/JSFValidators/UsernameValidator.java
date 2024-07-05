package org.pahappa.systems.registrationapp.services.JSFValidators;// Validator class

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("usernameValidator")
public class UsernameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String username = value.toString();
        if (username.length() < 4) {
            throw new ValidatorException(new FacesMessage("Username must have at least 4 characters"));
        }
        if (!username.matches("[a-zA-Z0-9_]*")) {
            throw new ValidatorException(new FacesMessage("Username must only contain alphabets, numbers and underscores"));
        }
    }
}