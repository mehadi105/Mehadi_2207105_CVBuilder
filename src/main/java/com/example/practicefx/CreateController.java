package com.example.practicefx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateController {

    @FXML private TextField inputName;
    @FXML private TextField inputEmail;
    @FXML private TextArea inputNumber;
    @FXML private TextArea inputAdd;

    @FXML private TextField exam1Field, inst1Field, major1Field, year1Field, grade1Field;
    @FXML private TextField exam2Field, inst2Field, major2Field, year2Field, grade2Field;
    @FXML private TextField exam3Field, inst3Field, major3Field, year3Field, grade3Field;
    @FXML private TextField exam4Field, inst4Field, major4Field, year4Field, grade4Field;

    @FXML private TextArea inputSkills;
    @FXML private TextArea inputWork;
    @FXML private TextArea inputProject;

    @FXML private ImageView profileImageView;
    private File selectedImageFile;

    @FXML
    private void onChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImageFile = file;
            profileImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void OnbtnClkBuildCv(ActionEvent event) {
        if (validateAllFields()) {
            launchPreview();
        }
    }

    private boolean validateAllFields() {
        if (inputName.getText() == null || inputName.getText().trim().isEmpty() ||
            inputEmail.getText() == null || inputEmail.getText().trim().isEmpty() ||
            inputNumber.getText() == null || inputNumber.getText().trim().isEmpty() ||
            inputAdd.getText() == null || inputAdd.getText().trim().isEmpty()) {
            showValidationError();
            return false;
        }

        TextField[][] educationRows = {
            {exam1Field, inst1Field, major1Field, year1Field, grade1Field},
            {exam2Field, inst2Field, major2Field, year2Field, grade2Field},
            {exam3Field, inst3Field, major3Field, year3Field, grade3Field},
            {exam4Field, inst4Field, major4Field, year4Field, grade4Field}
        };

        boolean hasEducation = false;
        for (TextField[] row : educationRows) {
            boolean rowComplete = true;
            for (TextField field : row) {
                if (field.getText() == null || field.getText().trim().isEmpty()) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                hasEducation = true;
                break;
            }
        }

        if (!hasEducation ||
            inputSkills.getText() == null || inputSkills.getText().trim().isEmpty() ||
            inputWork.getText() == null || inputWork.getText().trim().isEmpty() ||
            inputProject.getText() == null || inputProject.getText().trim().isEmpty() ||
            selectedImageFile == null) {
            showValidationError();
            return false;
        }

        return true;
    }

    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all the entries.");
        alert.showAndWait();
    }

    private void launchPreview() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("preview-view.fxml"));
            Stage previewStage = new Stage();
            previewStage.setTitle("CV Preview");
            previewStage.setScene(new Scene(loader.load(), 850, 1150));

            PreviewController previewController = loader.getController();

            List<String[]> educationData = new ArrayList<>();
            educationData.add(new String[]{exam1Field.getText(), inst1Field.getText(), major1Field.getText(), year1Field.getText(), grade1Field.getText()});
            educationData.add(new String[]{exam2Field.getText(), inst2Field.getText(), major2Field.getText(), year2Field.getText(), grade2Field.getText()});
            educationData.add(new String[]{exam3Field.getText(), inst3Field.getText(), major3Field.getText(), year3Field.getText(), grade3Field.getText()});
            educationData.add(new String[]{exam4Field.getText(), inst4Field.getText(), major4Field.getText(), year4Field.getText(), grade4Field.getText()});

            previewController.setCVData(
                    inputName.getText(),
                    inputEmail.getText(),
                    inputNumber.getText(),
                    inputAdd.getText(),
                    selectedImageFile,
                    inputSkills.getText(),
                    inputWork.getText(),
                    inputProject.getText(),
                    educationData
            );

            previewStage.show();

        } catch (IOException e) {
            System.err.println("Error launching preview-view.fxml:");
            e.printStackTrace();
        }
    }
}