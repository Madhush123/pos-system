package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.request.RequestCustomerDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBO {
     boolean saveCustomer(RequestCustomerDTO dto) throws SQLException, ClassNotFoundException;
     boolean updateCustomer(RequestCustomerDTO dto, String id) throws SQLException, ClassNotFoundException;
     boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
     ResponseCustomerDTO getCustomerById(String id) throws SQLException, ClassNotFoundException;
     List<ResponseCustomerDTO> searchCustomers(String searchText) throws SQLException, ClassNotFoundException;
     List<String> loadAllIds() throws SQLException, ClassNotFoundException;



}
