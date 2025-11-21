package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.UserBO;
import com.devstack.pos.dao.DAOFactory;
import com.devstack.pos.dao.custom.UserDAO;
import com.devstack.pos.dto.request.RequestUserDTO;
import com.devstack.pos.dto.response.ResponseUserDTO;
import com.devstack.pos.entity.User;
import com.devstack.pos.util.DAOType;
import com.devstack.pos.util.PasswordHash;

import java.sql.SQLException;
import java.util.UUID;

public class UserBOImpl implements UserBO {

    private final UserDAO userDao= DAOFactory.getInstance().getDao(DAOType.USER);

    @Override
    public boolean registerUser(RequestUserDTO userDTO) throws SQLException, ClassNotFoundException {
        userDao.save(new User(
                UUID.randomUUID().toString(),
                userDTO.getEmail(),
                userDTO.getDisplayName(),
                userDTO.getContactNumber(),
                PasswordHash.hashPassword(userDTO.getPassword())
        ));

        return true;
    }

    @Override
    public ResponseUserDTO loginUser(String email, String password) throws SQLException, ClassNotFoundException {
        User selectedUser = userDao.findByUserEmail(email);
        if(selectedUser==null){
            return new ResponseUserDTO(null,null,404,false,"User not found");
        }

        boolean passwordCheckedStatus=PasswordHash.checkPassword(password, selectedUser.getPassword());
        if(passwordCheckedStatus){
            return new ResponseUserDTO(selectedUser.getDisplayName(),selectedUser.getContactNumber(),200,true,"Login Successful");
        }else{
            return new ResponseUserDTO(null,null,401,false,"Wrong Password");
        }
    }
}
