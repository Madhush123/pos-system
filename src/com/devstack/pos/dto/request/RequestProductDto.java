package com.devstack.pos.dto.request;

public class RequestProductDto {
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private byte[] qr;

    public RequestProductDto() {
    }

    public RequestProductDto(String description, double unitPrice, int qtyOnHand, byte[] qr) {
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.qr = qr;
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

    public byte[] getQr() {
        return qr;
    }

    public void setQr(byte[] qr) {
        this.qr = qr;
    }
}
