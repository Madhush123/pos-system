package com.devstack.pos.dto.response;

import java.util.Date;

public class ResponseOrderDTO {
    private int id;
    private String customerId;
    private double totalCost;
    private Date date;

    public ResponseOrderDTO() {
    }

    public ResponseOrderDTO(int id, String customerId, double totalCost, Date date) {
        this.id = id;
        this.customerId = customerId;
        this.totalCost = totalCost;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
