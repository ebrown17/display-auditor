import java.util.concurrent.TimeUnit;

import data.Platform;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.scene.control.Label;

public class PlatformView {

	private final String platformName;
	private final String stationName;
	private final Integer platformId;
	private Label platformNameLabel;
	private Label currentMsgTypeLabel;
	private Label currentMsgTextLabel;
	private Label currentMsgPlaytimeLabel;
	private Disposable msgPlaySub;

	public PlatformView(Platform platform) {
		this.platformName = platform.getName();
		this.stationName = platform.getStation();
		this.platformId = platform.getPlatId();
		generatePlatformLabels();
	}

	private void generatePlatformLabels() {
		platformNameLabel = new Label(platformName);
		currentMsgTypeLabel = new Label("Uknown");
		currentMsgTextLabel = new Label("No messages received");
		currentMsgPlaytimeLabel = new Label("0");
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

	public Label getCurrentMsgTextLabel() {
		return currentMsgTextLabel;
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

	public void setCurrentMsgText(Label currentMsgTextLabel) {
		this.currentMsgTextLabel = currentMsgTextLabel;
	}

	/**
	 * If an observable interval is already counting the message playtime, it
	 * will dispose of it and restart the interval.
	 * 
	 */
	public void resetCurrentMsgPlaytime() {
		if (msgPlaySub == null) {
			msgPlaySub = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
					.observeOn(JavaFxScheduler.platform()).subscribe(currentMsgPlaytimeLabel::setText);
		} else {
			msgPlaySub.dispose();
			msgPlaySub = Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
					.delay(1, TimeUnit.SECONDS, Schedulers.io()).observeOn(JavaFxScheduler.platform())
					.subscribe(currentMsgPlaytimeLabel::setText);
		}
	}

}
