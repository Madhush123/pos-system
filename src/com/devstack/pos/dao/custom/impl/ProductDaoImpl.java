package com.devstack.pos.dao.custom.impl;

import com.devstack.pos.dao.CrudUtil;
import com.devstack.pos.dao.custom.ProductDao;
import com.devstack.pos.entity.Customer;
import com.devstack.pos.entity.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public long fillableCount() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT COUNT(*) FROM product WHERE qty_on_hand<=20");
        if(resultSet.next()){
            return resultSet.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean updateQty(String productId, int qty) throws SQLException, ClassNotFoundException {
         return CrudUtil.execute("UPDATE product SET qty_on_hand=(qty_on_hand-?) WHERE product_id=?",qty,productId);
    }


    @Override
    public boolean save(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO product VALUES (?,?,?,?,?)",
                product.getProductId(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getQtyOnHand(),
                product.getQr()!=null?product.getQr():null);
    }

    @Override
    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE product SET description=?,unit_price=?,qty_on_hand=? WHERE product_id=?",
                product.getDescription(),
                product.getUnitPrice(),
                product.getQtyOnHand(),
                product.getProductId()
        );
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM product WHERE product_id=?",s);
    }

    @Override
    public Product findById(String s) throws SQLException, ClassNotFoundException {
       ResultSet resultSet=CrudUtil.execute("SELECT * FROM product WHERE product_id=?",s);
       if(resultSet.next()){
           return new Product(
                   resultSet.getString(1),
                   resultSet.getString(2),
                   resultSet.getDouble(3),
                   resultSet.getInt(4),
                   resultSet.getBytes(5)
           );
       }
           return null;

    }

    @Override
    public List<Product> findAll(String searchText) throws SQLException, ClassNotFoundException {
        searchText="%"+searchText+"%";
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM product WHERE description LIKE ?", searchText);
        List<Product> products=new ArrayList<>();
        while(resultSet.next()){
            products.add(new Product(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getBytes(5)
            ));
        }
        return products;
    }

}
