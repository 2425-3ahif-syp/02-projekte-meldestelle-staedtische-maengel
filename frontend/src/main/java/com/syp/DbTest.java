package com.syp;

import com.syp.database.ComplaintRepository;
import com.syp.database.Database;
import com.syp.model.Complaint;
import java.util.List;

public class DbTest {
    public static void main(String[] args) {
        Database database = Database.getInstance();
        ComplaintRepository complaintRepository = new ComplaintRepository();

        Complaint complaint1 = new Complaint(1,"Littering in Park", "Environment", "Parkstrasse 5, Linz", "There is a lot of litter around the park.", "");
        Complaint complaint2 = new Complaint(2,"Broken Streetlight", "Infrastructure", "Main Street 12, Wels", "The streetlight near the bus stop is broken.", "");
        Complaint complaint3 = new Complaint(3,"Pothole on Road", "Infrastructure", "Schulstrasse 7, Linz", "There is a large pothole that needs repair.", "");

        complaintRepository.addComplaint(complaint1);
        complaintRepository.addComplaint(complaint2);
        complaintRepository.addComplaint(complaint3);

        List<Complaint> complaints = complaintRepository.getAllComplaints();

        complaints.forEach(System.out::println);
    }
}
