package com.example.practicefx;

import java.util.List;

public class CV {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String skills;
    private String workExperience;
    private String projects;
    private String imagePath;
    private List<EducationEntry> educationEntries;

    public CV() {}

    public CV(String name, String email, String phone, String address, 
              String skills, String workExperience, String projects, 
              String imagePath, List<EducationEntry> educationEntries) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.skills = skills;
        this.workExperience = workExperience;
        this.projects = projects;
        this.imagePath = imagePath;
        this.educationEntries = educationEntries;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }
    
    public String getWorkExperience() { return workExperience; }
    public void setWorkExperience(String workExperience) { this.workExperience = workExperience; }
    
    public String getProjects() { return projects; }
    public void setProjects(String projects) { this.projects = projects; }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    
    public List<EducationEntry> getEducationEntries() { return educationEntries; }
    public void setEducationEntries(List<EducationEntry> educationEntries) { this.educationEntries = educationEntries; }
}

