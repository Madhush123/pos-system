package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.OrderBo;
import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.DaoFactory;
import com.devstack.pos.dao.custom.OrderDao;
import com.devstack.pos.dao.custom.OrderDetailDao;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.db.DBConnection;
import com.devstack.pos.dto.request.RequestOrderDTO;
import com.devstack.pos.dto.request.RequestOrderDetailsDTO;
import com.devstack.pos.entity.Order;
import com.devstack.pos.entity.OrderDetail;
import com.devstack.pos.util.DaoType;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {

    private final OrderDao orderDao=DaoFactory.getInstance().getDao(DaoType.ORDER);
    private final OrderDetailDao orderDetailDao=DaoFactory.getInstance().getDao(DaoType.ORDER_DETAIL);
    private final ProductDao productDao=DaoFactory.getInstance().getDao(DaoType.PRODUCT);

    @Override
    public int findLastOrderId() throws SQLException, ClassNotFoundException {
        return orderDao.findLastOrderId();
    }

    @Override
    public boolean createOrder(RequestOrderDTO dto) throws SQLException, ClassNotFoundException {
        Connection connection=DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        boolean order=orderDao.createOrder(new Order(
                dto.getOrderId(),
                dto.getCustomerId(),
                dto.getTotalCost(),
                dto.getDate()
        ));
        if(order){
            for(RequestOrderDetailsDTO d:dto.getOrderDetailsDTOList()){
                boolean data=orderDetailDao.createData(
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
}
