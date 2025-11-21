package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDAO;
import com.devstack.pos.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<Customer,String> {
    List<String> loadAllIds() throws SQLException, ClassNotFoundException;
}
