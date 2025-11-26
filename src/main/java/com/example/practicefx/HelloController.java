package com.example.practicefx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private void onCreateNewCV(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setTitle("CV Builder - Creation Form");
            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            System.err.println("Error loading create-view.fxml:");
            e.printStackTrace();
        }
    }

    @FXML
    private void onViewAllCVs(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cv-list-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            
            CVListController controller = fxmlLoader.getController();
            controller.refreshList();
            
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setTitle("CV Builder - All CVs");
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            System.err.println("Error loading cv-list-view.fxml:");
            e.printStackTrace();
        }
    }
}