package org.pahappa.systems.registrationapp.dao;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.TopUser;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractDAO<T> implements Serializable{
    protected SessionFactory sessionFactory;
    private final Class<T> type;

    public AbstractDAO(Class<T> type) {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
        this.type = type;
    }

    public boolean isUsernameExists(String username) {
        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(this.type);
        criteria.add(Restrictions.eq("username", username));
        criteria.add(Restrictions.eq("deleted", false));
        List<T> list = criteria.list();
        session.close();
        return list.size() > 0;
    }

    public void save(T entity) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(entity);
        transaction.commit();
        session.close();
    }

    public void update(T entity, String oldUsername) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        T oldEntity = (T) session.createCriteria(this.type)
                .add(Restrictions.eq("username", oldUsername)).uniqueResult();
        session.evict(oldEntity);
        session.update(entity);
        transaction.commit();
        session.close();
    }


    public T getEntityByUsername(String username) {
        Session session = this.sessionFactory.openSession();
        T entity = (T) session.createCriteria(this.type)
                .add(Restrictions.eq("username", username)).uniqueResult();
        session.close();
        return entity;
    }

    public boolean containEntities() {
        Session session = this.sessionFactory.openSession();
        try {
            Criteria criteria = session.createCriteria(this.type);
            criteria.add(Restrictions.eq("deleted", false));
            criteria.add(Restrictions.eq("role", "USER"));
            criteria.setProjection(Projections.rowCount());
            Long count = (Long) criteria.uniqueResult();
            return count > 0;
        } finally {
            session.close();
        }
    }

}