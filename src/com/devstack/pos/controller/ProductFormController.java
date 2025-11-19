package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.ProductBo;
import com.devstack.pos.dto.request.RequestProductDTO;
import com.devstack.pos.dto.response.ResponseProductDTO;
import com.devstack.pos.util.BoType;
import com.devstack.pos.view.tm.ProductTm;
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
    public TextField txtQtyOnHansd;
    public Button btnSaveUpdate;
    public TextField txtSearch;
    public TableView<ProductTm> tblProducts;
    public TableColumn<ProductTm, Long> colId;
    public TableColumn<ProductTm, String> colDescription;
    public TableColumn<ProductTm, Double> colUnitPrice;
    public TableColumn<ProductTm, Integer> colQTYOnHand;
    public TableColumn<ProductTm, Button> colQRAvailability;
    public TableColumn<ProductTm, ButtonBar> colTools;
    public Label lblQty;

    private String selectedProductId=null;
    private byte[] qr=null;
    private String searchText="";

    private final ProductBo productBo= BoFactory.getInstance().getBo(BoType.PRODUCT);

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
        if(btnSaveUpdate.getText().equals("Save Product")) {
            //Sve Product
            RequestProductDTO dto=new RequestProductDTO(
                    txtDescription.getText().trim(),
                    Double.parseDouble(txtUnitPrice.getText().trim()),
                    Integer.parseInt(txtQtyOnHansd.getText().trim())
            );
            try{
                boolean isSaved=productBo.createProduct(dto);
                if(isSaved){
                    showAlert(Alert.AlertType.INFORMATION, "Product Saved Successfully",ButtonType.OK);
                    clear();
                    searchAll();
                }else {
                    showAlert(Alert.AlertType.ERROR,"Please try again",ButtonType.OK);
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
                e.printStackTrace();
            }
        }else{
            //Update Product
            if(selectedProductId==null){
                showAlert(Alert.AlertType.WARNING, "Please select the customer", ButtonType.OK);
                return;
            }
            try{
                productBo.updateProduct(
                       new RequestProductDTO(
                                txtDescription.getText().trim(),
                                Double.parseDouble(txtUnitPrice.getText().trim()),
                                Integer.parseInt(txtQtyOnHansd.getText().trim())
                        ),selectedProductId,qr
                );
                showAlert(Alert.AlertType.INFORMATION, "Product Updated Successfully",ButtonType.OK);
                searchAll();
                clear();

            }catch (SQLException | ClassNotFoundException e){
                showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
            }

        }
    }

    private void searchAll() {
        try {
            List<ResponseProductDTO> list=productBo.searchProducts(searchText);
            ObservableList<ProductTm> items= FXCollections.observableArrayList();
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

                ProductTm item=new ProductTm(
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
                    txtQtyOnHansd.setText(String.valueOf(rc.getQtyOnHand()));
                    selectedProductId=rc.getProductId();
                    qr= rc.getQr();
                });

                deleteButton.setOnAction(actionEvent -> {
                    Optional<ButtonType> buttonType= showAlert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.NO,ButtonType.YES);
                    if(buttonType.get()==ButtonType.YES){
                        try {
                            productBo.deleteProduct(rc.getProductId());
                            showAlert(Alert.AlertType.INFORMATION,"Product Deleted",ButtonType.OK);
                            searchAll();
                        }catch (SQLException|ClassNotFoundException e){
                            showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
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
        txtQtyOnHansd.clear();
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

    private void showAlert(Alert.AlertType AlertType, String message, ButtonType btnType) {
        Alert alert = new Alert(AlertType, message, btnType);
        alert.initOwner(context.getScene().getWindow());
        alert.showAndWait();
    }

    private Optional<ButtonType> showAlert(Alert.AlertType AlertType, String message, ButtonType btnType1,ButtonType btnType2) {
        Alert alert = new Alert(AlertType, message, btnType1,btnType2);
        alert.initOwner(context.getScene().getWindow());
        return alert.showAndWait();
    }
}
