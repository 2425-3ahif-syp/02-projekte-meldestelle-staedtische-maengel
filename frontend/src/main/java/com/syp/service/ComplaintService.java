package com.syp.service;

import com.syp.model.Complaint;
import com.syp.repository.ComplaintRepository;
import com.syp.util.EmailUtil;

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

        // E-Mail-Benachrichtigung senden, wenn Email angegeben
        if (email != null && !email.isBlank()) {
            String mailSubject = "Bestätigung Ihrer Mängelmeldung";
            String mailBody = String.format(
                    "Hallo,\n\nIhre Meldung \"%s\" wurde erfolgreich erfasst.\n" +
                            "Wir kümmern uns darum und halten Sie auf dem Laufenden.\n\n" +
                            "Mit freundlichen Grüßen\nIhre Gemeinde",
                    subject
            );
            EmailUtil.sendEmail(email, mailSubject, mailBody);
        }
    }


    public void updateComplaintStatus(int id, String newStatus) {
        repo.updateStatus(id, newStatus);

        // Nach dem Update die Beschwerde laden
        Complaint updatedComplaint = repo.findAll().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        if (updatedComplaint != null && updatedComplaint.getUserEmail() != null) {
            String subject = "Statusänderung Ihrer Mängelmeldung";
            String body = String.format(
                    "Hallo,\n\nDer Status Ihrer Meldung \"%s\" wurde auf \"%s\" geändert.\n\nMit freundlichen Grüßen\nIhre Gemeinde",
                    updatedComplaint.getSubject(),
                    newStatus
            );

            EmailUtil.sendEmail(updatedComplaint.getUserEmail(), subject, body);
        }
    }


    public void deleteComplaintById(int id) {
        repo.delete(id);
    }
}
