package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Patient;
import models.Result;

import java.io.IOException;
import java.util.ArrayList;

public class PatientRecordFMRPController {

    private DBController db = new DBController();
    private Patient patient;
    private ArrayList<Result> results;

    @FXML
    private Pane patientRecordFMRPPane;
    @FXML
    private TableView<Result> resultTable;
    @FXML
    private TableColumn<Result, Integer> resultIDColumn;
    @FXML
    private TableColumn<Result, String> dateColumn, infoColumn;
    @FXML
    private Label patientLabel;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            public void run() {
                patientLabel.setText(patient.getPatientID() + ": " +  patient.getFullName());
                ObservableList<Result> list = FXCollections.observableArrayList();
                results = db.selectResults(patient.getPatientID());
                for (int i = results.size() - 1; i >= 0; i--) {
                    list.add(results.get(i));
                }
                resultIDColumn.setCellValueFactory(new PropertyValueFactory<Result, Integer>("resultID"));
                dateColumn.setCellValueFactory(new PropertyValueFactory<Result, String>("noteDate"));
                infoColumn.setCellValueFactory(new PropertyValueFactory<Result, String>("resultInfo"));
                resultTable.setItems(list);
            }
        });
        resultTable.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                onClickedResultRecord();
            }
        });
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @FXML
    public void backBtnHandle() {
        changeScene("/MedicalRecordsPage.fxml", 1000, 800);
    }

    @FXML
    public void examBtnHandle()  {
        Stage stage = (Stage) patientRecordFMRPPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SymptomPage.fxml"));
        try {
            stage.setScene(new Scene((Parent) loader.load(),650, 500));
            SymptomController controller = loader.getController();
            controller.setPatient(patient);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeScene(String scene, int w, int h) {
        Stage stage = (Stage) patientRecordFMRPPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scene));
        try {
            stage.setScene(new Scene((Parent) loader.load(),w, h));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onClickedResultRecord() {
        ObservableList<Result> resultSelected, allResults;
        allResults = resultTable.getItems();
        resultSelected = resultTable.getSelectionModel().getSelectedItems();
        popUp(patient, results.get(allResults.indexOf(resultSelected.get(0))));
    }

    private void popUp(Patient patient, Result result) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ExaminationResultPageFERP.fxml"));
        try {
            stage.setScene(new Scene((Parent) loader.load()));
            ExaminationResultFERPController controller = loader.getController();
            controller.setResult(patient, result);
            stage.initOwner(patientRecordFMRPPane.getScene().getWindow());
            stage.setTitle("Examination Result");
            stage.initModality(Modality.NONE);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}