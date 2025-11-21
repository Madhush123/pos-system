package com.devstack.pos.controller;

import com.devstack.pos.bo.BOFactory;
import com.devstack.pos.bo.custom.OrderBO;
import com.devstack.pos.dto.response.ResponseStatisticsDTO;
import com.devstack.pos.util.BOType;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StatisticsFormController {
    public AnchorPane context;
    public DatePicker dpFrom;
    public DatePicker dpTo;
    public AreaChart chart;

    private final OrderBO orderBO=BOFactory.getInstance().getBo(BOType.ORDER);

    public void initialize() {
        setDate();
    }

    private void setDate() {
        LocalDate today = LocalDate.now();
        dpTo.setValue(today);
        dpFrom.setValue(today.minusDays(7));

        loadAllData();
    }

    private void loadAllData() {
        try {
            List<ResponseStatisticsDTO> dtoList = orderBO.loadStatistics(dpFrom.getValue(), dpTo.getValue());

            chart.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Total Sales");
            for (ResponseStatisticsDTO dto : dtoList) {
                series.getData().add(new XYChart.Data<>(String.valueOf(dto.getDate()), dto.getTotal()));
            }
            chart.getData().add(series);


        } catch (SQLException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToScreenOnAction(ActionEvent actionEvent) throws IOException {
        setUi("DashboardForm");
    }

    public void filterOnAction(ActionEvent actionEvent) {
        loadAllData();
    }

    private void setUi(String location) throws IOException {
        URL resource = getClass().getResource("/com/devstack/pos/view/" + location + ".fxml");
        Parent load = FXMLLoader.load(resource);
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(
                new Scene(load)
        );
    }
}
