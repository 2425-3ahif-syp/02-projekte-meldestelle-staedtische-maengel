package com.syp.view;

import com.syp.model.Complaint;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ComplaintListView extends VBox {
    private final ListView<Complaint> listView;

    public ComplaintListView(List<Complaint> complaints) {
        listView = new ListView<>();
        listView.setCellFactory(new ComplaintCellFactory());

        listView.getItems().addAll(complaints);
        getChildren().add(listView);
    }

    public void setComplaints(List<Complaint> complaints) {
        listView.getItems().setAll(complaints);
    }
}