package com.devstack.pos.dto.request;

public class RequestCustomerDTO {
    private String name;
    private String address;
    private double salary;

    public RequestCustomerDTO(String name, String address, double salary) {

        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public RequestCustomerDTO() {
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
