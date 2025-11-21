package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.CustomerBO;
import com.devstack.pos.dao.DAOFactory;
import com.devstack.pos.dao.custom.CustomerDAO;
import com.devstack.pos.dto.request.RequestCustomerDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.util.DAOType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomerBOImpl implements CustomerBO {

    private final CustomerDAO customerDao= DAOFactory.getInstance().getDao(DAOType.CUSTOMER);

    @Override
    public boolean saveCustomer(RequestCustomerDTO dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(
                toCustomer(dto)
        );
    }

    @Override
    public boolean updateCustomer(RequestCustomerDTO dto, String id) throws SQLException, ClassNotFoundException {
        Customer customer=toCustomer(dto);
        customer.setId(id);
        return customerDao.update(customer);
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDao.delete(id);
    }

    @Override
    public ResponseCustomerDTO getCustomerById(String id) throws SQLException, ClassNotFoundException {
        return toResponseCustomerDto(customerDao.findById(id));
    }

    @Override
    public List<ResponseCustomerDTO> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        List<ResponseCustomerDTO> list = new ArrayList<>();
        for(Customer c:customerDao.findAll(searchText)){
            list.add(toResponseCustomerDto(c));
        }
        return list;
    }

    @Override
    public List<String> loadAllIds() throws SQLException, ClassNotFoundException {
        return customerDao.loadAllIds();
    }

    private ResponseCustomerDTO toResponseCustomerDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return new ResponseCustomerDTO(
            customer.getId(),
            customer.getName(),
            customer.getAddress(),
            customer.getSalary()
        );
    }

    private Customer toCustomer(RequestCustomerDTO dto) {
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
