import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
  private ArrayList<GridPane> gridpanes = new ArrayList<GridPane>();

  public static void main(String[] args) {
    Map<String, ArrayList<Platforms.Platform>> stations = Stations.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platforms.Platform>> station : stations.entrySet()) {
      logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue());
    }

    launch(args);

  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Tile Pane Test");
    TilePane tile = new TilePane();
    tile.setPadding(new Insets(5, 5, 5, 5));
    tile.setVgap(4);
    tile.setHgap(4);
    tile.setPrefColumns(10);
    Map<String, ArrayList<Platforms.Platform>> stations = Stations.INSTANCE.getStationCache();

    for (Map.Entry<String, ArrayList<Platforms.Platform>> station : stations.entrySet()) {
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
      for(Platforms.Platform platform: station.getValue()){
        grid.add(new Label(platform.getName()), 0, rowIndex);
        grid.add(new Label("Arrival"),    1, rowIndex);
        grid.add(new Label("15"),         2, rowIndex++);
      }     
      
      
      grid.setStyle("-fx-background-color: FAE6F3; -fx-border-color: black;");
      Pane pane = new Pane();
      pane.setOnMouseEntered(e -> {
        grid.setStyle("-fx-background-color: ff9900; -fx-border-color: black;");
        
      });
      pane.setOnMouseExited((MouseEvent e) -> {
        grid.setStyle("-fx-background-color: FAE6F3; -fx-border-color: black;");
      });
      pane.setOnMouseClicked((MouseEvent e) -> {
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
          pane.setMinWidth(bounds.getMaxX());
          pane.setMinHeight(bounds.getMaxY());

        }
        else {
          tile.getChildren().remove(grid);
          pane.setMinWidth(pane.getPrefWidth());
          pane.setMinHeight(pane.getPrefHeight());
          for (GridPane p : gridpanes) {
            p.setMinWidth(p.getPrefWidth());
            p.setMinHeight(p.getPrefHeight());
            tile.getChildren().add(p);
          }

        }
      });
     
      grid.add(pane, 0, 0, 3, 3);

      gridpanes.add(grid);

      tile.getChildren().add(grid);
    
      
    }

    Scene scene = new Scene(tile, tile.getPrefWidth(), tile.getPrefHeight());
    primaryStage.setScene(scene);

    primaryStage.show();
  }

  
}

