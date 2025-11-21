package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.OrderDetailDAO;
import com.devstack.pos.entity.OrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {

    @Override
    public List<OrderDetail> findOrderDetailsByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        ResultSet rs=CrudUtil.execute("SELECT * FROM order_details WHERE order_id=?",orderId);
        List<OrderDetail> orderDetailsList = new ArrayList<>();
        while(rs.next()){
            orderDetailsList.add(new OrderDetail(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getInt(4),
                    rs.getDate(5)
            ));
        }
        return orderDetailsList;
    }


    @Override
    public boolean save(OrderDetail detail) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO order_details VALUES (?,?,?,?,?)",
                detail.getOrderId(),
                detail.getProductId(),
                detail.getUnitPrice(),
                detail.getQty(),
                detail.getDate()
        );
    }

    @Override
    public boolean update(OrderDetail detail) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public OrderDetail findById(Integer integer) throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<OrderDetail> findAll(String searchText) throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
