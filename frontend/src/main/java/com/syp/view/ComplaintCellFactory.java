package com.syp.view;

import com.syp.model.Complaint;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class ComplaintCellFactory implements Callback<ListView<Complaint>, ListCell<Complaint>> {
    @Override
    public ListCell<Complaint> call(ListView<Complaint> param) {
        return new ListCell<Complaint>() {
            @Override
            protected void updateItem(Complaint complaint, boolean empty) {
                super.updateItem(complaint, empty);
                if (empty || complaint == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ComplaintCard card = new ComplaintCard(complaint);
                    card.setOnMouseClicked(event -> {

                    });
                    card.getStyleClass().add("complaint-card");
                    setGraphic(card);
                }
            }
        };
    }
}