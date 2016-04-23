package com.devignition.nest.db;

import com.devignition.nest.core.NestThermostat;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

public class NestDao extends AbstractDAO<NestThermostat> {

    public NestDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public NestThermostat getById(long id) {
        return get(id);
    }

    public List<NestThermostat> getAll() {
        Criteria criteria = currentSession()
                .createCriteria(getEntityClass())
                .addOrder(Order.asc("location"));
        return list(criteria);
    }

    public long create(NestThermostat nest) {
        checkState(nest.getId() == null, "New nests cannot have an id");
        return persist(nest).getId();
    }

    public void update(NestThermostat nest) {
        checkState(nest.getId() != null, "Nest must have an id in order to update it");
        persist(nest);
    }

    public void delete(long id) {
        currentSession().delete(getById(id));
    }

}
