package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.custom.UserDAO;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.PasswordHash;

import java.sql.SQLException;
import java.util.UUID;

class UserDaoImplTest {


    void searchByUserEmail() {
    }


    void save() throws SQLException, ClassNotFoundException {
        UserDAO dao = new UserDAOImpl();
        User user = new User(
                UUID.randomUUID().toString(),
                "abc@gmail.com",
                "Janith",
                "838",
                PasswordHash.hashPassword("123")
        );
        boolean save = dao.save(user);
        System.out.println(save);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDaoImplTest test = new UserDaoImplTest();
        test.save();
    }
}