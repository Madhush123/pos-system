package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.bo.custom.OrderBo;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dto.request.RequestOrderDTO;
import com.devstack.pos.dto.request.RequestOrderDetailsDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.devstack.pos.util.BoType;
import com.devstack.pos.view.tm.CartTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaceOrderFormController {
    public AnchorPane context;
    public Label lblHeader;
    public TextField txtName;
    public TextField txtSalary;
    public TextField txtAddress;
    public TableView<CartTM> tblProduct;
    public TableColumn<CartTM, Integer> colId;
    public TableColumn<CartTM, String> colDescription;
    public TableColumn<CartTM, Double> colUnitPrice;
    public TableColumn<CartTM, Integer> colQty;
    public TableColumn<CartTM, Double> colTotal;
    public TableColumn<CartTM, Button> colTools;
    public ComboBox<String> cmbCustomerIds;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public Label lblTotal;
    public TextField txtProductCode;

    private final OrderBo orderBo= BoFactory.getInstance().getBo(BoType.ORDER);
    private final CustomerBo customerBo= BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private final ProductBo productBo= BoFactory.getInstance().getBo(BoType.PRODUCT);

    private int orderId;
    private double fullTotal=0;

    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTools.setCellValueFactory(new PropertyValueFactory<>("btn"));

        disableFields();
        setOrderId();
        setCustomerIds();

        cmbCustomerIds.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCustomerDetails(newValue);
            }
        });

        txtProductCode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setProductDetails(newValue);
            }
        });
    }

    private void setProductDetails(String newValue) {
        try {
            ResponseProductDTO selectedProduct = productBo.findById(newValue);
            if (selectedProduct == null) {
                return;
            }
            txtDescription.setText(selectedProduct.getDescription());
            txtUnitPrice.setText(String.valueOf(selectedProduct.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(selectedProduct.getQtyOnHand()));
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void disableFields() {
        txtName.setDisable(true);
        txtAddress.setDisable(true);
        txtSalary.setDisable(true);

        txtDescription.setDisable(true);
        txtUnitPrice.setDisable(true);
        txtQtyOnHand.setDisable(true);
    }

    private void setCustomerDetails(String newValue) {
        try {
             ResponseCustomerDTO selectedCustomer = customerBo.getCustomerById(newValue);
             if (selectedCustomer == null) {
                 showAlert(Alert.AlertType.WARNING, "Customer not found!", ButtonType.OK);
                 return;
             }
             txtName.setText(selectedCustomer.getName());
             txtAddress.setText(selectedCustomer.getAddress());
             txtSalary.setText(String.valueOf(selectedCustomer.getSalary()));

        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCustomerIds() {
        try {
            List<String> ids= customerBo.loadAllIds();
            ObservableList<String> list= FXCollections.observableArrayList(ids);
            cmbCustomerIds.setItems(list);
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setOrderId() {
        try {
            orderId=orderBo.findLastOrderId();
            lblHeader.setText("Place Order (Order ID : #"+ ++orderId+ ")");

        } catch (SQLException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newOrderOnAction(ActionEvent actionEvent) {
    }

    public void addToCartOnAction(ActionEvent actionEvent) {
        int qty=Integer.parseInt(txtQty.getText());
        int qtyOnHand=Integer.parseInt(txtQtyOnHand.getText());

        if(qtyOnHand<qty ||qty==0){
            showAlert(Alert.AlertType.WARNING,"Please fill the stock",ButtonType.OK);
            return;
        }

        double unitPrice=Double.parseDouble(txtUnitPrice.getText());

        Button button=new Button("Remove");
        button.setStyle("-fx-background-color: red; -fx-text-fill: white");

        CartTM cartTM=new CartTM(
                txtProductCode.getText(),
                txtDescription.getText(),
                unitPrice,
                qty,
                unitPrice*qty,
                button
        );

        button.setOnAction(e -> {
            for(CartTM t:tms){
                if(t.getId().equals(cartTM.getId())){
                    tms.remove(t);
                    manageTotal();
                    txtQty.clear();
                    return;
                }
            }
        });

        addToTable(cartTM);
        manageTotal();
        txtQty.clear();
    }

    private void manageTotal() {
        fullTotal=0;
        for(CartTM cartTM:tblProduct.getItems()) {
            fullTotal+=cartTM.getTotal();
        }
        lblTotal.setText(String.format("Total Cost : %.2f/=", fullTotal));
    }

    ObservableList<CartTM> tms=FXCollections.observableArrayList();

    private void addToTable(CartTM cartTM) {
        for(CartTM ctm:tms){
            if(ctm.getId().equals(cartTM.getId())){
                ctm.setQty(ctm.getQty()+ cartTM.getQty());
                ctm.setTotal(ctm.getQty()*cartTM.getUnitPrice());
                tblProduct.refresh();
                return;
            }
        }
        tms.add(cartTM);
        tblProduct.setItems(tms);
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        try {
        if(orderId==0 || tms.isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Try again",ButtonType.OK);
            return;
        }
        RequestOrderDTO requestOrderDTO=new RequestOrderDTO();
        List<RequestOrderDetailsDTO> orderDetailsDTOList=new ArrayList<>();

        for(CartTM cartTM:tblProduct.getItems()){
            orderDetailsDTOList.add(new RequestOrderDetailsDTO(
                    cartTM.getId(),
                    cartTM.getUnitPrice(),
                    cartTM.getQty()
            ));
        }

        requestOrderDTO.setOrderId(orderId);
        requestOrderDTO.setCustomerId(cmbCustomerIds.getValue());
        requestOrderDTO.setOrderDetailsDTOList(orderDetailsDTOList);
        requestOrderDTO.setTotalCost(fullTotal);
        requestOrderDTO.setDate(new Date());

            boolean isSavedOrder=orderBo.createOrder(requestOrderDTO);
            if(isSavedOrder){
                setOrderId();
                clearAllFields();
                printBill();
                showAlert(Alert.AlertType.INFORMATION,"Order Completed!..",ButtonType.OK);
            }
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void printBill() {
    }

    private void clearAllFields() {
        cmbCustomerIds.setValue(null);
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();

        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtProductCode.clear();
        txtQty.clear();

        fullTotal=0;
        lblTotal.setText("Total Cost : 0/=");

        txtProductCode.requestFocus();
        tms.clear();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/devstack/pos/view/" + location + ".fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(load)
        );
    }

    private void showAlert(Alert.AlertType AlertType, String message, ButtonType btnType) {
        Alert alert = new Alert(AlertType, message, btnType);
        alert.initOwner(context.getScene().getWindow());
        alert.showAndWait();
    }
}
