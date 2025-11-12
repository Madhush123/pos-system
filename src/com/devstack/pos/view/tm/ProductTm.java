package com.devstack.pos.view.tm;

import javafx.scene.control.ButtonBar;

public class ProductTm {
    private String id;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private String qrAvailability;
    private ButtonBar tools;

    public ProductTm() {
    }

    public ProductTm(String id, String description, double unitPrice, int qtyOnHand, String qrAvailability, ButtonBar tools) {
        this.id = id;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.qrAvailability = qrAvailability;
        this.tools = tools;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getQrAvailability() {
        return qrAvailability;
    }

    public void setQrAvailability(String qrAvailability) {
        this.qrAvailability = qrAvailability;
    }

    public ButtonBar getTools() {
        return tools;
    }

    public void setTools(ButtonBar tools) {
        this.tools = tools;
    }
}
