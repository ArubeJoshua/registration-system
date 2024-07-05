package org.pahappa.systems.registrationapp.services.JSFValidators;

// Validator class
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("nameValidator")
public class NameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        String name = value.toString();
        if (!name.matches("[a-zA-Z]*")) {
            throw new ValidatorException(new FacesMessage("Name must be alphabetic"));
        }
    }
}
