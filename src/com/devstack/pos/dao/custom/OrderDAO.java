package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDAO;
import com.devstack.pos.dto.response.ResponseStatisticsDTO;
import com.devstack.pos.entity.Order;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface OrderDAO extends CrudDAO<Order, Integer> {
    int findLastOrderId() throws SQLException, ClassNotFoundException;
    List<Order> getOrderHistory(LocalDate date) throws SQLException, ClassNotFoundException;
    List<ResponseStatisticsDTO> getStatistics(LocalDate from,LocalDate to) throws SQLException, ClassNotFoundException;
}
