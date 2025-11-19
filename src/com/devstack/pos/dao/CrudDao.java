package com.devstack.pos.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDao<T,ID> extends SuperDao{
     boolean save(T t) throws SQLException, ClassNotFoundException;
     boolean update(T t) throws SQLException, ClassNotFoundException;
     boolean delete(ID id) throws SQLException, ClassNotFoundException;
     T findById(ID id) throws SQLException, ClassNotFoundException;
     List<T> findAll(String searchText) throws SQLException, ClassNotFoundException;
}
