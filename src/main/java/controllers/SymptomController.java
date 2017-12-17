package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import models.Patient;
import models.Symptom;

import java.io.IOException;

public class SymptomController {

    private DBController db = new DBController();
    private Patient patient;

    @FXML
    private Pane symtomPane;
    @FXML
    private TextField dateField;
    @FXML
    private TextArea infoField;
    @FXML
    private TextField staffIDField;
    @FXML
    private TextField roomField;
    @FXML
    private Label patientLabel;

    @FXML
    public void initialize() {
        Platform.runLater(new Runnable() {
            public void run() {
                patientLabel.setText(patient.getPatientID() + ": " + patient.getFullName());
            }
        });
    }

    @FXML
    public void backBtnHandle() {
        Stage stage = (Stage) symtomPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PatientRecordPageFMRP.fxml"));
        try {
            stage.setScene(new Scene((Parent) loader.load(),1000, 800));
            PatientRecordFMRPController controller = loader.getController();
            controller.setPatient(patient);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void submitBtnHandle()  {
        Symptom symptom = new Symptom(dateField.getText(), infoField.getText(), patient.getPatientID(), Integer.parseInt(staffIDField.getText()));
        db.insertSymptom(symptom);
        db.updateStatus(roomField.getText(), patient.getPatientID());
        patient.addSymptom(symptom);
        changeScene("/MedicalRecordsPage.fxml", 1000, 800);
    }

    public void changeScene(String scene, int w, int h) {
        Stage stage = (Stage) symtomPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(scene));
        try {
            stage.setScene(new Scene((Parent) loader.load(),w, h));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}