package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.dto.request.RequestUserDTO;
import com.devstack.pos.util.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class RegisterFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public TextField txtDisplayName;
    public TextField txtContactNumber;
    public PasswordField txtPassword;
    public Alert alert;

    private UserBo userBo= BoFactory.getInstance().getBo(BoType.USER);

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("MainForm");
    }

    public void registerOnAction(ActionEvent actionEvent) throws IOException {
        RequestUserDTO user = new RequestUserDTO(
                txtEmail.getText().trim(),
                txtDisplayName.getText().trim(),
                txtContactNumber.getText().trim(),
                txtPassword.getText().trim()
        );
        try{
            boolean isSaved = userBo.registerUser(user);
            System.out.println(isSaved);
            if(isSaved){
                showAlert(Alert.AlertType.INFORMATION,String.format("User Saved %s",user.getDisplayName()),ButtonType.OK);
                setUi("LoginForm");
            }else{
                showAlert(Alert.AlertType.WARNING,"Try Again!",ButtonType.OK);
            }
        }catch (ClassNotFoundException | SQLException e){
            showAlert(Alert.AlertType.ERROR,"User already exists!",ButtonType.OK);
            e.printStackTrace();
        }
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/devstack/pos/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage=(Stage) context.getScene().getWindow();
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
