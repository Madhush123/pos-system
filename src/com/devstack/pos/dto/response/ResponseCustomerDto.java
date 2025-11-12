package com.devstack.pos.dto.response;

public class ResponseCustomerDto {
    private String CustomerId;
    private String name;
    private String address;
    private double salary;

    public ResponseCustomerDto(String CustomerId, String name, String address, double salary) {
        this.CustomerId = CustomerId;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public ResponseCustomerDto() {
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getSalary() {
        return salary;
    }
}
