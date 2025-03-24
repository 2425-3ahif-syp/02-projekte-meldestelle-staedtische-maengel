package com.syp;

import com.syp.view.ComplaintPresenter;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ComplaintPresenter.show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
