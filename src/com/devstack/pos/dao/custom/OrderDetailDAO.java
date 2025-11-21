package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDAO;
import com.devstack.pos.entity.OrderDetail;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail, Integer> {
    List<OrderDetail> findOrderDetailsByOrderId(int orderId) throws SQLException, ClassNotFoundException;
}
