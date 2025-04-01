package com.syp;

import com.syp.view.ComplaintPresenter;
import com.syp.view.HomePagePresenter;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //out-comment one of these if you want to see different pages:
        //run in /frontend mvn jpro:run for web-view
        //run \.startDBServer.cmd for database


        HomePagePresenter.show(stage);
        //ComplaintPresenter.show(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}