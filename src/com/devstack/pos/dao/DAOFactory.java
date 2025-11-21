package com.devstack.pos.dao;

import com.devstack.pos.dao.custom.impl.*;
import com.devstack.pos.util.DAOType;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {}
    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public <T> T getDao(DAOType daoType) {
        switch (daoType) {
            case USER:
                return (T) new UserDAOImpl();
            case CUSTOMER:
                return (T) new CustomerDAOImpl();
            case PRODUCT:
                return (T) new ProductDAOImpl();
            case ORDER:
                return (T) new OrderDAOImpl();
            case ORDER_DETAIL:
                return (T) new OrderDetailDAOImpl();
            default:
                return null;

        }
    }
}
