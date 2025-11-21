package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.request.RequestOrderDTO;
import com.devstack.pos.dto.response.ResponseOrderDTO;
import com.devstack.pos.dto.response.ResponseStatisticsDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface OrderBO {
    int findLastOrderId() throws SQLException, ClassNotFoundException;
    boolean createOrder(RequestOrderDTO dto) throws SQLException, ClassNotFoundException;
    List<ResponseOrderDTO> getOrderHistory(LocalDate date) throws SQLException, ClassNotFoundException;
    ResponseOrderDTO findOrderById(int orderId) throws SQLException, ClassNotFoundException;
    List<ResponseStatisticsDTO> loadStatistics(LocalDate from,LocalDate to) throws SQLException, ClassNotFoundException;


}
