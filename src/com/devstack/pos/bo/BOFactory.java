package com.devstack.pos.bo;

import com.devstack.pos.bo.custom.impl.*;
import com.devstack.pos.util.BOType;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {}
    public static BOFactory getInstance() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public <T> T getBo(BOType boType) {
        switch (boType) {
            case USER:
                return (T) new UserBOImpl();
            case CUSTOMER:
                return (T) new CustomerBOImpl();
            case PRODUCT:
                return (T) new ProductBOImpl();
            case ORDER:
                return (T) new OrderBOImpl();
            case ORDER_DETAILS:
                return (T) new OrderDetailBOImpl();
            default:
                return null;

        }
    }
}
