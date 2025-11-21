package com.devstack.pos.controller;

import com.devstack.pos.bo.BOFactory;
import com.devstack.pos.bo.custom.CustomerBO;
import com.devstack.pos.dto.request.RequestCustomerDTO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.util.BOType;
import com.devstack.pos.view.tm.CustomerTM;
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
    public TableView<CustomerTM> tblCustomers;
    public TableColumn<CustomerTM, Long> colId;
    public TableColumn<CustomerTM, String> colName;
    public TableColumn<CustomerTM, String> colAddress;
    public TableColumn<CustomerTM, Double> colSalary;
    public TableColumn<CustomerTM, ButtonBar> colTools;

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

    private CustomerBO customerBo= BOFactory.getInstance().getBo(BOType.CUSTOMER);

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        clearAll();
    }

    public void saveUpdateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText().trim();
        String address = txtAddress.getText().trim();
        String salaryText = txtSalary.getText().trim();

        // Regex patterns
        String nameRegex = "^[A-Za-z ]{3,}$"; // At least 3 letters, spaces allowed
        String addressRegex = "^[A-Za-z0-9 ,.-/]{5,}$"; // Letters, numbers, and basic punctuation
        String salaryRegex = "^[0-9]+(\\.[0-9]{1,2})?$"; // e.g. 1000 or 1000.50

        // --- Validation ---
        if (name.isEmpty() || address.isEmpty() || salaryText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "All fields are required!").show();
            return;
        }

        if (!name.matches(nameRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid name! Use letters only (min 3 characters).").show();
            return;
        }

        if (!address.matches(addressRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid address! Minimum 5 characters.").show();
            return;
        }

        if (!salaryText.matches(salaryRegex)) {
            new Alert(Alert.AlertType.WARNING, "Invalid salary format! Example: 50000 or 50000.75").show();
            return;
        }

        double salary = Double.parseDouble(salaryText);
        if (salary <= 0) {
            new Alert(Alert.AlertType.WARNING, "Salary must be greater than 0!").show();
            return;
        }

        if(btnSave.getText().equalsIgnoreCase("Save Customer")) {
           try{
               boolean isSaved=customerBo.saveCustomer(
                       new RequestCustomerDTO(name,address,salary)
               );
               if(isSaved){
                   new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully",ButtonType.OK).show();
                   searchAll();
                   clearAll();
               }

           }catch (SQLException | ClassNotFoundException e){
               new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
            }
        }else{

            if(selectedCustomerId==null){
                new Alert(Alert.AlertType.WARNING, "Please select the customer", ButtonType.OK).show();
                return;
            }
            try{
                customerBo.updateCustomer(
                        new RequestCustomerDTO(name,address,salary),selectedCustomerId
                );
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully",ButtonType.OK).show();
                searchAll();
                clearAll();

            }catch (SQLException | ClassNotFoundException e){
                new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
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
            ObservableList<CustomerTM> items= FXCollections.observableArrayList();
            long id=1;

            for(ResponseCustomerDTO rc:list){

                ButtonBar bar=new ButtonBar();
                Button updateButton=new Button("Update");
                Button deleteButton=new Button("Delete");

                updateButton.setStyle("-fx-background-color: green;-fx-text-fill: white");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white");

                bar.getButtons().addAll(updateButton,deleteButton);

                CustomerTM item=new CustomerTM(
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
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Are you sure?",ButtonType.NO,ButtonType.YES);
                    Optional<ButtonType> buttonType=alert.showAndWait();
                    if(buttonType.get()==ButtonType.YES){
                        try {
                            customerBo.deleteCustomer(rc.getCustomerId());
                            System.out.println(rc.getCustomerId());
                            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted",ButtonType.OK).show();
                            searchAll();
                        }catch (SQLException|ClassNotFoundException e){
                            new Alert(Alert.AlertType.ERROR,"Error: "+e.getMessage(),ButtonType.OK).show();
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

/*
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
*/

}
