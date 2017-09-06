package fxviews;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class PlatformView {
  private final static Logger logger = LoggerFactory.getLogger("StationView");

  private final String platformName;
  private final String stationName;
  private final Integer platformId;
  private Label platformNameLabel;
  private Label currentMsgTypeLabel;
  private Label currentMsgTextLabel;
  private Label currentMsgPlaytimeLabel;
  private Tooltip currentMsgToolTip;
  private Disposable msgPlaytimeDisposable;
  private HBox currentMsgTextBox = new HBox();

  public PlatformView(Platform platform) {
    this.platformName = platform.getName();
    this.stationName = platform.getStation();
    this.platformId = platform.getPlatId();
    generatePlatformLabels();
  }

  private void generatePlatformLabels() {
    platformNameLabel = new Label(platformName);
    platformNameLabel.setPadding(new Insets(0, 0, 0, 2));
    currentMsgTypeLabel = new Label("Uknown");
    currentMsgTypeLabel.setPadding(new Insets(0, 0, 0, 5));

    currentMsgToolTip = new Tooltip("");
    currentMsgTextLabel = new Label("No messages received");

    Tooltip.install(currentMsgTextLabel, currentMsgToolTip);

    currentMsgTextLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 10));
    currentMsgTextLabel.setTextFill(Color.RED);
    
    currentMsgTextBox.setAlignment(Pos.CENTER);
    currentMsgTextBox.getChildren().add(currentMsgTextLabel);
    currentMsgTextBox.setMinHeight(40);
    currentMsgTextBox.setMaxHeight(40);  
    currentMsgTextBox.setMinWidth(180);
    currentMsgTextBox.setMaxWidth(180);
    currentMsgTextBox.setStyle("-fx-background-color: black;-fx-border-color: skyblue;-fx-border-width:2;-fx-padding:2;");
    
    

    JavaFxObservable.eventsOf(currentMsgTextLabel, MouseEvent.MOUSE_ENTERED).map(me -> currentMsgTextLabel.getText())
        .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgToolTip::setText);

    currentMsgPlaytimeLabel = new Label("0");
    currentMsgPlaytimeLabel.setMinWidth(50);
    currentMsgPlaytimeLabel.setAlignment(Pos.CENTER_RIGHT);
  }

  public String getPlatformName() {
    return platformName;
  }

  public String getStationName() {
    return stationName;
  }

  public Integer getPlatformId() {
    return platformId;
  }

  public Label getPlatformNameLabel() {
    return platformNameLabel;
  }

  public Label getCurrentMsgTypeLabel() {
    return currentMsgTypeLabel;
  }

  public HBox getCurrentMsgTextBox() {
    return currentMsgTextBox;
  }

  public Label getCurrentMsgPlaytimeLabel() {
    return currentMsgPlaytimeLabel;
  }

  public void setPlatformNameLabel(Label platformNameLabel) {
    this.platformNameLabel = platformNameLabel;
  }

  public void setCurrentMsgType(Label currentMsgTypeLabel) {
    this.currentMsgTypeLabel = currentMsgTypeLabel;

  }

  public void setCurrentMsgText(String currentMsgText) {
    Observable<String> observer = Observable.just(currentMsgText);
    observer.observeOn(JavaFxScheduler.platform()).subscribe(currentMsgTextLabel::setText);

  }

  /**
   * If an observable interval is already counting the message playtime, it will dispose of it and
   * restart the interval.
   * 
   */
  public void resetCurrentMsgPlaytime() {
    if (msgPlaytimeDisposable == null) {
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgPlaytimeLabel::setText);
    }
    else {
      msgPlaytimeDisposable.dispose();
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .delay(1, TimeUnit.SECONDS, Schedulers.io()).observeOn(JavaFxScheduler.platform())
          .subscribe(currentMsgPlaytimeLabel::setText);
    }
  }

}
