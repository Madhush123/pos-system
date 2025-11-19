package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.CustomerBo;
import com.devstack.pos.dto.request.RequestCustomerDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.util.BoType;
import com.devstack.pos.view.tm.CustomerTm;
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

public class CustomerFormController {

    public AnchorPane context;
    public TextField txtName;
    public TextField txtSalary;
    public TextField txtAddress;
    public Button btnSave;
    public TextField txtSearch;
    public TableView<CustomerTm> tblCustomers;
    public TableColumn<CustomerTm, Long> colId;
    public TableColumn<CustomerTm, String> colName;
    public TableColumn<CustomerTm, String> colAddress;
    public TableColumn<CustomerTm, Double> colSalary;
    public TableColumn<CustomerTm, ButtonBar> colTools;

    private  String searchText="";
    private String selectedCustomerId=null;

    public void initialize() {
        searchAll();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colTools.setCellValueFactory(new PropertyValueFactory<>("tools"));

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                searchText=newValue;
                searchAll();
            }
        });
    }

    private CustomerBo customerBo= BoFactory.getInstance().getBo(BoType.CUSTOMER);

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        clearAll();
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        if(btnSave.getText().equalsIgnoreCase("Save Customer")) {
           try{
               boolean isSaved=customerBo.saveCustomer(
                       new RequestCustomerDTO(
                               txtName.getText().trim(),
                               txtAddress.getText().trim(),
                               Double.parseDouble(txtSalary.getText())
                       )
               );
               if(isSaved){
                   showAlert(Alert.AlertType.INFORMATION, "Customer Saved Successfully",ButtonType.OK);
                   searchAll();
                   clearAll();
               }

           }catch (SQLException | ClassNotFoundException e){
               showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
            }
        }else{

            if(selectedCustomerId==null){
                showAlert(Alert.AlertType.WARNING, "Please select the customer", ButtonType.OK);
                return;
            }
            try{
                customerBo.updateCustomer(
                        new RequestCustomerDTO(
                                txtName.getText().trim(),
                                txtAddress.getText().trim(),
                                Double.parseDouble(txtSalary.getText())
                        ),selectedCustomerId
                );
                showAlert(Alert.AlertType.INFORMATION, "Customer Updated Successfully",ButtonType.OK);
                searchAll();
                clearAll();

            }catch (SQLException | ClassNotFoundException e){
                showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
            }
        }
    }

    private void clearAll() {
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        btnSave.setText("Save Customer");
        selectedCustomerId=null;
    }

    private void searchAll() {
        try {
            List<ResponseCustomerDTO> list=customerBo.searchCustomers(searchText);
            ObservableList<CustomerTm> items= FXCollections.observableArrayList();
            long id=1;

            for(ResponseCustomerDTO rc:list){

                ButtonBar bar=new ButtonBar();
                Button updateButton=new Button("Update");
                Button deleteButton=new Button("Delete");

                updateButton.setStyle("-fx-background-color: green;-fx-text-fill: white");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white");

                bar.getButtons().addAll(updateButton,deleteButton);

                CustomerTm item=new CustomerTm(
                        id++,
                        rc.getName(),
                        rc.getAddress(),
                        rc.getSalary(),
                        bar
                );

                updateButton.setOnAction(actionEvent -> {
                    btnSave.setText("Update Customer");
                    txtName.setText(rc.getName());
                    txtAddress.setText(rc.getAddress());
                    txtSalary.setText(String.valueOf(rc.getSalary()));
                    selectedCustomerId=rc.getCustomerId();
                });

                deleteButton.setOnAction(actionEvent -> {
                    Optional<ButtonType> buttonType= showAlert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.NO,ButtonType.YES);
                    if(buttonType.get()==ButtonType.YES){
                        try {
                            customerBo.deleteCustomer(rc.getCustomerId());
                            System.out.println(rc.getCustomerId());
                            showAlert(Alert.AlertType.INFORMATION,"Customer Deleted",ButtonType.OK);
                            searchAll();
                        }catch (SQLException|ClassNotFoundException e){
                            showAlert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK);
                        }
                    }
                });

                items.add(item);
            }

            tblCustomers.setItems(items);

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
        }
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
