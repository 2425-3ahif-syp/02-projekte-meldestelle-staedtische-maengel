package com.syp.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Report {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty complaintId = new SimpleIntegerProperty();
    private final StringProperty reason = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> reportTime = new SimpleObjectProperty<>();

    public Report(int id, int complaintId, String reason, LocalDateTime reportTime) {
        setId(id);
        setComplaintId(complaintId);
        setReason(reason);
        setReportTime(reportTime);
    }

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public int getComplaintId() {
        return complaintId.get();
    }
    public IntegerProperty complaintIdProperty() {
        return complaintId;
    }
    public void setComplaintId(int complaintId) {
        this.complaintId.set(complaintId);
    }

    public String getReason() {
        return reason.get();
    }
    public StringProperty reasonProperty() {
        return reason;
    }
    public void setReason(String reason) { this.reason.set(reason); }

    public LocalDateTime getReportTime() {
        return reportTime.get();
    }
    public ObjectProperty<LocalDateTime> reportTimeProperty() {
        return reportTime;
    }
    public void setReportTime(LocalDateTime reportTime) { this.reportTime.set(reportTime); }
}
