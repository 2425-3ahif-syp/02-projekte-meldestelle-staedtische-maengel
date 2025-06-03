package com.syp;

import com.syp.database.ComplaintRepository;
import com.syp.database.Database;
import com.syp.model.Complaint;
import java.time.LocalDateTime;
import java.util.List;

public class DbTest {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        ComplaintRepository complaintRepository = new ComplaintRepository();

        // Testdaten mit allen erforderlichen Feldern
        Complaint complaint1 = new Complaint(
                0, // ID wird automatisch generiert
                "Littering in Park",
                "Environment",
                "Parkstrasse 5, Linz",
                "There is a lot of litter around the park.",
                null, // imagePath kann null sein
                "OPEN", // Status
                LocalDateTime.now(), // createdAt
                null,  // completedAt (initial null)
                1 // userId (Beispiel-Benutzer ID)
        );

        Complaint complaint2 = new Complaint(
                0,
                "Broken Streetlight",
                "Infrastructure",
                "Main Street 12, Wels",
                "The streetlight near the bus stop is broken.",
                null,
                "OPEN",
                LocalDateTime.now(),
                null,
                1
        );

        Complaint complaint3 = new Complaint(
                0,
                "Pothole on Road",
                "Infrastructure",
                "Schulstrasse 7, Linz",
                "ThereY is a large pothole that needs repair.",
                null,
                "IN_PROGRESS",
                LocalDateTime.now().minusDays(2),
                null,
                1
        );

        // Meldungen hinzufügen
        int id1 = complaintRepository.createComplaint(complaint1);
        int id2 = complaintRepository.createComplaint(complaint2);
        int id3 = complaintRepository.createComplaint(complaint3);

        System.out.println("Added complaints with IDs: " + id1 + ", " + id2 + ", " + id3);

        // Alle Meldungen abrufen (erste Seite mit 10 Einträgen)
        List<Complaint> complaints = complaintRepository.getFilteredComplaints(
                null, null, null, 1, 10);
        System.out.println("\nAll complaints (first page):");
        complaints.forEach(System.out::println);

        // Test: Eine Meldung als erledigt markieren
        if (!complaints.isEmpty()) {
            Complaint toUpdate = complaints.get(0);
            toUpdate.setStatus("RESOLVED");
            toUpdate.setCompletedAt(LocalDateTime.now());
            boolean updated = complaintRepository.updateComplaint(toUpdate);
            System.out.println("\nUpdate successful? " + updated);
        }

        // Erneuter Abruf
        System.out.println("\nAfter update:");
        complaintRepository.getFilteredComplaints(null, null, null, 1, 10)
                .forEach(System.out::println);

        // Test: Gesamtanzahl der Meldungen
        int total = complaintRepository.getTotalComplaintCount(null, null, null);
        System.out.println("\nTotal complaints in database: " + total);
    }
}