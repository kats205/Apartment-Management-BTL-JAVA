package com.utc2.apartmentmanagement.Model;

import java.time.LocalDate;

public class Report {
    private int id;
    private String reportType;
    private LocalDate generationDate;
    private int generatedByUserId;
    private String parameters;
    private String filePath;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Report() {
        // Constructor mặc định
    }

    public Report(int id, String reportType, LocalDate generationDate, int generatedByUserId, String parameters,
                    String filePath, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.reportType = reportType;
        this.generationDate = generationDate;
        this.generatedByUserId = generatedByUserId;
        this.parameters = parameters;
        this.filePath = filePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(LocalDate generationDate) {
        this.generationDate = generationDate;
    }

    public int getGeneratedByUserId() {
        return generatedByUserId;
    }

    public void setGeneratedByUserId(int generatedByUserId) {
        this.generatedByUserId = generatedByUserId;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", reportType='" + reportType + '\'' +
                ", generationDate=" + generationDate +
                ", generatedByUserId=" + generatedByUserId +
                ", parameters='" + parameters + '\'' +
                ", filePath='" + filePath + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}