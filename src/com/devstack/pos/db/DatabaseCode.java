package com.devstack.pos.db;

import com.devstack.pos.model.LoginData;
import com.devstack.pos.model.User;
import com.devstack.pos.util.CrudUtil;
import com.devstack.pos.util.PasswordHash;

import java.sql.*;

public class DatabaseCode {
    //Registration process

    public static boolean registerUser(User user) throws ClassNotFoundException, SQLException {

        return CrudUtil.execute("INSERT INTO user VALUES(?,?,?,?,?)",
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getContactNumber(),
                user.getPassword());
    }

    public static LoginData loginUser(String email,String password) throws ClassNotFoundException, SQLException {

        ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE email=?",email);
        if(resultSet.next()){
            String userEmail=resultSet.getString("email");
            String hashedPassword=resultSet.getString("password");
            String displayName=resultSet.getString("display_name");


           boolean passwordCheckedStatus= PasswordHash.checkPassword(password, hashedPassword);

           if(passwordCheckedStatus){
               return new LoginData(200,true,"Login Successful!",userEmail,displayName);
           }else{
               return new LoginData(401,false,"Wrong Password!",null,null);
           }
        }else{
            return new LoginData(404,false,"Can't find user!",null,null);
        }
    }
}
