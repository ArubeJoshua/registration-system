package org.pahappa.systems.registrationapp.dao;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

public class UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAO() {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
    }

    public boolean isUsernameExists(String username) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", username));
            criteria.setProjection(Projections.rowCount()); // Count rows matching the criteria
            Long count = (Long) criteria.uniqueResult();
            session.clear();
            return count > 0; // If count > 0, username exists; otherwise, it does not

        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void save(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.clear();
        } catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void update(User user, String oldUsername) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", oldUsername));
            User existingUser = (User) criteria.uniqueResult();

            if (existingUser != null) {
                existingUser.setUsername(user.getUsername());
                existingUser.setFirstname(user.getFirstname());
                existingUser.setLastname(user.getLastname());
                existingUser.setDateOfBirth(user.getDateOfBirth());

                session.update(existingUser);

                transaction.commit();
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found with username: " + user.getUsername());
            }
            session.clear();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Log or handle the exception appropriately
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    public void deleteUser(String username) {
       Session session = null;
       Transaction transaction = null;
       try{
           session = sessionFactory.openSession();
           transaction = session.beginTransaction();
           Criteria criteria = session.createCriteria(User.class);
           criteria.add(Restrictions.eq("username", username));
           User user = (User) criteria.uniqueResult();

           if (user != null) {
               session.delete(user);
               transaction.commit();
           }
           session.clear();
       }catch (Exception e){
           if (transaction != null) {
               transaction.rollback();
           }
       }finally {
           if (session != null) {
               session.close();
           }
       }
    }

    public void deleteAllUsers() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Create a query to delete all users
            Query query = session.createQuery("DELETE FROM User");
            int rowsDeleted = query.executeUpdate();

            transaction.commit();

            System.out.println(rowsDeleted + " user(s) deleted successfully.");
            session.clear();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Handle or log the exception appropriately
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    public List<User> getAllUsers() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            session.clear();
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public User getUserByUsername(String username) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("username", username));
            session.clear();
            return (User) criteria.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    public boolean containUsers() {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            Criteria criteria = session.createCriteria(User.class);
            criteria.setProjection(Projections.rowCount()); // Count rows matching the criteria
            Long count = (Long) criteria.uniqueResult();
            session.clear();

            return count > 0; // Return true if there are users in the database
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
