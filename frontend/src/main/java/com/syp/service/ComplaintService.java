package com.syp.service;

import com.syp.model.Complaint;
import com.syp.repository.ComplaintRepository;

import java.util.List;

public class ComplaintService {
    private final ComplaintRepository repo = new ComplaintRepository();

    public List<Complaint> getAllComplaints() {
        return repo.findAll();
    }

    public List<Complaint> getFilteredComplaints(String searchText, String category, String status) {
        return repo.findFiltered(searchText, category, status);
    }

    public void registerComplaint(String subject, String category, String address, String description,
                                  String imagePath, String email) {
        if (subject == null || subject.isBlank() ||
                category == null || category.isBlank() ||
                address == null || address.isBlank()) {
            throw new IllegalArgumentException("Betreff, Kategorie und Standort sind Pflichtfelder.");
        }

        Complaint c = new Complaint(
                0,
                subject,
                category,
                address,
                description,
                imagePath,
                "Offen",
                email,
                null,
                null
        );

        repo.save(c);
    }


    public void updateComplaintStatus(int id, String newStatus) {
        repo.updateStatus(id, newStatus);
    }

    public Complaint findById(int id) {
        return repo.findById(id);
    }

    public void deleteComplaintById(int id) {
        repo.delete(id);
    }
}
