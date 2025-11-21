package com.devstack.pos.controller;

import com.devstack.pos.bo.BOFactory;
import com.devstack.pos.bo.custom.CustomerBO;
import com.devstack.pos.bo.custom.OrderBO;
import com.devstack.pos.bo.custom.OrderDetailBO;
import com.devstack.pos.dto.response.ResponseCustomerDTO;
import com.devstack.pos.dto.response.ResponseOrderDTO;
import com.devstack.pos.dto.response.ResponseOrderDetailDTO;
import com.devstack.pos.util.BOType;
import com.devstack.pos.view.tm.OrderDetailTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class OrderDetailsFormController {
    public AnchorPane context;
    public ListView<String> lstDetails;
    public TableView<OrderDetailTM> tblDetails;
    public TableColumn<OrderDetailTM, Integer> colId;
    public TableColumn<OrderDetailTM, String> colDescription;
    public TableColumn<OrderDetailTM, Double> colUnitPrice;
    public TableColumn<OrderDetailTM, Integer> colQty;
    public TableColumn<OrderDetailTM, Double> colTotal;

    private final OrderBO orderBo= BOFactory.getInstance().getBo(BOType.ORDER);
    private final CustomerBO customerBo= BOFactory.getInstance().getBo(BOType.CUSTOMER);
    private final OrderDetailBO orderDetailBO= BOFactory.getInstance().getBo(BOType.ORDER_DETAILS);

    private int orderId=0;
    private double totalCost=0.0;

   public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    public void setData(int orderId)  {
        this.orderId=orderId;
        try {
            ResponseOrderDTO selectedOrder=orderBo.findOrderById(orderId);
            if(selectedOrder==null) {
                new Alert(Alert.AlertType.WARNING,"Order not found!..",ButtonType.OK).show();
                return;
            }
            ResponseCustomerDTO selectedCustomer=customerBo.getCustomerById(selectedOrder.getCustomerId());
          /*  if(selectedCustomer==null) {
               new Alert(Alert.AlertType.WARNING,"Customer not found!..",ButtonType.OK).show();
                return;
            }*/

            List<ResponseOrderDetailDTO> orderDetails = orderDetailBO.getOrderDetailsByOrderId(orderId);
            ObservableList<OrderDetailTM> tms= FXCollections.observableArrayList();
            int id=0;
            for(ResponseOrderDetailDTO orderDetail:orderDetails) {
                tms.add(new OrderDetailTM(
                   ++id,
                   orderDetail.getProductName(),
                   orderDetail.getUnitPrice(),
                   orderDetail.getQty(),
                   orderDetail.getTotalCost()
                ));
            }
            tblDetails.setItems(tms);
            String customer="Customer Name: UNKNOWN";
            String address="Customer Address: UNKNOWN";
            String oId="Order Id: #"+orderId;
            totalCost=selectedOrder.getTotalCost();
            if(selectedCustomer!=null) {
                customer="Customer Name: "+selectedCustomer.getName();
                address="Customer Address: "+selectedCustomer.getAddress();
            }

            String date="Date: "+selectedOrder.getDate();
            String cost="Total Cost: "+totalCost;
            String itemCount="Item Count: "+orderDetails.size()+" Items";
            System.out.println(customer);

            lstDetails.getItems().addAll(oId,customer,address,date,cost,itemCount);

        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void printBillOnAction(ActionEvent actionEvent) {
// Get order details from table
        ObservableList<OrderDetailTM> items = tblDetails.getItems();
        if (items.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No order details to print!", ButtonType.OK).show();
            return;
        }

        // Extract info from list view (customer, date, cost, etc.)
        String orderid = lstDetails.getItems().get(0);
        String customer =lstDetails.getItems().get(1);
        String address  =lstDetails.getItems().get(2);
        String date     =lstDetails.getItems().get(3);

        // Build bill layout
        VBox billPane = new VBox(5);
        billPane.setPadding(new Insets(10));
        billPane.getChildren().addAll(
                new Label("POS SYSTEM BILL"),
                new Label(orderid),
                new Label(customer),
                new Label(address),
                new Label(date),
                new Label("--------------------------------------------------------------------")
        );

        // Add table items to bill
        for (OrderDetailTM item : items) {
            billPane.getChildren().add(new Label(
                    item.getDescription() + " | Qty: " + item.getQty() +
                            " | Unit Price: " + String.format("%.2f", item.getUnitPrice()) +
                            " | Total: " + String.format("%.2f", item.getTotalCost())
            ));
        }


        billPane.getChildren().addAll(
                new Label("--------------------------------------------------------------------"),
                new Label("TOTAL: " + String.format("%.2f",totalCost)));


        // Use default printer / PDF printer
        Printer pdfPrinter = null;
        for (Printer p : Printer.getAllPrinters()) {
            if (p.getName().toLowerCase().contains("pdf")) {
                pdfPrinter = p;
                break;
            }
        }

        if (pdfPrinter == null) {
            new Alert(Alert.AlertType.ERROR, "No PDF printer found!").show();
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.setPrinter(pdfPrinter);

            PageLayout pageLayout = pdfPrinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            job.getJobSettings().setPageLayout(pageLayout);

            // Automatically save PDF to desktop with order ID
            job.getJobSettings().setJobName("Order_" + orderId + ".pdf");

            if (job.printPage(billPane)) {
                job.endJob();
                new Alert(Alert.AlertType.INFORMATION, "Bill saved as PDF successfully!").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save PDF!").show();
            }
        }
    }

}
