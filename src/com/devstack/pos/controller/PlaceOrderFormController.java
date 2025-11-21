package com.devstack.pos.controller;

import com.devstack.pos.bo.BOFactory;
import com.devstack.pos.bo.custom.CustomerBO;
import com.devstack.pos.bo.custom.OrderBO;
import com.devstack.pos.bo.custom.ProductBO;
import com.devstack.pos.dto.request.RequestOrderDTO;
import com.devstack.pos.dto.request.RequestOrderDetailsDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.devstack.pos.util.BOType;
import com.devstack.pos.view.tm.CartTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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

    private final OrderBO orderBo= BOFactory.getInstance().getBo(BOType.ORDER);
    private final CustomerBO customerBo= BOFactory.getInstance().getBo(BOType.CUSTOMER);
    private final ProductBO productBo= BOFactory.getInstance().getBo(BOType.PRODUCT);

    private int orderId;
    private double fullTotal=0;

    ResponseCustomerDTO selectedCustomer;

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

        txtProductCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                txtProductCode.clear();
                txtDescription.clear();
                txtUnitPrice.clear();
                txtQtyOnHand.clear();

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
                 new Alert(Alert.AlertType.WARNING, "Customer not found!", ButtonType.OK).show();
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

        String productId = txtProductCode.getText().trim();
        String qtyText = txtQty.getText().trim();
        String qtyOnHandText = txtQtyOnHand.getText().trim();
        String unitPriceText = txtUnitPrice.getText().trim();
        String description = txtDescription.getText().trim();

        // Regex patterns
        String qtyRegex = "^[0-9]+$";
        String priceRegex = "^[0-9]+(\\.[0-9]{1,2})?$";

        // --- Field validations ---
        if (productId.isEmpty() || description.isEmpty() || qtyText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields before adding to cart!").show();
            return;
        }

        if (!qtyText.matches(qtyRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid quantity! Must be a positive whole number.").show();
            return;
        }

        if (!unitPriceText.matches(priceRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid unit price format!").show();
            return;
        }

        if (!qtyOnHandText.matches(qtyRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid stock quantity format!").show();
            return;
        }

        int qty = Integer.parseInt(qtyText);
        int qtyOnHand = Integer.parseInt(qtyOnHandText);
        double unitPrice = Double.parseDouble(unitPriceText);

        if (qty <= 0) {
            new Alert(Alert.AlertType.WARNING, "Quantity must be greater than 0!").show();
            return;
        }

        if (qty > qtyOnHand) {
            new Alert(Alert.AlertType.WARNING, "Insufficient stock available!").show();
            return;
        }

        // --- Create CartTM ---
        Button btn = new Button("Remove");
        btn.setStyle("-fx-background-color: red;-fx-text-fill: white");
        CartTM cartTM = new CartTM(productId, description, unitPrice, qty, unitPrice * qty, btn);

        btn.setOnAction(e -> {
            for(CartTM t:tms){
                if(t.getId().equals(cartTM.getId())){
                    tms.remove(t);
                    manageTotal();
                    txtQty.clear();
                    return;
                }
            }
        });

        addToTable(cartTM,qtyOnHand);
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

    private void addToTable(CartTM cartTM,int qtyOnHand) {
        for(CartTM ctm:tms){
            if(ctm.getId().equals(cartTM.getId())){
                int newQty = ctm.getQty() + cartTM.getQty();
                if(newQty<=qtyOnHand){
                    ctm.setQty(newQty);
                    ctm.setTotal(ctm.getQty()*cartTM.getUnitPrice());
                    tblProduct.refresh();
                    return;
                }else {
                    new Alert(Alert.AlertType.WARNING,
                            "Not enough stock to add more.",
                            ButtonType.OK).show();
                    return;
                }

            }
        }
        tms.add(cartTM);
        tblProduct.setItems(tms);
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        try {
            if (orderId == 0) {
                new Alert(Alert.AlertType.WARNING, "Order ID not initialized. Please try again!").show();
                return;
            }

           /* if (cmbCustomerIds.getValue() == null || cmbCustomerIds.getValue().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please select a customer before placing the order!").show();
                return;
            }*/

            if (tms.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Your cart is empty! Please add products first.").show();return;
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
                new Alert(Alert.AlertType.INFORMATION,"Order Completed!..",ButtonType.OK).show();
            }
        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void printBill() {
        if (tms.isEmpty()) return;

        VBox billPane = new VBox(5);
        billPane.setPadding(new Insets(10));

        billPane.getChildren().addAll(
                new Label("POS SYSTEM BILL"),
                new Label("Order ID: #" + --orderId),
                new Label("Customer Name: " + txtName.getText()),
                new Label("Customer Address: " + txtAddress.getText()),
                new Label("Date: " + new Date()),
                new Label("--------------------------------------------------------------------")
        );


        for (CartTM item : tms) {
            billPane.getChildren().add(new Label(
                    item.getDescription() + " | Qty: " + item.getQty() +
                            " | Unit Price: " + String.format("%.2f", item.getUnitPrice()) +
                            " | Total: " + String.format("%.2f", item.getTotal())
            ));
        }
        billPane.getChildren().addAll(
                new Label("--------------------------------------------------------------------"),
                new Label("TOTAL: " + String.format("%.2f", fullTotal)));

        // Use default printer / PDF printer
        Printer pdfPrinter = null;
        for (Printer p : Printer.getAllPrinters()) {
            if (p.getName().toLowerCase().contains("pdf")) {
                pdfPrinter = p;
                break;
            }
        }

        if (pdfPrinter == null) {
            new Alert(Alert.AlertType.ERROR, "No PDF printer found!").show();
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.setPrinter(pdfPrinter);

            PageLayout pageLayout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            job.getJobSettings().setPageLayout(pageLayout);

            // Automatically save PDF to desktop with order ID
            job.getJobSettings().setJobName("Order_" + orderId + ".pdf");

            if (job.printPage(billPane)) {
                job.endJob();
                fullTotal=0;
                txtName.clear();
                txtAddress.clear();
                new Alert(Alert.AlertType.INFORMATION, "Bill saved as PDF successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save PDF!").show();
            }
        }

        tms.clear();
    }

    private void clearAllFields() {
        cmbCustomerIds.setValue(null);
        txtSalary.clear();

        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtProductCode.clear();
        txtQty.clear();


        lblTotal.setText("Total Cost : 0/=");

        txtProductCode.requestFocus();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/devstack/pos/view/" + location + ".fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(load)
        );
    }


}
