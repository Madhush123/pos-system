package com.devstack.pos.dto.response;

import java.util.Date;

public class ResponseStatisticsDTO {
    private Date date;
    private double total;

    public ResponseStatisticsDTO() {
    }

    public ResponseStatisticsDTO(Date date, double total) {
        this.date = date;
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
