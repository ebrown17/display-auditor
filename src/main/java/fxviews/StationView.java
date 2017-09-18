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
import javafx.scene.control.Label;
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
  private final static double WIDTH = 225.0;
  private Label title;
  private HBox titleBox;
  private int row = 0;
  private int column = 0;

  public StationView(String stationName, ArrayList<Platform> stationPlatforms) {
    this.stationName = stationName;
    this.stationPlatforms = stationPlatforms;
    storePlatformViews();
    buildStationView();
    addPlatformView();
  }

  private void storePlatformViews() {
    for (Platform platform : stationPlatforms) {
      stationPlatformViews.add(platform.getPlatformView());
    }
  }

  private void buildStationView() {
    stationView = new GridPane();
    titleBox = new HBox();
    titleBox.setMaxWidth(WIDTH);
    titleBox.setMinWidth(WIDTH);
    titleBox.setAlignment(Pos.CENTER);
    title = new Label(stationName);
    title.setStyle("-fx-font-family: Arial; -fx-font-size: 18; -fx-font-weight: bolder;");
    
    title.setTextFill(Color.WHITE);
    titleBox.getChildren().add(title);
    stationView.add(titleBox, column, row++,2,1);
    stationView.setStyle("-fx-background-color: skyblue; -fx-border-color:black;-fx-border-radius: 5.0;-fx-background-radius: 5.0;");
    stationView.setPadding(new Insets(2, 2, 2, 2));
    stationView.setMaxSize(225, 225);
    
    JavaFxObservable.eventsOf(stationView, MouseEvent.MOUSE_ENTERED)
        .map(
            me -> "-fx-background-color: f08080; -fx-border-color: black; -fx-border-radius: 5.0;-fx-background-radius: 5.0;")
        .observeOn(JavaFxScheduler.platform()).subscribe(stationView::setStyle);

    JavaFxObservable.eventsOf(stationView, MouseEvent.MOUSE_EXITED)
        .map(
            me -> "-fx-background-color: skyblue; -fx-border-color: black; -fx-border-radius: 5.0;-fx-background-radius: 5.0;")
        .observeOn(JavaFxScheduler.platform()).subscribe(stationView::setStyle);

  }
  
  private void addPlatformView() {
    
	  if(stationPlatformViews.size()==3 && stationName.contains("K")){
    	  row++;
      }
	  
    for(PlatformView platform : stationPlatformViews){
      if (row > 2) {
        column++;
        row=1;
        titleBox.setMaxWidth(titleBox.getMaxWidth() + WIDTH);
        titleBox.setMinWidth(titleBox.getMinWidth() + WIDTH);
        titleBox.setAlignment(Pos.CENTER);
        stationView.setPadding(new Insets(2, 6,2,6));
      }
    	  
      stationView.add(platform.getPlatformView(), column, row++);
    }
   
  }

  public GridPane getStationView() {
    return stationView;
  }

  public ArrayList<PlatformView> getPlatformViews() {
    return stationPlatformViews;
  }
  
  public static GridPane getBlankView(){
    GridPane blank = new GridPane();
    HBox blankBox = new HBox();
    blankBox.setMaxWidth(WIDTH);
    blankBox.setMinWidth(WIDTH);
    blankBox.setAlignment(Pos.CENTER);
    blank.add(blankBox, 0, 0);
    blank.setPadding(new Insets(2, 2, 2, 2));
    blank.setStyle("-fx-border-color: darkslategrey;-fx-border-radius: 5.0;-fx-background-radius: 5.0;");
    blank.setMaxSize(225, 225);;
    return blank;
  }
}
