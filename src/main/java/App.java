import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import data.StationCache;
import fxviews.PlatformView;
import fxviews.StationView;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
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
  private static ArrayList<PlatformView> pv = new ArrayList<PlatformView>();
  private static ArrayList<Label> labeltest = new ArrayList<Label>();
  private Label testLab;

  public static void main(String[] args) {
    Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

   /* for (Map.Entry<String, ArrayList<Platform>> station : stations.entrySet()) {
      logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue());
    }*/

    Runnable runnable = () -> {
      launch(args);
    };
    new Thread(runnable).start();
    logger.info("Thread started");
    try {
		Thread.sleep(4000);
	} catch (InterruptedException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    for(PlatformView pV : pv){      
		pV.resetCurrentMsgPlaytime();
		pV.setCurrentMsgText("THIS IS A TEST MESSAGE\nTHIS IS A TEST MESSAGE\n");
	}
    int count = 0;
    while(count < 20){
    	try {
			Thread.sleep(10000);
			for(PlatformView pV : pv){
				pV.resetCurrentMsgPlaytime();
				if(count %3 == 0){
				  pV.setCurrentMsgText("This is a long message test that should disappear from page and not wrap");  
				}
				else if(count %2 == 0){
                  pV.setCurrentMsgText("Display Test: Showing \nHow a multi line\nwill look to the user");  
                }
				else{
				  pV.setCurrentMsgText("THIS IS A TEST MESSAGE\nTHIS IS A TEST MESSAGE\n");
				}
				
			}
			count++;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    // new Thread(() -> { launch(args); }).start();
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Tile Pane Test");
   /* TilePane tile = new TilePane();
    tile.setPrefColumns(8);*/
    FlowPane tile = new FlowPane();
   // tile.setPrefWrapLength(1920);
    tile.setPadding(new Insets(5, 5, 5, 5));
    tile.setVgap(5);
    tile.setHgap(5);
    tile.setStyle("-fx-background-color: darkslategrey;");

    Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platform>> station : stations.entrySet()) {
      logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue());
      
      StationView stationView = new StationView(station.getKey(),station.getValue());
      
      gridpanes.add(stationView.getStationView());
      pv.addAll(stationView.getPlatformViews());
      
      tile.getChildren().add(stationView.getStationView());
    }
    
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    Scene scene = new Scene(tile, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
    
    JavaFxObservable.eventsOf(tile, MouseEvent.MOUSE_CLICKED).subscribe(me -> primaryStage.setFullScreen(fullScreen= !fullScreen));
    
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

