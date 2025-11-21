package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.OrderDAO;
import com.devstack.pos.dto.response.ResponseStatisticsDTO;
import com.devstack.pos.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    @Override
    public int findLastOrderId() throws SQLException, ClassNotFoundException {
        ResultSet rs=CrudUtil.execute("SELECT * FROM orders ORDER BY order_id DESC LIMIT 1");
        if(rs.next()){
            return rs.getInt("order_id");
        }
        return 0;
    }

    @Override
    public List<Order> getOrderHistory(LocalDate date) throws SQLException, ClassNotFoundException {
        ResultSet rs=CrudUtil.execute("SELECT * FROM orders WHERE DATE(date)=? ORDER BY date DESC",date);
        List<Order> orders=new ArrayList<>();
        while (rs.next()){
            orders.add(new Order(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDate(4)
            ));
        }

        return orders;
    }

    @Override
    public List<ResponseStatisticsDTO> getStatistics(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        ResultSet rs=CrudUtil.execute("SELECT DATE(date) AS order_date, SUM(total_cost) AS total_sales FROM orders WHERE date BETWEEN ? AND ? GROUP BY DATE(date) ORDER By DATE(date)", from+" 00:00:00", to+" 23:59:59");
        List<ResponseStatisticsDTO> dtoList=new ArrayList<>();
        while (rs.next()){
            dtoList.add(new ResponseStatisticsDTO(
                    rs.getDate(1),
                    rs.getDouble(2)
            ));
        }
        return dtoList;
    }

    @Override
    public boolean save(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO orders VALUES(?,?,?,?)",
                order.getOrderId(),
                order.getCustomerId(),
                order.getTotalCost(),
                order.getDate());
    }

    @Override
    public boolean update(Order order) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(Integer integer) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public Order findById(Integer integer) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * FROM orders WHERE order_id = ?", integer);
        if (rs.next()) {
            return new Order(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getDate(4)
            );
        }
        return null;
    }

    @Override
    public List<Order> findAll(String searchText) throws SQLException, ClassNotFoundException {
        return List.of();
    }
}
