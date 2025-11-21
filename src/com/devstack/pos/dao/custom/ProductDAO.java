package com.devstack.pos.dao.custom;

import com.devstack.pos.dao.CrudDAO;
import com.devstack.pos.entity.Product;

import java.sql.SQLException;

public interface ProductDAO extends CrudDAO<Product,String> {
     long fillableCount() throws SQLException, ClassNotFoundException;
     boolean updateQty(String productId, int qty) throws SQLException, ClassNotFoundException;
}
