package fxviews;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
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
import protobuf.JdssAuditor;
import protobuf.JdssAuditor.DisplayData.AuditorMessageType;
import protobuf.JdssAuditor.DisplayData.Destination;
import protobuf.JdssAuditor.DisplayData.Eta;

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

	private static String lightcoral = "-fx-background-color: lightcoral";
	private static String lightgreen = "-fx-background-color: lightgreen";
	private static String mintcream = "-fx-background-color: mintcream";
	private static String tomato = "-fx-background-color: tomato";
	private static String firebrick = "-fx-background-color: firebrick";
	private static String thistle = "-fx-background-color: thistle";
	private static String mistyrose = "-fx-background-color: mistyrose";
	private static String dodgerblue = "-fx-background-color: dodgerblue";
	private static String lightseagreen = "-fx-background-color: lightseagreen";
	private static String palegoldenrod = "-fx-background-color: palegoldenrod";

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
		platformInfoBox.setStyle("-fx-font-family: monospace;-fx-font-size: 12;");

		platformDisplayBox = new HBox();
		platformDisplayBox.setMaxWidth(225);
		platformDisplayBox.setMinWidth(225);

		currentDisplayLabel = new Label("No Messages Received ");
		currentDisplayLabel.setAlignment(Pos.CENTER);
		currentDisplayLabel.setMinWidth(200);
		currentDisplayLabel.setMaxWidth(200);
		currentDisplayLabel.setMinHeight(60);
		currentDisplayLabel.setMaxHeight(60);
		currentDisplayLabel.setTextFill(Color.RED);
		currentDisplayLabel.setStyle("-fx-background-color: black;-fx-font-size: 12;");

		platformDisplayBox.getChildren().add(currentDisplayLabel);
		platformDisplayBox.setAlignment(Pos.CENTER);
		platformDisplayBox.setPadding(new Insets(2, 2, 2, 2));
		platformBox = new VBox();
		platformBox.getChildren().addAll(platformInfoBox, platformDisplayBox);

		currentMsgToolTip = new Tooltip("");
		currentMsgToolTip.setStyle("-fx-background-color: white; -fx-font-size: 12; -fx-text-fill: black;");
		currentDisplayLabel.setTooltip(currentMsgToolTip);

		JavaFxObservable.eventsOf(platformDisplayBox, MouseEvent.MOUSE_ENTERED).map(me -> currentDisplayLabel.getText())
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

	/**
	 * Calling this method also resets the message playtime interval calling
	 * {@link #resetCurrentMsgPlaytime() resetCurrentMsgPlaytime}
	 * 
	 * @param currentMsgType
	 *            sets the {@code}Label currentMessageTypeLabel text to
	 *            specified String <br>
	 *            This change will take place on RxJava's
	 *            {@link io.reactivex.schedulers.Schedulers#computation()
	 *            Computation} Scheduler then will be observed on the main
	 *            JavaFX Application Thread.
	 * 
	 */
	public void configureMessage(JdssAuditor.DisplayData message) {
		AuditorMessageType type = message.getMessageType();
		resetCurrentMsgPlaytime();

		switch (type) {
		case BOARDING_ADVICE:
			Observable.just(lightcoral).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getBoardingAdvice().getAdvice());

			break;
		case SHORT_ETA:
			Observable.just(lightgreen).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, buildEtaMsg(message.getShortEta().getShortEtaList()));

			break;
		case FULL_ETA:
			Observable.just(lightseagreen).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, buildEtaMsg(message.getFullEta().getFullEtaList()));
			break;
		case DESTINATION:
			if(message.getDestination().getDisplayType().equals("Arrived")){
				Observable.just(firebrick).observeOn(JavaFxScheduler.platform()).subscribe(currentMessageTypeLabel::setStyle);
			}else {
				Observable.just(tomato).observeOn(JavaFxScheduler.platform()).subscribe(currentMessageTypeLabel::setStyle);
			}

			Observable.just(message.getDestination().getDisplayType().toUpperCase()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, buildDestinatoinMsg(message.getDestination()));
			break;
		case NEXT_TRAIN:
			Observable.just(thistle).observeOn(JavaFxScheduler.platform()).subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getNextTrain().getNextTrain());
			break;
		case SCROLL_INFO:
			Observable.just(mistyrose).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getScrollInfo().getScrollingMessage());
			break;
		case TIME:
			Observable.just(dodgerblue).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getTime().getTime());
			break;
		case INFO:
			Observable.just(mintcream).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getInfo().getInfo());
			break;
		case OUT_OF_SERVICE:
			Observable.just(palegoldenrod).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setStyle);

			Observable.just(type.toString()).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMessageTypeLabel::setText);

			setCurrentMsgText(type, message.getOutOfService().getOutOfService());
			break;
		default:
			break;

		}

	}

	/**
	 * This {@code}Label is used to simulate a message on a Platform Sign
	 * 
	 * @param currentMsgText
	 *            sets the {@code}Label currentDisplayLabel text to specified
	 *            String <br>
	 *            This change will take place on RxJava's
	 *            {@link io.reactivex.schedulers.Schedulers#computation()
	 *            Computation} Scheduler then will be observed on the main
	 *            JavaFX Application Thread.
	 * 
	 */
	private void setCurrentMsgText(AuditorMessageType type, String currentMsgText) {
		switch (type) {
		case BOARDING_ADVICE:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case SHORT_ETA:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case FULL_ETA:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case DESTINATION:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case NEXT_TRAIN:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case SCROLL_INFO:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case TIME:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bolder;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case INFO:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 10; -fx-font-weight: bold;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		case OUT_OF_SERVICE:
			Observable
					.just("-fx-background-color: black;-fx-font-family: monospace; -fx-font-size: 12; -fx-font-weight: bolder;")
					.observeOn(JavaFxScheduler.platform()).subscribe(currentDisplayLabel::setStyle);
			break;
		default:
			break;
		}
		Observable.just(currentMsgText).subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
				.subscribe(currentDisplayLabel::setText);
	}

	/**
	 * If an observable interval is already counting the message playtime, it
	 * will dispose of it and restart the interval.
	 * 
	 */
	private void resetCurrentMsgPlaytime() {
		if (msgPlaytimeDisposable == null) {
			msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
					.map(l -> l.toString()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentPlaytimeLabel::setText);
		} else {
			msgPlaytimeDisposable.dispose();
			msgPlaytimeDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
					.map(l -> l.toString()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentPlaytimeLabel::setText);
		}
	}

	private static String buildEtaMsg(List<Eta> etaList) {
		StringBuilder etas = new StringBuilder("");
		for (Eta eta : etaList) {
			if (eta.getEta2() == 0) {
				etas.append(String.format("%-18s %s MINS%n", eta.getDestination(), eta.getEta1()));
			} else {
				etas.append(String.format("%-18s %s,%s MINS%n", eta.getDestination(), eta.getEta1(), eta.getEta2()));
			}
			etas.append(eta.getLength() + " CAR TRAIN\n");
		}
		return etas.toString();
	}

	private static String buildDestinatoinMsg(Destination data) {
		StringBuilder destination = new StringBuilder("");
		int maxWidth=0;
		if(data.getDestination().contains("\n")){
			String[] dest = data.getDestination().split("\n");
			for(String partialD : dest){
				maxWidth = Math.max(partialD.length(), data.getTrainLength().length());
			}
			for(String partialD : dest){
				destination.append(String.format("%s\n", StringUtils.center(partialD, maxWidth)));
			}
		}else{
			maxWidth = Math.max(data.getDestination().length(), data.getTrainLength().length());
			destination.append(String.format("%s\n", StringUtils.center(data.getDestination(), maxWidth)));
		}
		
		destination.append(String.format("%s", StringUtils.center(data.getTrainLength(), maxWidth)));
		return destination.toString();
	}

}
