package com.devstack.pos.bo.custom.impl;

import com.devstack.pos.bo.custom.ProductBO;
import com.devstack.pos.dao.DAOFactory;
import com.devstack.pos.dao.custom.ProductDAO;
import com.devstack.pos.dto.request.RequestProductDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.devstack.pos.entity.Product;
import com.devstack.pos.util.DAOType;
import com.devstack.pos.util.QRGenerator;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductBOImpl implements ProductBO {

    private final ProductDAO productDao= DAOFactory.getInstance().getDao(DAOType.PRODUCT);
    @Override
    public boolean createProduct(RequestProductDTO product) throws IOException, WriterException, SQLException, ClassNotFoundException {
        String id=UUID.randomUUID().toString();
        byte[] qr=QRGenerator.generateQRCodeImageBytes(id,300,300);

        Product p= new Product(
                id,
                product.getDescription(),
                product.getUnitPrice(),
                product.getQtyOnHand(),
                qr
        );
        return productDao.save(p);
    }

    @Override
    public boolean updateProduct(RequestProductDTO dto, String id, byte[] qr) throws SQLException, ClassNotFoundException {
        Product p=new Product(
                id,
                dto.getDescription(),
                dto.getUnitPrice(),
                dto.getQtyOnHand(),
                qr

        );
        return productDao.update(p);
    }

    @Override
    public List<ResponseProductDTO> searchProducts(String text) throws IOException, WriterException, SQLException, ClassNotFoundException {
        List<ResponseProductDTO> list=new ArrayList<>();
        for(Product p:productDao.findAll(text)){
            ResponseProductDTO dto=new ResponseProductDTO(
                    p.getProductId(),
                    p.getDescription(),
                    p.getUnitPrice(),
                    p.getQtyOnHand(),
                    p.getQr()
            );
            list.add(dto);
        }
        return list;
    }

    @Override
    public ResponseProductDTO findById(String id) throws SQLException, ClassNotFoundException {
         Product product=productDao.findById(id);
         if(product==null){
             return null;
         }
         return new ResponseProductDTO(
               product.getProductId(),
               product.getDescription(),
               product.getUnitPrice(),
               product.getQtyOnHand(),
               product.getQr()
         );
    }

    @Override
    public boolean deleteProduct(String id) throws SQLException, ClassNotFoundException {
        return productDao.delete(id);
    }

    @Override
    public long fillableCount() throws SQLException, ClassNotFoundException {
        return productDao.fillableCount();
    }
}
