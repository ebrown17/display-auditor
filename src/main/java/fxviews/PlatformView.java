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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import protobuf.JdssAuditor.DisplayData.AuditorMessageType;

public class PlatformView {
  private final static Logger logger = LoggerFactory.getLogger("StationView");

  private final String platformName;
  private final String stationName;
  private final Integer platformId;
  
  private VBox platformBox;
  private HBox platformInfoBox, platformDisplayBox;
  
  private Label platformNameLabel;
  private Label currentMessageTypeLabel;
  private Label currentDisplayLabel;
  private Label currentPlaytimeLabel;
  private Tooltip currentMsgToolTip;
  private Disposable msgPlaytimeDisposable;
  
  private HBox currentMsgTextBox = new HBox();
  private HBox rowBox = new HBox();

  public PlatformView(Platform platform) {
    this.platformName = platform.getName();
    this.stationName = platform.getStation();
    this.platformId = platform.getPlatId();
    generatePlatformLabels();
  }

  private void generatePlatformLabels() {
    platformInfoBox = new HBox();
    platformInfoBox.setMaxWidth(225);
    platformInfoBox.setMinWidth(225);

    platformNameLabel = new Label(platformName);
    platformNameLabel.setAlignment(Pos.CENTER_RIGHT);
    platformNameLabel.setMinWidth(50);
    platformNameLabel.setMaxWidth(50);

    currentMessageTypeLabel = new Label("UKNOWN_MSG_TYPE");
    currentMessageTypeLabel.setAlignment(Pos.CENTER);
    currentMessageTypeLabel.setMinWidth(135);
    currentMessageTypeLabel.setMaxWidth(135);

    currentPlaytimeLabel = new Label("0");
    currentPlaytimeLabel.setAlignment(Pos.CENTER);
    currentPlaytimeLabel.setMinWidth(40);
    currentPlaytimeLabel.setMaxWidth(40);

    platformInfoBox.getChildren().addAll(platformNameLabel, currentMessageTypeLabel, currentPlaytimeLabel);
    platformInfoBox.setStyle("-fx-font-family: monospace;-fx-font-size: 12");
    
    platformDisplayBox = new HBox();
    platformDisplayBox.setMaxWidth(225);
    platformDisplayBox.setMinWidth(225);

    currentDisplayLabel = new Label("No Messages Received");
    currentDisplayLabel.setAlignment(Pos.CENTER);
    currentDisplayLabel.setMinWidth(200);
    currentDisplayLabel.setMaxWidth(200);
    currentDisplayLabel.setMinHeight(60);
    currentDisplayLabel.setMaxHeight(60);
    currentDisplayLabel.setTextFill(Color.RED);
    currentDisplayLabel.setStyle("-fx-background-color: black;");
    
    platformDisplayBox.getChildren().add(currentDisplayLabel);
    platformDisplayBox.setAlignment(Pos.CENTER);
    platformDisplayBox.setPadding(new Insets(2, 2, 2, 2));
    platformBox = new VBox();
    platformBox.getChildren().addAll(platformInfoBox,  platformDisplayBox);

    currentMsgToolTip = new Tooltip("");
    currentMsgToolTip.setStyle("-fx-background-color: white; -fx-font-size: 12; -fx-text-fill: black;"); 
    currentDisplayLabel.setTooltip(currentMsgToolTip);
  

    JavaFxObservable.eventsOf(currentDisplayLabel, MouseEvent.MOUSE_ENTERED).map(me -> currentDisplayLabel.getText())
        .observeOn(JavaFxScheduler.platform()).subscribe(currentMsgToolTip::setText);


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
  
  public VBox getPlatformView() {
    return platformBox;
  }

/*  public Label getPlatformNameLabel() {
    return platformNameLabel;
  }

  public Label getcurrentMessageTypeLabel() {
    return currentMessageTypeLabel;
  }

  public HBox getCurrentMsgTextBox() {
    return currentMsgTextBox;
  }

  public Label getcurrentPlaytimeLabel() {
    return currentPlaytimeLabel;
  }*/

  /**
   * Calling this method also resets the message playtime interval calling
   * {@link #resetCurrentMsgPlaytime() resetCurrentMsgPlaytime}
   * 
   * @param currentMsgType sets the {@code}Label currentMessageTypeLabel text to specified String <br>
   *        This change will take place on RxJava's
   *        {@link io.reactivex.schedulers.Schedulers#computation() Computation} Scheduler then will
   *        be observed on the main JavaFX Application Thread.
   * 
   */
  public void setCurrentMsgType(String currentMsgType) {
    resetCurrentMsgPlaytime();
    Observable<String> observer = Observable.just(currentMsgType);
    observer.subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
        .subscribe(currentMessageTypeLabel::setText);
  }

  /**
   * This {@code}Label is used to simulate a message on a Platform Sign
   * 
   * @param currentMsgText sets the {@code}Label currentDisplayLabel text to specified String <br>
   *        This change will take place on RxJava's
   *        {@link io.reactivex.schedulers.Schedulers#computation() Computation} Scheduler then will
   *        be observed on the main JavaFX Application Thread.
   * 
   */
  public void setCurrentMsgText(AuditorMessageType type, String currentMsgText) {
    switch (type) {
      case BOARDING_ADVICE:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case SHORT_ETA:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case FULL_ETA:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case DESTINATION:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case NEXT_TRAIN:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case SCROLL_INFO:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case TIME:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bolder;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case INFO:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      case OUT_OF_SERVICE:
        Observable.just("-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bolder;")
            .observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
        break;
      default:
        break;
    }
    Observable.just(currentMsgText).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
        .subscribe(currentDisplayLabel::setText);
  }

  /**
   * If an observable interval is already counting the message playtime, it will dispose of it and
   * restart the interval.
   * 
   */
  private void resetCurrentMsgPlaytime() {
    if (msgPlaytimeDisposable == null) {
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .observeOn(JavaFxScheduler.platform()).subscribe(currentPlaytimeLabel::setText);
    }
    else {
      msgPlaytimeDisposable.dispose();
      msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
          .observeOn(JavaFxScheduler.platform()).subscribe(currentPlaytimeLabel::setText);
    }
  }

}
