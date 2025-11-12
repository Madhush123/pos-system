package com.devstack.pos.controller;

import com.devstack.pos.view.tm.ProductTm;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ProductFormController {
    public AnchorPane context;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHansd;
    public Button btnSaveUpdate;
    public TextField txtSearch;
    public TableView<ProductTm> tblProducts;
    public TableColumn<ProductTm, String> colId;
    public TableColumn<ProductTm, String> colDescription;
    public TableColumn<ProductTm, Double> colUnitPrice;
    public TableColumn<ProductTm, Integer> colQTYOnHand;
    public TableColumn<ProductTm, String> colQRAvailability;
    public TableColumn<ProductTm, ButtonBar> colTools;
    public Label lblQty;

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newProductOnAction(ActionEvent actionEvent) {
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
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
