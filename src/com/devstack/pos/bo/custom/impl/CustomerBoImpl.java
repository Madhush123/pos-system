package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.CustomerDao;
import com.devstack.pos.dto.request.RequestCustomerDto;
import com.devstack.pos.dto.response.ResponseCustomerDto;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.util.DaoType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerBoImpl implements CustomerBo {

    CustomerDao customerDao= DaoFactory.getInstance().getDao(DaoType.CUSTOMER);

    @Override
    public boolean saveCustomer(RequestCustomerDto dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(
                toCustomer(dto)
        );
    }

    @Override
    public boolean updateCustomer(RequestCustomerDto dto,String id) throws SQLException, ClassNotFoundException {
        Customer customer=toCustomer(dto);
        customer.setId(id);
        return customerDao.update(customer);
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public ResponseCustomerDto getCustomerById(String id) throws SQLException, ClassNotFoundException {
        return toResponseCustomerDto(customerDao.findById(id));
    }

    @Override
    public List<ResponseCustomerDto> getAllCustomers() throws SQLException, ClassNotFoundException {
        List<ResponseCustomerDto> list = new ArrayList<>();
        for(Customer c:customerDao.findAll()){
            list.add(toResponseCustomerDto(c));
        }
        return list;
    }

    @Override
    public List<ResponseCustomerDto> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        List<ResponseCustomerDto> list = new ArrayList<>();
        for(Customer c:customerDao.searchAll(searchText)){
            list.add(toResponseCustomerDto(c));
        }
        return list;
    }

    private ResponseCustomerDto toResponseCustomerDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new ResponseCustomerDto(
            customer.getId(),
            customer.getName(),
            customer.getAddress(),
            customer.getSalary()
        );
    }

    private Customer toCustomer(RequestCustomerDto dto) {
        if (dto == null) {
            return null;
        }
        return new Customer(
                UUID.randomUUID().toString(),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary()
        );
    }
}
