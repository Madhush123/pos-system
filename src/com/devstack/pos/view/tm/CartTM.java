package com.devstack.pos.view.tm;

import javafx.scene.control.Button;

public class CartTM {
    private String id;
    private String description;
    private double unitePrice;
    private int qty;
    private double total;
    Button btn;

    public CartTM() {
    }

    public CartTM(String id, String description, double unitePrice, int qty, double total, Button btn) {
        this.id = id;
        this.description = description;
        this.unitePrice = unitePrice;
        this.qty = qty;
        this.total = total;
        this.btn = btn;
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
        return unitePrice;
    }

    public void setUnitPrice(double unite_price) {
        this.unitePrice = unite_price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
