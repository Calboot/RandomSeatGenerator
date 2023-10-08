package com.edp2021c1.ui.controller;

import com.alibaba.excel.EasyExcel;
import com.edp2021c1.Main;
import com.edp2021c1.core.Seat;
import com.edp2021c1.core.SeatManager;
import com.edp2021c1.data.SeatRowData;
import com.edp2021c1.data.SeatRowData_Old;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class MainWindowController {

    @FXML
    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField seedInput;

    @FXML
    private TableView<SeatRowData> seatTable;

    private static Seat seat;

    @FXML
    void dateAsSeed(ActionEvent event) {
        Date t = new Date();
        seedInput.setText(String.format("%tY%tm%td%tH%tM%tS", t, t, t, t, t, t));
    }

    @FXML
    void exportSeatTable(ActionEvent event) throws Exception {
        if(seat==null) generateSeatTable(null);
        FileChooser fc=new FileChooser();
        fc.setTitle("导出座位表");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel 工作薄","*.xlsx"));
        File f=fc.showSaveDialog(stage);
        Date date=new Date();
        if(f==null) {
            return;
        } else if(!f.createNewFile()) {
            f.delete();
            f.createNewFile();
        }
        EasyExcel.write(f, SeatRowData.class).sheet(String.format("座位表-%tF", date)).doWrite(SeatRowData.fromSeat(seat));
    }

    @FXML
    void generateRandomSeed(ActionEvent event) {
        seedInput.setText(Long.toString(new Random().nextLong()));
    }

    @FXML
    void generateSeatTable(ActionEvent event) throws Exception {
        initSeatTable();
        long seed;
        try {
            Long.parseLong(seedInput.getText());
        } catch (NumberFormatException e) {
            generateRandomSeed(null);
        }
        seed = Long.parseLong(seedInput.getText());
        SeatManager.config = Main.seatConfig;
        seat = SeatManager.generate(seed);
        seatTable.setItems(FXCollections.observableArrayList(SeatRowData.fromSeat(seat)));
    }

    @FXML
    void openPreferencesDialog(ActionEvent event) throws IOException {
        Stage s = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/assets/fxml/dialog/PreferencesDialog.fxml")));
        s.initOwner(stage);
        s.show();
    }

    @FXML
    void initialize() throws Exception {
        initSeatTable();
    }

    void initSeatTable() throws Exception {
        seatTable.getColumns().clear();
        int rowCount = Main.seatConfig.getRowCount(), columnCount = Main.seatConfig.getColumnCount();
        TableColumn<SeatRowData, String> c;
        double d = 1d / columnCount;
        for (int i = 0; i < columnCount; i++) {
            c = new TableColumn<>("C" + (i + 1)) {{
                prefWidthProperty().bind(seatTable.widthProperty().multiply(d));
            }};
            c.setCellValueFactory(new PropertyValueFactory<>("c" + (i + 1)));
            c.setSortable(false);
            seatTable.getColumns().add(c);
        }
        seatTable.setEditable(false);
        seatTable.setItems(FXCollections.observableArrayList(SeatRowData.emptySeat(rowCount, columnCount)));
    }

}
