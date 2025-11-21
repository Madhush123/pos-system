package com.devstack.pos.dto.response;

import java.util.Date;

public class ResponseOrderDetailDTO {
    private int orderId;
    private String productId;
    private String productName;
    private double unitPrice;
    private int qty;
    private double totalCost;

    public ResponseOrderDetailDTO() {
    }

    public ResponseOrderDetailDTO(int orderId, String productId, String productName, double unitPrice, int qty, double totalCost) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.qty = qty;
        this.totalCost = totalCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
