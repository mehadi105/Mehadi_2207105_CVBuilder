package com.example.practicefx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CVListController {

    @FXML private TableView<CV> cvTableView;
    @FXML private TableColumn<CV, Integer> idColumn;
    @FXML private TableColumn<CV, String> nameColumn;
    @FXML private TableColumn<CV, String> emailColumn;
    @FXML private Button viewButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button refreshButton;

    private ObservableList<CV> cvList = FXCollections.observableArrayList();
    private DatabaseManager dbManager;

    @FXML
    public void initialize() {
        dbManager = DatabaseManager.getInstance();
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        cvTableView.setItems(cvList);
        loadCVs();
        
        viewButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        
        cvTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                viewButton.setDisable(!hasSelection);
                editButton.setDisable(!hasSelection);
                deleteButton.setDisable(!hasSelection);
            }
        );
    }

    @FXML
    private void onRefresh(ActionEvent event) {
        loadCVs();
    }

    @FXML
    private void onView(ActionEvent event) {
        CV selectedCV = cvTableView.getSelectionModel().getSelectedItem();
        if (selectedCV != null) {
            openPreview(selectedCV);
        }
    }

    @FXML
    private void onEdit(ActionEvent event) {
        CV selectedCV = cvTableView.getSelectionModel().getSelectedItem();
        if (selectedCV != null) {
            openEditView(selectedCV);
        }
    }

    @FXML
    private void onDelete(ActionEvent event) {
        CV selectedCV = cvTableView.getSelectionModel().getSelectedItem();
        if (selectedCV != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Delete");
            confirmAlert.setHeaderText("Delete CV");
            confirmAlert.setContentText("Are you sure you want to delete " + selectedCV.getName() + "'s CV?");
            
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (dbManager.deleteCV(selectedCV.getId())) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("CV deleted successfully!");
                        successAlert.showAndWait();
                        loadCVs();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Failed to delete CV.");
                        errorAlert.showAndWait();
                    }
                }
            });
        }
    }

    @FXML
    private void onBack(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setTitle("CV Builder");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void refreshList() {
        loadCVs();
    }

    private void loadCVs() {
        cvList.clear();
        List<CV> cvs = dbManager.getAllCVs();
        cvList.addAll(cvs);
    }

    private void openPreview(CV cv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("preview-view.fxml"));
            Stage previewStage = new Stage();
            previewStage.setTitle("CV Preview - " + cv.getName());
            previewStage.setScene(new Scene(loader.load(), 850, 1150));

            PreviewController previewController = loader.getController();
            
            List<String[]> educationData = new ArrayList<>();
            if (cv.getEducationEntries() != null) {
                for (EducationEntry entry : cv.getEducationEntries()) {
                    educationData.add(new String[]{
                        entry.getExamination(),
                        entry.getInstitution(),
                        entry.getMajor(),
                        entry.getYear(),
                        entry.getGrade()
                    });
                }
            }
            
            File imageFile = cv.getImagePath() != null ? 
                new File(cv.getImagePath()) : null;

            previewController.setCVData(
                cv.getName(),
                cv.getEmail(),
                cv.getPhone(),
                cv.getAddress(),
                imageFile,
                cv.getSkills(),
                cv.getWorkExperience(),
                cv.getProjects(),
                educationData
            );

            previewStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditView(CV cv) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-view.fxml"));
            Stage editStage = new Stage();
            editStage.setTitle("Edit CV - " + cv.getName());
            editStage.setScene(new Scene(loader.load(), 900, 950));

            CreateController editController = loader.getController();
            editController.loadCVData(cv);

            editStage.setOnHidden(e -> loadCVs());
            
            editStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

