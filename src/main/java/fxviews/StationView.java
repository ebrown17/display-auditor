package fxviews;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class StationView {
	private final static Logger logger = LoggerFactory.getLogger("StationView");

	private final String stationName;
	private final ArrayList<Platform> stationPlatforms;
	private ArrayList<PlatformView> stationPlatformViews = new ArrayList<PlatformView>();
	private GridPane stationView;

	public StationView(String stationName, ArrayList<Platform> stationPlatforms) {
		this.stationName = stationName;
		this.stationPlatforms = stationPlatforms;
		buildStationPlatformViews();
		buildStationView();
	}

	private void buildStationPlatformViews() {
		for (Platform platform : stationPlatforms) {
			PlatformView platView = new PlatformView(platform);
			stationPlatformViews.add(platView);
		}
	}

	private void buildStationView() {
    stationView = new GridPane();
    
    Text stationTitle = new Text(stationName);
    stationTitle.setFont(Font.font("Arial", FontWeight.BOLD, 25));
    stationTitle.setFill(Color.WHITE);
    
    HBox hbBtn = new HBox();
    hbBtn.setAlignment(Pos.CENTER);
    hbBtn.getChildren().add(stationTitle);
    hbBtn.setPadding(new Insets(0, 0, -5, 5));
    stationView.add(hbBtn, 0, 0, 3, 1);
    GridPane.setHalignment(hbBtn, HPos.CENTER);
    int rowIndex = 1;
    int colSpan = 0;
    for (PlatformView platView : stationPlatformViews) {
    	if(rowIndex >4){colSpan=3; rowIndex=1;}
      stationView.add(platView.getPlatformNameLabel(), 0+colSpan, rowIndex);
      stationView.add(platView.getCurrentMsgTypeLabel(), 1+colSpan, rowIndex);
      stationView.add(platView.getCurrentMsgPlaytimeLabel(), 2+colSpan, rowIndex++);
      stationView.add(platView.getCurrentMsgTextBox(), 0+colSpan, rowIndex++, 3, 1);
      GridPane.setHalignment(platView.getCurrentMsgTextBox(), HPos.CENTER);
    }
    stationView.setPadding(new Insets(5, 5, 5, 5));
    stationView.setStyle("-fx-background-color: skyblue ; -fx-border-color: black; -fx-border-radius: 5.0;-fx-background-radius: 5.0;");
    stationView.setHgap(5);
    //stationView.setGridLinesVisible(true);
    JavaFxObservable.eventsOf(stationView, MouseEvent.MOUSE_ENTERED)
        .map(me -> "-fx-background-color: f08080; -fx-border-color: black; -fx-border-radius: 5.0;-fx-background-radius: 5.0;").observeOn(JavaFxScheduler.platform())
        .subscribe(stationView::setStyle);

    JavaFxObservable.eventsOf(stationView, MouseEvent.MOUSE_EXITED)
        .map(me -> "-fx-background-color: skyblue; -fx-border-color: black; -fx-border-radius: 5.0;-fx-background-radius: 5.0;").observeOn(JavaFxScheduler.platform())
        .subscribe(stationView::setStyle);

  }

	public GridPane getStationView() {
		return stationView;
	}

	public ArrayList<PlatformView> getPlatformViews() {
		return stationPlatformViews;
	}
}
