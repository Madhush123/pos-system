package com.devstack.pos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class LoginFormController {
    public AnchorPane context;

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("MainForm");
    }

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
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
