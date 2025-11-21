package com.devstack.pos.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T,ID> extends SuperDAO {
     boolean save(T t) throws SQLException, ClassNotFoundException;
     boolean update(T t) throws SQLException, ClassNotFoundException;
     boolean delete(ID id) throws SQLException, ClassNotFoundException;
     T findById(ID id) throws SQLException, ClassNotFoundException;
     List<T> findAll(String searchText) throws SQLException, ClassNotFoundException;
}
