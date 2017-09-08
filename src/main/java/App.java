import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import data.StationPlatformAndViewCache;
import fxviews.PlatformView;
import fxviews.StationView;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import protobuf.JdssAuditor;
import protobuf.JdssAuditor.DisplayData.AuditorMessageType;
import protobuf.JdssAuditor.DisplayData.Eta;
import server.Server;

public class App extends Application {

  private final static Logger logger = LoggerFactory.getLogger("App");
  private boolean fullScreen = false;
  private static ArrayList<GridPane> gridpanes = new ArrayList<GridPane>();
  private static ArrayList<PlatformView> pv = new ArrayList<PlatformView>();
  private static Server server;


  public static void main(String[] args) {

    Runnable runnable = () -> {
      launch(args);
    };

    new Thread(runnable).start();

    LinkedBlockingQueue<JdssAuditor.DisplayData> msgQueue = new LinkedBlockingQueue<JdssAuditor.DisplayData>();

    try {
      logger.debug("Server Starting... ");
      InetSocketAddress socketAddress = new InetSocketAddress(26002);

      server = new Server(socketAddress);
      server.configure(msgQueue);
      server.startServer();

    } catch (Exception e) {
      e.printStackTrace();
    }
    
    
    

    Map<Integer, Platform> platformCache = StationPlatformAndViewCache.INSTANCE.getPlatformCache();

    while (true) {

      try {
        JdssAuditor.DisplayData message = msgQueue.take();

        AuditorMessageType type = message.getMessageType();

        PlatformView platform = platformCache.get(message.getPlatform()).getPlatformView();
        logger.debug("Msg recieved type: {} text: {} ", type, message.toString());
        switch (type) {
          case BOARDING_ADVICE:

           /* Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getBoardingAdvice().getAdvice()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
        	  
        	  platform.setCurrentMsgType(type.toString());
        	  platform.setCurrentMsgText(message.getBoardingAdvice().getAdvice());

            break;
          case SHORT_ETA:

           /* Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getShortEta().getShortEtaList()).map(etaMsg -> buildEtaMsg(etaMsg))
                .subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
                .subscribe(platform::setCurrentMsgText);*/
        	  
            platform.setCurrentMsgType(type.toString());
      	  platform.setCurrentMsgText(buildEtaMsg(message.getShortEta().getShortEtaList()));
            
            break;
          case FULL_ETA:
            /*Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getFullEta().getFullEtaList()).map(etaMsg -> buildEtaMsg(etaMsg))
                .subscribeOn(Schedulers.computation()).observeOn(JavaFxScheduler.platform())
                .subscribe(platform::setCurrentMsgText);*/
            
            platform.setCurrentMsgType(type.toString());
        	  platform.setCurrentMsgText(buildEtaMsg(message.getFullEta().getFullEtaList()));
            break;
          case DESTINATION:
            /*Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getDestination().getDestination()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
            
            platform.setCurrentMsgType(type.toString());
      	  platform.setCurrentMsgText(message.getDestination().getDestination());
            
            break;
          case NEXT_TRAIN:
            /*Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getNextTrain().getNextTrain()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
            break;
          case SCROLL_INFO:
            /*Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getScrollInfo().getScrollingMessage()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
            
            platform.setCurrentMsgType(type.toString());
        	  platform.setCurrentMsgText(message.getScrollInfo().getScrollingMessage());
            
            break;
          case TIME:
        	  platform.setCurrentMsgType(type.toString());
        	  platform.setCurrentMsgText(message.getTime().getTime());
          /*  Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getTime().getTime()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
            break;
          case INFO:
            /*Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getInfo().getInfo()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);*/
            
            platform.setCurrentMsgType(type.toString());
      	  platform.setCurrentMsgText(message.getInfo().getInfo());
            break;
          case OUT_OF_SERVICE:
            Observable.just(type).map(msgType -> msgType.toString()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgType);

            Observable.just(message.getOutOfService().getOutOfService()).subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform()).subscribe(platform::setCurrentMsgText);
            break;
          default:
            break;
        }
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public static String buildEtaMsg(List<Eta> etaList) {
    StringBuilder etas = new StringBuilder("");
    for (Eta eta : etaList) {
      if (eta.getEta2() == 0) {
        etas.append(String.format("%-18s %5s MINS%n", eta.getDestination(), eta.getEta1()));
      }
      else {
        etas.append(String.format("%-18s %5s, %s MINS%n", eta.getDestination(), eta.getEta1(), eta.getEta2()));
      }
      etas.append(eta.getLength() + " CAR TRAIN\n");
    }
    return etas.toString();
  }



  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Tile Pane Test");
    TilePane tile = new TilePane();
    tile.setPrefColumns(8);
    // FlowPane tile = new FlowPane();
    // tile.setPrefWrapLength(1920);
    tile.setPadding(new Insets(5, 5, 5, 5));
    tile.setVgap(5);
    tile.setHgap(5);
    tile.setStyle("-fx-background-color: darkslategrey;");

    List<StationView> stationViews = StationPlatformAndViewCache.INSTANCE.getStationViewCache();

    for (StationView stationView : stationViews) {
      gridpanes.add(stationView.getStationView());
      pv.addAll(stationView.getPlatformViews());

      tile.getChildren().add(stationView.getStationView());
     // stationView.getStationView().setGridLinesVisible(true);
    }

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    Scene scene = new Scene(tile, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
    primaryStage.setOnCloseRequest(ce -> shutdownApp());

    JavaFxObservable.eventsOf(tile, MouseEvent.MOUSE_CLICKED)
        .subscribe(me -> primaryStage.setFullScreen(fullScreen = !fullScreen));

    JavaFxObservable.eventsOf(scene, KeyEvent.KEY_RELEASED).map(KeyEvent::getCode)
        .subscribe(key -> handleKeyevent(key));

  }

  private void shutdownApp() {
    server.shutdownServer();
    try {
		Thread.sleep(3000);
		System.exit(0);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
  }


  private void handleKeyevent(KeyCode code) {

    switch (code) {
      case SPACE:
        shutdownApp();
        break;
      default:
        break;

    }

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
