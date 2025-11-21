package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.OrderDetailBO;
import com.devstack.pos.dao.DAOFactory;
import com.devstack.pos.dao.custom.OrderDetailDAO;
import com.devstack.pos.dao.custom.ProductDAO;
import com.devstack.pos.dto.response.ResponseOrderDetailDTO;
import com.devstack.pos.entity.OrderDetail;
import com.devstack.pos.entity.Product;
import com.devstack.pos.util.DAOType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailBOImpl implements OrderDetailBO {

    private final OrderDetailDAO orderDetailDao= DAOFactory.getInstance().getDao(DAOType.ORDER_DETAIL);
    private final ProductDAO productDao= DAOFactory.getInstance().getDao(DAOType.PRODUCT);

    @Override
    public List<ResponseOrderDetailDTO> getOrderDetailsByOrderId(int orderId) throws SQLException, ClassNotFoundException {
        List<OrderDetail> orderDetails=orderDetailDao.findOrderDetailsByOrderId(orderId);
        List<ResponseOrderDetailDTO> dtoList=new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            String productName="UNKNOWN";
            Product selectedProduct=productDao.findById(orderDetail.getProductId());
            if(selectedProduct!=null){
                productName=selectedProduct.getDescription();
            }
            dtoList.add(new ResponseOrderDetailDTO(
                    orderDetail.getOrderId(),
                    orderDetail.getProductId(),
                    productName,
                    orderDetail.getUnitPrice(),
                    orderDetail.getQty(),
                    orderDetail.getUnitPrice()*orderDetail.getQty()
            ));
        }
        return dtoList;
    }
}
