package com.syp.util;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {

    public static void show(Stage ownerStage, String message) {
        Platform.runLater(() -> {

            Label toastLabel = new Label(message);
            toastLabel.setStyle(
                    "-fx-text-fill: white; " +
                            "-fx-font-size: 13px;"
            );
            toastLabel.setWrapText(true);
            toastLabel.setPadding(new Insets(10));

            StackPane container = new StackPane(toastLabel);
            container.setStyle(
                    "-fx-background-color: rgba(44, 62, 80, 0.9); " +
                            "-fx-background-radius: 4;"
            );

            double maxToastWidth = ownerStage.getWidth() * 0.8;
            container.setPrefWidth(maxToastWidth);

            double labelWidth = maxToastWidth - 20;

            toastLabel.setMaxWidth(labelWidth);

            Text helper = new Text(message);
            helper.setStyle(toastLabel.getStyle());
            helper.setWrappingWidth(labelWidth);
            helper.applyCss();
            double textHeight = helper.getLayoutBounds().getHeight();

            double toastHeight = textHeight + 20;

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double stageX = ownerStage.getX();
            double stageY = ownerStage.getY();
            double stageWidth = ownerStage.getWidth();

            double x = stageX + stageWidth - maxToastWidth - 20;

            double y = stageY + 20;

            if (x + maxToastWidth > screenBounds.getMaxX()) {
                x = screenBounds.getMaxX() - maxToastWidth - 20;
            }
            if (y + toastHeight > screenBounds.getMaxY()) {
                y = screenBounds.getMaxY() - toastHeight - 20;
            }

            System.out.println("TOAST Popup will show at x=" + x + ", y=" + y);


            Popup popup = new Popup();
            popup.getContent().add(container);
            popup.setAutoFix(true);
            popup.setAutoHide(true);
            popup.setHideOnEscape(true);
            popup.show(ownerStage, x, y);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), container);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), container);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(Duration.seconds(2));
            fadeOut.setOnFinished(evt -> popup.hide());

            fadeIn.play();
            fadeIn.setOnFinished(evt -> fadeOut.play());
        });
    }
}
