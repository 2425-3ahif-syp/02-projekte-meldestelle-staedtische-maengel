package com.syp.service;

import com.syp.model.Complaint;
import com.syp.model.Report;
import com.syp.repository.ReportRepository;

import java.util.List;
import java.time.LocalDateTime;

public class ReportService {
    private final ReportRepository reportRepository = new ReportRepository();

    public void saveReport(int complaintId, String reason) {
        if (complaintId <= 0) {
            throw new IllegalArgumentException("BeschwerdenId darf nicht kleiner 0 sein.");
        }

        Report report = new Report(0, complaintId, reason, LocalDateTime.now());
        reportRepository.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
