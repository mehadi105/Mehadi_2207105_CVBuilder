package com.example.practicefx;

public class EducationEntry {
    private int id;
    private int cvId;
    private String examination;
    private String institution;
    private String major;
    private String year;
    private String grade;

    public EducationEntry() {}

    public EducationEntry(String examination, String institution, String major, String year, String grade) {
        this.examination = examination;
        this.institution = institution;
        this.major = major;
        this.year = year;
        this.grade = grade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCvId() { return cvId; }
    public void setCvId(int cvId) { this.cvId = cvId; }
    
    public String getExamination() { return examination; }
    public void setExamination(String examination) { this.examination = examination; }
    
    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}

