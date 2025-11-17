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
                new FileChooser.ExtensionFilter("Image Files" }