package org.pahappa.systems.registrationapp.dao;

import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import javax.persistence.NoResultException;

import org.hibernate.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.pahappa.systems.registrationapp.config.SessionConfiguration.getSessionFactory;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO() {
        super(User.class);
    }

    public String getAdminUsername() {
        Session session = getSessionFactory().openSession();
        String username = null;
        try {
            Query query = session.createQuery("SELECT u.username FROM User u WHERE u.role = :role");
            query.setParameter("role", "ADMIN");
            query.setMaxResults(1); // Limit to one result
            username = (String) query.uniqueResult();
        } catch (NoResultException e) {
            username = null;
        } finally {
            session.close();
        }
        return username;
    }

    public User getAdminUser() {
        Session session = null;
        User adminUser = null;
        try {
            session = getSessionFactory().openSession();
            Query query = session.createQuery("SELECT u FROM User u WHERE u.role = :role");
            query.setParameter("role", "ADMIN");
            query.setMaxResults(1); // Limit to one result
            adminUser = (User) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception appropriately in your application
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return adminUser;
    }

    public void deleteAllEntities() {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Retrieve all users with role 'USER' and not deleted
            List<User> users = session.createCriteria(User.class)
                    .add(Restrictions.eq("deleted", false))
                    .add(Restrictions.eq("role", "USER"))
                    .list();

            // Iterate through each user
            for (User user : users) {
                // Soft delete the user
                user.setDeleted(true);

                // Iterate through each dependent of the user and soft delete them
                for (Dependant dependant : user.getDependants()) {
                    dependant.setDeleted(true);
                    session.update(dependant);
                }
                // Update the user in the session
                session.update(user);
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


    public void deleteEntity(String username) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            // Retrieve the user with the specified username
            User user = (User) session.createCriteria(User.class)
                    .add(Restrictions.eq("username", username))
                    .uniqueResult();

            // If the user exists, soft delete the user and their dependents
            if (user != null) {
                user.setDeleted(true);
                for (Dependant dependant : user.getDependants()) {
                    dependant.setDeleted(true);
                    session.update(dependant);
                }
                session.update(user);
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

    public Set<User> getAllEntities(String role, boolean isDeleted) {
        Session session = getSessionFactory().openSession();
        try {
            Criteria criteria = session.createCriteria(User.class);  // Specify User class directly
            criteria.add(Restrictions.eq("role", role));
            criteria.add(Restrictions.eq("deleted", isDeleted));
            List<User> users = criteria.list();

            // Initialize dependants for each user
            for (User user : users) {
                try {
                    Hibernate.initialize(user.getDependants());
                } catch (HibernateException e) {
                    e.printStackTrace();
                }
            }
            return new HashSet<>(users);
        } finally {
            session.close();
        }
    }

    public User findById(Long userId) {
        Session session = getSessionFactory().openSession();
        try {
            User user = (User) session.get(User.class, userId);
            Hibernate.initialize(user.getDependants()); // Initialize the dependants collection
            return user;
        } finally {
            if (session != null) {
                session.close();

            }
        }
    }

    public Long countUsersWithDependants() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {

            String hql =
                    "SELECT COUNT(DISTINCT u) " +
                            "FROM User u " +
                            "JOIN u.dependants d " +
                            "WHERE u.role = :userRole " +
                            "AND u.deleted = :userDeleted " +
                            "AND d.deleted = :dependantDeleted";

            Query query = session.createQuery(hql);
            query.setParameter("userRole", "USER");
            query.setParameter("userDeleted", false);
            query.setParameter("dependantDeleted", false);
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

    public Long countDeletedUsersWithRoleUSER() {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {

            String hql = "SELECT COUNT(u) FROM User u WHERE u.role = :userRole AND u.deleted = :userDeleted";

            Query query = session.createQuery(hql);
            query.setParameter("userRole", "USER");
            query.setParameter("userDeleted", true);

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

    public List<Dependant> getDependants(User user) {
        Session session = null;
        List<Dependant> dependants = null;
        try {
            session = getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Dependant.class);

            // Add restrictions for user and deleted status
            criteria.add(Restrictions.eq("user", user));
            criteria.add(Restrictions.eq("deleted", false));

            dependants = criteria.list();  // Correct method to get a list
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return dependants;
    }
}



