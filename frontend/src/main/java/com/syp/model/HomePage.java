package com.syp.model;

import com.syp.database.HomePageRepository;
import java.util.List;

public class HomePage {
    private final HomePageRepository repository;

    public HomePage() {
        this.repository = new HomePageRepository();
    }

    public List<Complaint> getAllComplaints() {
        return repository.getFilteredComplaints(
                null,   // searchTerm
                null,   // category
                null,   // status
                1,      // page
                Integer.MAX_VALUE  // pageSize (alle Einträge)
        );
    }

    // Optional: Methode mit Paginierung
    public List<Complaint> getComplaints(int page, int pageSize) {
        return repository.getFilteredComplaints(null, null, null, page, pageSize);
    }
}