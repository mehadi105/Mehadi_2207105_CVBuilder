package com.example.practicefx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

public class PreviewController {

    @FXML private BorderPane previewRoot;
    @FXML private Label mainTitleLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private ImageView profileImageView;
    @FXML private Label photoPlaceholder;
    @FXML private VBox skillsContainer;
    @FXML private VBox workContainer;
    @FXML private VBox educationContainer;
    @FXML private VBox projectsContainer;

    public void setCVData(String name, String email, String phone, String address,
                          File imageFile, String skills, String work, String projects,
                          List<String[]> educationData) {

        nameLabel.setText(name.isEmpty() ? "[FULL NAME]" : name.toUpperCase());
        emailLabel.setText(email.isEmpty() ? "Email: [Email Address]" : "Email: " + email);
        phoneLabel.setText(phone.isEmpty() ? "Phone: [Phone Number]" : "Phone: " + phone);
        addressLabel.setText(address.isEmpty() ? "Address: [Address]" : "Address: " + address);

        mainTitleLabel.setText("Curriculum Vitae of " + (name.isEmpty() ? "[Full Name]" : name));

        if (imageFile != null && imageFile.exists()) {
            try {
                profileImageView.setImage(new Image(imageFile.toURI().toString()));
                photoPlaceholder.setVisible(false);
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                photoPlaceholder.setVisible(true);
            }
        } else {
            photoPlaceholder.setVisible(true);
        }

        skillsContainer.getChildren().clear();
        if (skills != null && !skills.trim().isEmpty()) {
            String[] skillLines = skills.split("\n");
            for (String skillLine : skillLines) {
                if (!skillLine.trim().isEmpty()) {
                    String[] skillItems = skillLine.split(",");
                    for (String skill : skillItems) {
                        String trimmedSkill = skill.trim();
                        if (!trimmedSkill.isEmpty()) {
                            Label skillLabel = new Label("• " + trimmedSkill);
                            skillLabel.setStyle("-fx-text-fill: #1E90FF; -fx-font-size: 14;");
                            skillsContainer.getChildren().add(skillLabel);
                        }
                    }
                }
            }
        }

        workContainer.getChildren().clear();
        if (work != null && !work.trim().isEmpty()) {
            String[] workLines = work.split("\n");
            for (String workLine : workLines) {
                if (!workLine.trim().isEmpty()) {
                    String[] workItems = workLine.split(",");
                    for (String workItem : workItems) {
                        String trimmedWork = workItem.trim();
                        if (!trimmedWork.isEmpty()) {
                            Label workLabel = new Label("• " + trimmedWork);
                            workLabel.setStyle("-fx-text-fill: #1E90FF; -fx-font-size: 14;");
                            workLabel.setWrapText(true);
                            workContainer.getChildren().add(workLabel);
                        }
                    }
                }
            }
        }

        educationContainer.getChildren().clear();
        if (educationData != null) {
            for (String[] edu : educationData) {
                if (edu != null && edu.length >= 5) {
                    String exam = edu[0] != null ? edu[0].trim() : "";
                    String inst = edu[1] != null ? edu[1].trim() : "";
                    String major = edu[2] != null ? edu[2].trim() : "";
                    String year = edu[3] != null ? edu[3].trim() : "";
                    String grade = edu[4] != null ? edu[4].trim() : "";

                    if (!exam.isEmpty() || !inst.isEmpty() || !major.isEmpty() || !year.isEmpty() || !grade.isEmpty()) {
                        StringBuilder eduText = new StringBuilder();
                        if (!exam.isEmpty()) eduText.append(exam);
                        if (!inst.isEmpty()) {
                            if (eduText.length() > 0) eduText.append(" - ");
                            eduText.append(inst);
                        }
                        if (!major.isEmpty()) {
                            if (eduText.length() > 0) eduText.append(", ");
                            eduText.append(major);
                        }
                        if (!year.isEmpty()) {
                            if (eduText.length() > 0) eduText.append(" (");
                            else eduText.append("(");
                            eduText.append(year);
                            eduText.append(")");
                        }
                        if (!grade.isEmpty()) {
                            if (eduText.length() > 0) eduText.append(" - Grade: ");
                            else eduText.append("Grade: ");
                            eduText.append(grade);
                        }

                        Label eduLabel = new Label(eduText.toString());
                        eduLabel.setStyle("-fx-text-fill: #1E90FF; -fx-font-size: 14;");
                        eduLabel.setWrapText(true);
                        educationContainer.getChildren().add(eduLabel);
                    }
                }
            }
        }

        projectsContainer.getChildren().clear();
        if (projects != null && !projects.trim().isEmpty()) {
            String[] projectLines = projects.split("\n");
            for (String projectLine : projectLines) {
                if (!projectLine.trim().isEmpty()) {
                    String[] projectItems = projectLine.split(",");
                    for (String project : projectItems) {
                        String trimmedProject = project.trim();
                        if (!trimmedProject.isEmpty()) {
                            Label projectLabel = new Label("• " + trimmedProject);
                            projectLabel.setStyle("-fx-text-fill: #1E90FF; -fx-font-size: 14;");
                            projectLabel.setWrapText(true);
                            projectsContainer.getChildren().add(projectLabel);
                        }
                    }
                }
            }
        }
    }
}
