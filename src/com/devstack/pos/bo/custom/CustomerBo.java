package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.request.RequestCustomerDto;
import com.devstack.pos.dto.response.ResponseCustomerDto;
import com.devstack.pos.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface CustomerBo {
    public boolean saveCustomer(RequestCustomerDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateCustomer(RequestCustomerDto dto,String id) throws SQLException, ClassNotFoundException;
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    public ResponseCustomerDto getCustomerById(String id) throws SQLException, ClassNotFoundException;
    public List<ResponseCustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException;
    public List<ResponseCustomerDto> searchCustomers(String searchText) throws SQLException, ClassNotFoundException;



}
