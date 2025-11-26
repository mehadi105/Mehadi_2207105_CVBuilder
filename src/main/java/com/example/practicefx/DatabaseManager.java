package com.example.practicefx;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:cv_database.db";
    private static DatabaseManager instance;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DatabaseManager() {
        initializeDatabase();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            String createCVsTable = """
                CREATE TABLE IF NOT EXISTS cvs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    email TEXT NOT NULL,
                    phone TEXT,
                    address TEXT,
                    skills TEXT,
                    work_experience TEXT,
                    projects TEXT,
                    image_path TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            stmt.execute(createCVsTable);

            String createEducationTable = """
                CREATE TABLE IF NOT EXISTS education (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cv_id INTEGER NOT NULL,
                    examination TEXT,
                    institution TEXT,
                    major TEXT,
                    year TEXT,
                    grade TEXT,
                    FOREIGN KEY (cv_id) REFERENCES cvs(id) ON DELETE CASCADE
                )
            """;
            stmt.execute(createEducationTable);
            
            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }

    public int insertCV(CV cv) {
        String sql = """
            INSERT INTO cvs (name, email, phone, address, skills, work_experience, projects, image_path)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cv.getName());
            pstmt.setString(2, cv.getEmail());
            pstmt.setString(3, cv.getPhone());
            pstmt.setString(4, cv.getAddress());
            pstmt.setString(5, cv.getSkills());
            pstmt.setString(6, cv.getWorkExperience());
            pstmt.setString(7, cv.getProjects());
            pstmt.setString(8, cv.getImagePath());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        int cvId = rs.getInt(1);
                        
                        if (cv.getEducationEntries() != null && !cv.getEducationEntries().isEmpty()) {
                            insertEducationEntries(cvId, cv.getEducationEntries());
                        }
                        
                        return cvId;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting CV: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error inserting CV: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
        return -1;
    }

    private void insertEducationEntries(int cvId, List<EducationEntry> entries) {
        String sql = """
            INSERT INTO education (cv_id, examination, institution, major, year, grade)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (EducationEntry entry : entries) {
                if (entry.getExamination() != null && !entry.getExamination().trim().isEmpty()) {
                    pstmt.setInt(1, cvId);
                    pstmt.setString(2, entry.getExamination());
                    pstmt.setString(3, entry.getInstitution());
                    pstmt.setString(4, entry.getMajor());
                    pstmt.setString(5, entry.getYear());
                    pstmt.setString(6, entry.getGrade());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting education entries: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<CV> getAllCVs() {
        List<CV> cvs = new ArrayList<>();
        String sql = "SELECT * FROM cvs ORDER BY created_at DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                CV cv = new CV();
                cv.setId(rs.getInt("id"));
                cv.setName(rs.getString("name"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setAddress(rs.getString("address"));
                cv.setSkills(rs.getString("skills"));
                cv.setWorkExperience(rs.getString("work_experience"));
                cv.setProjects(rs.getString("projects"));
                cv.setImagePath(rs.getString("image_path"));
                
                cv.setEducationEntries(getEducationEntriesByCvId(cv.getId()));
                
                cvs.add(cv);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all CVs: " + e.getMessage());
            e.printStackTrace();
        }
        return cvs;
    }

    public CV getCVById(int id) {
        String sql = "SELECT * FROM cvs WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                CV cv = new CV();
                cv.setId(rs.getInt("id"));
                cv.setName(rs.getString("name"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setAddress(rs.getString("address"));
                cv.setSkills(rs.getString("skills"));
                cv.setWorkExperience(rs.getString("work_experience"));
                cv.setProjects(rs.getString("projects"));
                cv.setImagePath(rs.getString("image_path"));
                cv.setEducationEntries(getEducationEntriesByCvId(cv.getId()));
                return cv;
            }
        } catch (SQLException e) {
            System.err.println("Error getting CV by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private List<EducationEntry> getEducationEntriesByCvId(int cvId) {
        List<EducationEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM education WHERE cv_id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cvId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                EducationEntry entry = new EducationEntry();
                entry.setId(rs.getInt("id"));
                entry.setCvId(rs.getInt("cv_id"));
                entry.setExamination(rs.getString("examination"));
                entry.setInstitution(rs.getString("institution"));
                entry.setMajor(rs.getString("major"));
                entry.setYear(rs.getString("year"));
                entry.setGrade(rs.getString("grade"));
                entries.add(entry);
            }
        } catch (SQLException e) {
            System.err.println("Error getting education entries: " + e.getMessage());
            e.printStackTrace();
        }
        return entries;
    }

    public boolean updateCV(CV cv) {
        String sql = """
            UPDATE cvs 
            SET name = ?, email = ?, phone = ?, address = ?, 
                skills = ?, work_experience = ?, projects = ?, image_path = ?
            WHERE id = ?
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cv.getName());
            pstmt.setString(2, cv.getEmail());
            pstmt.setString(3, cv.getPhone());
            pstmt.setString(4, cv.getAddress());
            pstmt.setString(5, cv.getSkills());
            pstmt.setString(6, cv.getWorkExperience());
            pstmt.setString(7, cv.getProjects());
            pstmt.setString(8, cv.getImagePath());
            pstmt.setInt(9, cv.getId());
            
            deleteEducationEntriesByCvId(cv.getId());
            
            if (cv.getEducationEntries() != null) {
                insertEducationEntries(cv.getId(), cv.getEducationEntries());
            }
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating CV: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCV(int id) {
        String sql = "DELETE FROM cvs WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting CV: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void deleteEducationEntriesByCvId(int cvId) {
        String sql = "DELETE FROM education WHERE cv_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cvId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting education entries: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

