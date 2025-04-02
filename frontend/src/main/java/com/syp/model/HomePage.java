package com.syp.model;

import com.syp.database.HomePageRepository;

import java.util.List;

public class HomePage {
    private HomePageRepository repository;

    public HomePage() {
        repository = new HomePageRepository();
    }

    public List<Complaint> getAllComplaints() {
        return repository.getAllComplaints();
    }
}