package com.devstack.pos.controller;

import com.devstack.pos.bo.BOFactory;
import com.devstack.pos.bo.custom.ProductBO;
import com.devstack.pos.dto.request.RequestProductDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.devstack.pos.util.BOType;
import com.devstack.pos.view.tm.ProductTM;
import com.google.zxing.WriterException;
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
import java.util.List;
import java.util.Optional;

public class ProductFormController {
    public AnchorPane context;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public Button btnSaveUpdate;
    public TextField txtSearch;
    public TableView<ProductTM> tblProducts;
    public TableColumn<ProductTM, Long> colId;
    public TableColumn<ProductTM, String> colDescription;
    public TableColumn<ProductTM, Double> colUnitPrice;
    public TableColumn<ProductTM, Integer> colQTYOnHand;
    public TableColumn<ProductTM, Button> colQRAvailability;
    public TableColumn<ProductTM, ButtonBar> colTools;
    public Label lblQty;

    private String selectedProductId=null;
    private byte[] qr=null;
    private String searchText="";

    private final ProductBO productBo= BOFactory.getInstance().getBo(BOType.PRODUCT);

    public void initialize() {
        searchAll();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQTYOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colQRAvailability.setCellValueFactory(new PropertyValueFactory<>("qrAvailability"));
        colTools.setCellValueFactory(new PropertyValueFactory<>("tools"));

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                searchText=newValue;
                searchAll();
            }
        });

        loadFillableCount();
    }

    private void loadFillableCount() {
        try {
            long l=productBo.fillableCount();
            lblQty.setText(String.valueOf(l));
        } catch (SQLException |ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newProductOnAction(ActionEvent actionEvent) {
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        String description = txtDescription.getText().trim();
        String unitPriceText = txtUnitPrice.getText().trim();
        String qtyText = txtQtyOnHand.getText().trim();

        // Regex patterns
        String priceRegex = "^[0-9]+(\\.[0-9]{1,2})?$"; // allows 123 or 123.45
        String qtyRegex = "^[0-9]+$"; // only digits

        // ---- Validation ----
        if (description.isEmpty() || unitPriceText.isEmpty() || qtyText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are required!").show();
            return;
        }

        if (!unitPriceText.matches(priceRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid unit price! Use a valid number (e.g., 12.50)").show();
            return;
        }

        if (!qtyText.matches(qtyRegex)) {
            new Alert(Alert.AlertType.WARNING, "Quantity must be a whole number!").show();
            return;
        }

        double unitPrice = Double.parseDouble(unitPriceText);
        int qty = Integer.parseInt(qtyText);

        if (unitPrice <= 0) {
            new Alert(Alert.AlertType.WARNING, "Unit price must be greater than 0!").show();
            return;
        }

        if (qty < 0) {
            new Alert(Alert.AlertType.WARNING, "Quantity cannot be negative!").show();
            return;
        }
        if(btnSaveUpdate.getText().equals("Save Product")) {

            //Sve Product
            RequestProductDTO dto=new RequestProductDTO(description,unitPrice,qty);
            try{
                boolean isSaved=productBo.createProduct(dto);
                if(isSaved){
                    new Alert(Alert.AlertType.INFORMATION, "Product Saved Successfully",ButtonType.OK).show();
                    clear();
                    searchAll();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Please try again",ButtonType.OK).show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
                e.printStackTrace();
            }
        }else{
            //Update Product
            if(selectedProductId==null){
                new Alert(Alert.AlertType.WARNING, "Please select the customer", ButtonType.OK).show();
                return;
            }
            try{
                productBo.updateProduct(
                       new RequestProductDTO(description,unitPrice,qty),selectedProductId,qr
                );
                new Alert(Alert.AlertType.INFORMATION, "Product Updated Successfully",ButtonType.OK).show();
                searchAll();
                clear();

            }catch (SQLException | ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
            }

        }
    }

    private void searchAll() {
        try {
            List<ResponseProductDTO> list=productBo.searchProducts(searchText);
            ObservableList<ProductTM> items= FXCollections.observableArrayList();
            long id=1;

            for(ResponseProductDTO rc:list){
                ButtonBar bar=new ButtonBar();
                Button updateButton=new Button("Update");
                Button deleteButton=new Button("Delete");
                Button qrButton=new Button("Show QR");

                updateButton.setStyle("-fx-background-color: green;-fx-text-fill: white");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white");
                qrButton.setStyle("-fx-background-color:  #0652DD; -fx-text-fill: white");

                bar.getButtons().addAll(updateButton,deleteButton);

                ProductTM item=new ProductTM(
                        id++,
                        rc.getDescription(),
                        rc.getUnitPrice(),
                        rc.getQtyOnHand(),
                        qrButton,
                        bar
                );

                updateButton.setOnAction(actionEvent -> {
                    btnSaveUpdate.setText("Update Customer");
                    txtDescription.setText(rc.getDescription());
                    txtUnitPrice.setText(String.valueOf(rc.getUnitPrice()));
                    txtQtyOnHand.setText(String.valueOf(rc.getQtyOnHand()));
                    selectedProductId=rc.getProductId();
                    qr= rc.getQr();
                });

                deleteButton.setOnAction(actionEvent -> {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.NO,ButtonType.YES);
                    Optional<ButtonType> buttonType=alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        try {
                            productBo.deleteProduct(rc.getProductId());
                            new Alert(Alert.AlertType.INFORMATION,"Product Deleted",ButtonType.OK).show();
                            searchAll();
                        }catch (SQLException|ClassNotFoundException e){
                            new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
                        }
                    }
                });

                qrButton.setOnAction(actionEvent -> {
                    URL resource = getClass().getResource("/com/devstack/pos/view/QRForm.fxml");

                    try {
                        FXMLLoader loader =new FXMLLoader(resource);
                        Parent parent=loader.load();

                        QRFormController controller=loader.getController();
                        controller.setQr(rc.getQr());
                        Stage stage=new Stage();
                        stage.setScene(new Scene(parent));
                        stage.initOwner(context.getScene().getWindow());
                        stage.show();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

                items.add(item);
            }

            tblProducts.setItems(items);

        } catch (SQLException|IOException|ClassNotFoundException|WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void clear() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        btnSaveUpdate.setText("Save Product");
    }

    public void GenQROnAction(ActionEvent actionEvent) {
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
