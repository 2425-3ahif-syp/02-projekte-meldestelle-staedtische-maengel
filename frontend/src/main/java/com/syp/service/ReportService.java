package com.syp.service;

import com.syp.model.Report;
import com.syp.repository.ReportRepository;

import java.time.LocalDateTime;

public class ReportService {
    private final ReportRepository reportRepository = new ReportRepository();

    public void createReport(int complaintId, String reason) {
        if (complaintId <= 0 || reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("BeschwerdenId und Grund sind Pflichtfelder.");
        }

        Report report = new Report(0, complaintId, reason, LocalDateTime.now());
        reportRepository.save(report);
    }
}
