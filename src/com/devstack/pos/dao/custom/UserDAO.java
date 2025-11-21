package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDAO;
import com.devstack.pos.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO extends CrudDAO<User,String> {
     List<User> searchByName(String name);
     User findByUserEmail(String email) throws SQLException, ClassNotFoundException;
}
