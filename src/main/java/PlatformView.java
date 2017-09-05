import java.util.concurrent.TimeUnit;

import data.Platform;
import io.reactivex.Observable;
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

	public PlatformView(Platform platform) {
		this.platformName = platform.getName();
		this.stationName = platform.getStation();
		this.platformId = platform.getPlatId();
		generatePlatformLabels();
		Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
		.switchMap( outer -> {
			 Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation())
					.startWith(0L)
					.observeOn(JavaFxScheduler.platform())
					.doOnNext(l -> l.toString()).subscribe( t -> currentMsgPlaytimeLabel.setText(t.toString()));
					
					//Observable.from(l.toString())).observeOn(JavaFxScheduler.platform())
		});
        	
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

	public void setCurrentMsgTypeLabel(Label currentMsgTypeLabel) {
		this.currentMsgTypeLabel = currentMsgTypeLabel;

		Observable.interval(1, TimeUnit.SECONDS, Schedulers.computation()).map(l -> l.toString())
				.observeOn(JavaFxScheduler.platform()).subscribe(currentMsgPlaytimeLabel::setText);

	}

	public void setCurrentMsgTextLabel(Label currentMsgTextLabel) {
		this.currentMsgTextLabel = currentMsgTextLabel;
	}

	public void setCurrentMsgPlaytimeLabel(Label currentMsgPlaytimeLabel) {
		this.currentMsgPlaytimeLabel = currentMsgPlaytimeLabel;
	}

}
