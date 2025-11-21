package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.OrderBO;
import com.devstack.pos.dao.DAOFactory;
import com.devstack.pos.dao.custom.CustomerDAO;
import com.devstack.pos.dao.custom.OrderDAO;
import com.devstack.pos.dao.custom.OrderDetailDAO;
import com.devstack.pos.dao.custom.ProductDAO;
import com.devstack.pos.db.DBConnection;
import com.devstack.pos.dto.request.RequestOrderDTO;
import com.devstack.pos.dto.request.RequestOrderDetailsDTO;
import com.devstack.pos.dto.response.ResponseOrderDTO;
import com.devstack.pos.dto.response.ResponseStatisticsDTO;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.Order;
import com.devstack.pos.entity.OrderDetail;
import com.devstack.pos.util.DAOType;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderBOImpl implements OrderBO {

    private final OrderDAO orderDao= DAOFactory.getInstance().getDao(DAOType.ORDER);
    private final OrderDetailDAO orderDetailDao= DAOFactory.getInstance().getDao(DAOType.ORDER_DETAIL);
    private final ProductDAO productDao= DAOFactory.getInstance().getDao(DAOType.PRODUCT);
    private final CustomerDAO customerDao= DAOFactory.getInstance().getDao(DAOType.CUSTOMER);

    @Override
    public int findLastOrderId() throws SQLException, ClassNotFoundException {
        return orderDao.findLastOrderId();
    }

    @Override
    public boolean createOrder(RequestOrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        boolean order=orderDao.save(new Order(
                dto.getOrderId(),
                dto.getCustomerId(),
                dto.getTotalCost(),
                dto.getDate()
        ));
        if(order){
            for(RequestOrderDetailsDTO d:dto.getOrderDetailsDTOList()){
                boolean data=orderDetailDao.save(
                        new OrderDetail(
                                dto.getOrderId(),
                                d.getProductId(),
                                d.getUnitPrice(),
                                d.getQty(),
                                dto.getDate()
                        )
                );
                if(data){
                    boolean isUpdated =productDao.updateQty(d.getProductId(),d.getQty());
                    if(!isUpdated){
                        connection.rollback();
                        return false;
                    }
                }else {
                    connection.rollback();
                    return false;
                }
            }
        }else{
            connection.rollback();
        }
        connection.commit();
        return true;
    }

    @Override
    public List<ResponseOrderDTO> getOrderHistory(LocalDate date) throws SQLException, ClassNotFoundException {
        List<Order> orderList=orderDao.getOrderHistory(date);
        List<ResponseOrderDTO> responseOrderDTOList=new ArrayList<>();
        for(Order o:orderList){
            String customerName="UNKNOWN";
            Customer selectedCustomer = customerDao.findById(o.getCustomerId());
            if (selectedCustomer != null) {
                customerName = selectedCustomer.getName();
            }

            responseOrderDTOList.add(new ResponseOrderDTO(
                    o.getOrderId(),
                    customerName,
                    o.getTotalCost(),
                    o.getDate()
            ));

        }
        return responseOrderDTOList;
    }

    @Override
    public ResponseOrderDTO findOrderById(int orderId) throws SQLException, ClassNotFoundException {
        Order order=orderDao.findById(orderId);
        if(order!=null){
            return new ResponseOrderDTO(
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getTotalCost(),
                    order.getDate()
            );
        }
            return null;

    }

    @Override
    public List<ResponseStatisticsDTO> loadStatistics(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        return orderDao.getStatistics(from, to);
    }
}
