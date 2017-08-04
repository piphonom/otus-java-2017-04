package ru.otus.lesson16.dbservice.implementation;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.otus.lesson16.dbservice.datasets.PhoneDataSet;
import ru.otus.lesson16.dbservice.datasets.UserDataSet;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

public class UserDataSetDAO {
    private Session session;

    public UserDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
    }

    public UserDataSet read(int id) {
        return session.load(UserDataSet.class, id);
    }

    public UserDataSet readByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<UserDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }

    public List<PhoneDataSet> readPhonesByUserName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        Root<PhoneDataSet> phones = criteria.from(PhoneDataSet.class);
        Subquery<Integer> subquery = criteria.subquery(Integer.class);
        Root<UserDataSet> users = subquery.from(UserDataSet.class);
        subquery.select(users.get("id")).where(
                builder.equal(users.get("name"), name));
        criteria.select(phones).where(builder.in(phones.get("user")).value(subquery));

        TypedQuery<PhoneDataSet> query = session.createQuery(criteria);
        List<PhoneDataSet> result = query.getResultList();
        return result;
    }
}
