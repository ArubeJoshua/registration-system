package org.pahappa.systems.registrationapp.dao;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.Gender;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

import static org.pahappa.systems.registrationapp.config.SessionConfiguration.getSessionFactory;

public class DependantDAO extends AbstractDAO<Dependant> {
    public DependantDAO() {
        super(Dependant.class);
    }

    public void addDependantToUser(User user, Dependant dependant) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        // Persist the dependant entity
        session.save(dependant);

        // Update the user-dependant relationship
        user.getDependants().add(dependant);
        session.update(user);

        transaction.commit();
        if (session != null) {
            session.close();
        }
    }

    public List<Dependant> getDependants(User user, boolean isDeleted) {
        Session session = null;
        Transaction transaction = null;
        List<Dependant> dependants = null;
        try {
            session = getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Dependant.class);
            criteria.add(Restrictions.eq("user", user));
            criteria.add(Restrictions.eq("deleted", isDeleted));

            dependants = criteria.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return dependants;
    }


    public List<Dependant> getAllDependants(boolean isDeleted) {
        Session session = getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(Dependant.class);
            criteria.add(Restrictions.eq("deleted", isDeleted));

            return (List<Dependant>) criteria.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteAllEntities() {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Retrieve all users with role 'USER' and not deleted
            List<Dependant> dependants = session.createCriteria(Dependant.class)
                    .add(Restrictions.eq("deleted", false))
                    .list();

            for (Dependant dependant : dependants) {
                // Soft delete the user
                dependant.setDeleted(true);
                session.update(dependant);
            }

            transaction.commit();
        } catch (Exception e) {
            // Rollback the transaction in case of an exception
            transaction.rollback();
            throw e;
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }

    public void deleteDependant(Dependant dependant) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Retrieve the user with the specified username
            dependant = (Dependant) session.createCriteria(Dependant.class)
                    .add(Restrictions.eq("firstname", dependant.getFirstname()))
                    .uniqueResult();

            // If the user exists, soft delete the user and their dependents
            if (dependant != null) {
                dependant.setDeleted(true);
                session.update(dependant);
            }

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            // Rollback the transaction in case of an exception
            transaction.rollback();
            throw e;
        } finally {
            // Close the session
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(Dependant dependant) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(dependant);
        transaction.commit();
        session.close();
    }

    public Long countMaleDependantsNotDeleted() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction =  session.beginTransaction();// Declare a transaction object

        try {

            String hql = "SELECT COUNT(d) FROM Dependant d WHERE d.gender = :gender AND d.deleted = :deleted";
            Query query = session.createQuery(hql);
            query.setParameter("gender", Gender.MALE); // Assuming gender is stored as "MALE"
            query.setParameter("deleted", false);

            return (Long) query.uniqueResult();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Rollback transaction on error
            }
            throw e; // Re-throw the exception for proper handling
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}