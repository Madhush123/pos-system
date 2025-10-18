package com.devstack.pos.controller;

import com.devstack.pos.db.DatabaseCode;
import com.devstack.pos.model.LoginData;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class LoginFormController {
    public AnchorPane context;
    public TextField txtEmail;
    public PasswordField txtPassword;
    public Alert alert;

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("MainForm");
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        try{
            LoginData loginData = DatabaseCode.loginUser(
                    txtEmail.getText().trim(), txtPassword.getText()
            );
            if(loginData.isStatus()){
                alert=new Alert(Alert.AlertType.INFORMATION,loginData.getMsg());
                alert.initOwner(context.getScene().getWindow());
                alert.showAndWait();
                setUi("DashboardForm");
            }else{
                alert=new Alert(Alert.AlertType.WARNING,loginData.getMsg());
                alert.initOwner(context.getScene().getWindow());
                alert.showAndWait();
            }
        }catch (ClassNotFoundException | SQLException e){
            alert=new Alert(Alert.AlertType.ERROR,"Error Occurred!...(" +e.getMessage()+")");
            alert.initOwner(context.getScene().getWindow());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void navigateToForgotPasswordOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ForgotPasswordForm");
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/devstack/pos/view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage=(Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(load)
        );
    }
}
