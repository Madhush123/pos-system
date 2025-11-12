package com.devstack.pos.bo;

import com.devstack.pos.bo.custom.impl.CustomerBoImpl;
import com.devstack.pos.bo.custom.impl.UserBoImpl;
import com.devstack.pos.dao.custom.impl.UserDaoImpl;
import com.devstack.pos.util.BoType;

public class BoFactory {
    private static BoFactory boFactory;
    private BoFactory() {}
    public static BoFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BoFactory();
        }
        return boFactory;
    }

   /* public SuperDao getDao(DaoType daoType) {
        switch (daoType) {
            case USER:
                return new UserDaoImpl();
            case CUSTOMER:
                return null;
            case PRODUCT:
                return null;
            default:
                return null;

        }
    }*/

    public <T> T getBo(BoType boType) {
        switch (boType) {
            case USER:
                return (T) new UserBoImpl();
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case PRODUCT:
                return null;
            default:
                return null;

        }
    }
}
