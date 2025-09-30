package com.devstack.pos.controller;

import com.devstack.pos.env.StaticResource;
import javafx.scene.control.Label;

public class MainFormController {
    public Label lblCompany;
    public Label lblVersion;

    public void initialize(){
        setStaticData();
    }

    private void setStaticData() {
        lblVersion.setText("Version : "+ StaticResource.getVersion());
        lblVersion.setText("From : "+ StaticResource.getCompany());
    }

}
