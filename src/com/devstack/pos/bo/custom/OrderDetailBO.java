package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.response.ResponseOrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailBO {
    List<ResponseOrderDetailDTO> getOrderDetailsByOrderId(int orderId) throws SQLException, ClassNotFoundException;
}
