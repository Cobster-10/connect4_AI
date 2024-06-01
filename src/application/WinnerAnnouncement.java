package application;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WinnerAnnouncement  {

    private Stage winnerStage;

    public void showWinnerMessage(int winner) {
        winnerStage = new Stage();
        winnerStage.setTitle("Winner Announcement");

        Text winnerText = new Text("Player " + winner + " wins");
        winnerText.setFont(new Font(20));
        winnerText.setFill(Color.GREEN);

        StackPane stackPane = new StackPane(winnerText);
        stackPane.setStyle("-fx-background-color: white; -fx-padding: 20px;");

        Scene winnerScene = new Scene(stackPane, 300, 100);
        winnerStage.setScene(winnerScene);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(winnerText.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(winnerText.opacityProperty(), 0.0))
        );
        timeline.setCycleCount(1);
        timeline.setOnFinished(event -> winnerStage.close());

        timeline.play();

        winnerStage.show();
    }

    // The rest of your WinnerAnnouncement class remains unchanged
}
