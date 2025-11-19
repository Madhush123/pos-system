package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.request.RequestOrderDTO;

import java.sql.SQLException;

public interface OrderBo {
    int findLastOrderId() throws SQLException, ClassNotFoundException;
    boolean createOrder(RequestOrderDTO dto) throws SQLException, ClassNotFoundException;
}
