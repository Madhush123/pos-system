package com.devstack.pos.bo.custom;

import com.devstack.pos.dto.request.RequestProductDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface ProductBo {
     boolean createProduct(RequestProductDTO product) throws IOException, WriterException, SQLException, ClassNotFoundException;
     boolean updateProduct(RequestProductDTO dto, String id, byte[] qr) throws SQLException, ClassNotFoundException;
     List<ResponseProductDTO> searchProducts(String searchText) throws IOException, WriterException, SQLException, ClassNotFoundException;
     ResponseProductDTO findById(String id) throws SQLException, ClassNotFoundException;
     boolean deleteProduct(String id) throws SQLException, ClassNotFoundException;
     long fillableCount() throws SQLException, ClassNotFoundException;

}
