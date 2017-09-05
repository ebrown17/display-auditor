import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import data.StationCache;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

  private final static Logger logger = LoggerFactory.getLogger("App");
  private boolean fullScreen = false;
  private static ArrayList<GridPane> gridpanes = new ArrayList<GridPane>();
  private static ArrayList<Label> labeltest = new ArrayList<Label>();
  private Label testLab;

  public static void main(String[] args) {
    Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platform>> station : stations.entrySet()) {
      logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue());
    }

    Runnable runnable = () -> {
      launch(args);
    };
    new Thread(runnable).start();
 
    // new Thread(() -> { launch(args); }).start();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Tile Pane Test");
    TilePane tile = new TilePane();
    tile.setPadding(new Insets(5, 5, 5, 5));
    tile.setVgap(4);
    tile.setHgap(4);
    tile.setPrefColumns(10);

    Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platform>> station : stations.entrySet()) {
      logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue());
                
      GridPane grid = new GridPane();
      Text scenetitle = new Text(station.getKey());
      scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
      HBox hbBtn = new HBox();
      hbBtn.setAlignment(Pos.CENTER);
      hbBtn.getChildren().add(scenetitle);
      grid.setHgap(10);
      grid.setVgap(10);
      grid.add(hbBtn, 0, 0, 3, 1);
      int rowIndex = 1;

      for (Platform platform : station.getValue()) {
    	  
    	  PlatformView platV = new PlatformView(platform);
    	  
        grid.add(platV.getPlatformNameLabel(), 0, rowIndex);
        grid.add(platV.getCurrentMsgTypeLabel(), 1, rowIndex);
        
        grid.add(platV.getCurrentMsgPlaytimeLabel(), 2, rowIndex++);
      }
      grid.setStyle("-fx-background-color: FAE6F3; -fx-border-color: black;");



      JavaFxObservable.eventsOf(grid, MouseEvent.MOUSE_ENTERED)
          .map(me -> "-fx-background-color: ff9900; -fx-border-color: black;").subscribe(grid::setStyle);

      JavaFxObservable.eventsOf(grid, MouseEvent.MOUSE_EXITED)
          .map(me -> "-fx-background-color: FAE6F3; -fx-border-color: black;").subscribe(grid::setStyle);

      JavaFxObservable.eventsOf(grid, MouseEvent.MOUSE_CLICKED).subscribe(me -> test(primaryStage, tile, grid));

      gridpanes.add(grid);

      tile.getChildren().add(grid);

    }

    Scene scene = new Scene(tile, tile.getPrefWidth(), tile.getPrefHeight());
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
  }

  private void test(Stage primaryStage, TilePane tile, GridPane grid) {
    primaryStage.setFullScreen(fullScreen = !fullScreen);
    if (fullScreen) {
      for (GridPane p : gridpanes) {
        if (grid != p) {
          tile.getChildren().remove(p);
        }
      }
      Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
      grid.setMinWidth(bounds.getMaxX());
      grid.setMinHeight(bounds.getMaxY());

    }
    else {
      tile.getChildren().remove(grid);
      for (GridPane p : gridpanes) {
        p.setMinWidth(p.getPrefWidth());
        p.setMinHeight(p.getPrefHeight());
        tile.getChildren().add(p);
      }

    }
    primaryStage.centerOnScreen();
  }


}

