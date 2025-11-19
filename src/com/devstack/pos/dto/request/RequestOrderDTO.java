package com.devstack.pos.dto.request;

import java.util.Date;
import java.util.List;

public class RequestOrderDTO {
    private int orderId;
    private String customerId;
    private double totalCost;
    private Date date;
    private List<RequestOrderDetailsDTO> orderDetailsDTOList;

    public RequestOrderDTO() {
    }

    public RequestOrderDTO(int orderId, String customerId, double totalCost, Date date, List<RequestOrderDetailsDTO> orderDetailsDTOList) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.totalCost = totalCost;
        this.date = date;
        this.orderDetailsDTOList = orderDetailsDTOList;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public List<RequestOrderDetailsDTO> getOrderDetailsDTOList() {
        return orderDetailsDTOList;
    }

    public void setOrderDetailsDTOList(List<RequestOrderDetailsDTO> orderDetailsDTOList) {
        this.orderDetailsDTOList = orderDetailsDTOList;
    }
}
