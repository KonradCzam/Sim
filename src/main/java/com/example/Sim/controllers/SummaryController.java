package com.example.Sim.controllers;

import com.example.Sim.Config.ScreensConfiguration;
import com.example.Sim.FXML.DialogController;
import com.example.Sim.FXML.FXMLDialog;
import com.example.Sim.Model.Month;
import com.example.Sim.Model.RowData;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.joda.time.Duration;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ResourceBundle;

@Service
public class SummaryController implements Initializable, DialogController {

    private TableView<RowData> table = new TableView<>();
    private ScreensConfiguration screens;
    private FXMLDialog dialog;
    Month currentMonth;
    @FXML
    AnchorPane summaryAnchor;

    public SummaryController(ScreensConfiguration screens) {
        this.screens = screens;
    }

    public void setDialog(FXMLDialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTableColumns();
        ObservableList<RowData> oRowData = FXCollections.observableArrayList(currentMonth.getRowDataList());
        table.setEditable(true);
        table.setItems(oRowData);
        summaryAnchor.getChildren().addAll(table);
    }
    public void initData(Month currentMonth){
        this.currentMonth = currentMonth;
    }

    private void initializeTableColumns() {
        TableColumn<RowData, String> dayColumn = new TableColumn<>("Lp.");
        TableColumn<RowData, String> codeColumn = new TableColumn<RowData, String>("Kod nieobecnosci");
        TableColumn<RowData, String> workHoursColumn = new TableColumn<RowData, String>("Godz. pracy");
        TableColumn<RowData, String> missingDaysColumn = new TableColumn<RowData, String>("Nieob.");
        TableColumn timeWorkedColumn = new TableColumn("Pracy");

        dayColumn.setCellValueFactory(
                new PropertyValueFactory<RowData,String>("lp")
        );
        codeColumn.setCellValueFactory(
                new PropertyValueFactory<RowData,String>("kodNieob")
        );
        workHoursColumn.setCellValueFactory(
                new PropertyValueFactory<RowData,String>("godzPracy")
        );
        timeWorkedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RowData, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RowData, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(formatTime(p.getValue().getIloscGodzPracy()));
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });
        missingDaysColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RowData, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RowData, String> p) {
                if (p.getValue() != null) {
                    return new SimpleStringProperty(formatTime(p.getValue().getIloscGodzNieob()));
                } else {
                    return new SimpleStringProperty("");
                }
            }
        });
        table.getColumns().addAll(dayColumn, codeColumn, workHoursColumn,missingDaysColumn,timeWorkedColumn);
    }
    private String formatTime(Duration duration) {
        if(duration != null) {
            if (duration.equals(Duration.ZERO)) {
                return "";
            }
            String minutes = String.format("%02d", duration.toPeriod().getMinutes());
            String hours = String.format("%02d", duration.toPeriod().getHours());
            return hours + "-" + minutes;
        }else{
            return "";
        }
    }
}
