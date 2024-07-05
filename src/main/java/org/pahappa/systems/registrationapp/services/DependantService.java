package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.dao.UserDAO;
import org.pahappa.systems.registrationapp.models.Dependant;

import java.util.List;

public class DependantService {
    private final DependantDAO dependentDAO;
    private UserDAO userDAO;

    public DependantService() {
        dependentDAO = new DependantDAO();
    }

   public List<Dependant> retrieveDependantsList() {
       return dependentDAO.getAllDependants( false);
    }
}
