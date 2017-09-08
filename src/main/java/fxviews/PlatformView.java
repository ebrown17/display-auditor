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
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlatformView {
  private final static Logger logger = LoggerFactory.getLogger("StationView");

  private final String platformName;
  private final String stationName;
  private final Integer platformId;
  private final Platform platform;
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
    this.platform = platform;
    platform.setPlatformView(this);
    generatePlatformLabels();
  }

  private void generatePlatformLabels() {
    platformNameLabel = new Label(platformName);
    platformNameLabel.setPadding(new Insets(0, 0, 0, 2));

    currentMsgTypeLabel = new Label("Uknown");
    currentMsgTypeLabel.setPadding(new Insets(0, 0, 0, 5));
    currentMsgTypeLabel.setMinWidth(145);
    currentMsgTypeLabel.setMaxWidth(145);
    currentMsgTypeLabel.setAlignment(Pos.CENTER_LEFT);


    currentMsgToolTip = new Tooltip("");
    currentMsgToolTip.setStyle("-fx-background-color: white; -fx-font-size: 12; -fx-text-fill: black;");

    currentMsgTextLabel = new Label("No messages received");
    currentMsgTextLabel.setTooltip(currentMsgToolTip);
    currentMsgTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
    currentMsgTextLabel.setTextFill(Color.RED);

    currentMsgTextBox.setAlignment(Pos.CENTER);
    currentMsgTextBox.getChildren().add(currentMsgTextLabel);
    currentMsgTextBox.setMinHeight(60);
    currentMsgTextBox.setMaxHeight(60);
    currentMsgTextBox.setMinWidth(180);
    currentMsgTextBox.setMaxWidth(180);
    currentMsgTextBox.setStyle("-fx-background-color: black;-fx-border-color: black;-fx-border-width:2;");

    JavaFxObservable.eventsOf(currentMsgTextLabel, MouseEvent.MOUSE_ENTERED).map(me -> currentMsgTextLabel.getText())
        .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgToolTip::setText);

    currentMsgPlaytimeLabel = new Label("0");
    currentMsgPlaytimeLabel.setMinWidth(50);
    currentMsgPlaytimeLabel.setMaxWidth(50);
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

  /**
   * Calling this method also resets the message playtime interval calling
   * {@link #resetCurrentMsgPlaytime() resetCurrentMsgPlaytime}
   * 
   * @param currentMsgType sets the {@code}Label currentMsgTypeLabel text to specified String <br>
   *        This change will take place on RxJava's {@link io.reactivex.schedulers.Schedulers#computation()
   *        Computation} Scheduler then will be observed on the main JavaFX Application
   *        Thread.
   * 
   */
  public void setCurrentMsgType(String currentMsgType) {
    resetCurrentMsgPlaytime();
    Observable<String> observer = Observable.just(currentMsgType);
    observer.subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
        .subscribe(currentMsgTypeLabel::setText);
  }

  /**
   * This {@code}Label is used to simulate a message on a Platform Sign
   * 
   * @param currentMsgText sets the {@code}Label currentMsgTextLabel text to specified String <br>
   *        This change will take place on RxJava's {@link io.reactivex.schedulers.Schedulers#computation()
   *        Computation} Scheduler then will be observed on the main JavaFX Application
   *        Thread.
   * 
   */
  public void setCurrentMsgText(String currentMsgText) {
    Observable.just(currentMsgText).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
        .subscribe(currentMsgTextLabel::setText);
  }

  /**
   * If an observable interval is already counting the message playtime, it will dispose of it and
   * restart the interval.
   * 
   */
  private void resetCurrentMsgPlaytime() {
    if (msgPlaytimeDisposable == null) {
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgPlaytimeLabel::setText);
    }
    else {
      msgPlaytimeDisposable.dispose();
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgPlaytimeLabel::setText);
    }
  }

}
