package com.devstack.pos.view.tm;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;

public class ProductTM {
    private long id;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private Button qrAvailability;
    private ButtonBar tools;

    public ProductTM() {
    }

    public ProductTM(long id, String description, double unitPrice, int qtyOnHand, Button qrAvailability, ButtonBar tools) {
        this.id = id;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.qrAvailability = qrAvailability;
        this.tools = tools;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public Button getQrAvailability() {
        return qrAvailability;
    }

    public void setQrAvailability(Button qrAvailability) {
        this.qrAvailability = qrAvailability;
    }

    public ButtonBar getTools() {
        return tools;
    }

    public void setTools(ButtonBar tools) {
        this.tools = tools;
    }
}
