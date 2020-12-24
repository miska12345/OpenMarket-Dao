package io.openmarket.mysql.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public abstract class AbstractMySQLDao<T> {
    @Getter(AccessLevel.PROTECTED)
    private final SessionFactory sessionFactory;

    public AbstractMySQLDao(@NonNull final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<T> load(final Class<T> objClass, int id) {
        final Session session = sessionFactory.openSession();
        final Optional<T> result = Optional.ofNullable(session.get(objClass, id));
        session.close();
        return result;
    }

    public void save(@NonNull final T obj) {
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(obj);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(@NonNull final T obj) {
        final Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(obj);
        session.getTransaction().commit();
        session.close();
    }

    public abstract Optional<T> load(int id);
}
