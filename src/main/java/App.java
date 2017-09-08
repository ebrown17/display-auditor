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
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getBoardingAdvice().getAdvice());
					break;
				case SHORT_ETA:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, buildEtaMsg(message.getShortEta().getShortEtaList()));

					break;
				case FULL_ETA:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, buildEtaMsg(message.getFullEta().getFullEtaList()));
					break;
				case DESTINATION:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getDestination().getDestination());
					break;
				case NEXT_TRAIN:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getNextTrain().getNextTrain());
					break;
				case SCROLL_INFO:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getScrollInfo().getScrollingMessage());
					break;
				case TIME:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getTime().getTime());
					break;
				case INFO:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getInfo().getInfo());
					break;
				case OUT_OF_SERVICE:
					platform.setCurrentMsgType(type.toString());
					platform.setCurrentMsgText(type, message.getOutOfService().getOutOfService());
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
				etas.append(String.format("%-18s %s MINS%n", eta.getDestination(), eta.getEta1()));
			} else {
				etas.append(String.format("%-18s %s,%s MINS%n", eta.getDestination(), eta.getEta1(), eta.getEta2()));
			}
			etas.append(eta.getLength() + " CAR TRAIN\n");
		}
		return etas.toString();
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Tile Pane Test");
		// TilePane tile = new TilePane();
		// tile.setPrefColumns(8);
		FlowPane tile = new FlowPane();
		tile.setPrefWrapLength(1920);
		tile.setPadding(new Insets(5, 5, 5, 5));
		tile.setVgap(5);
		tile.setHgap(5);
		tile.setStyle("-fx-background-color: darkslategrey;");

		HBox space;

		Map<String, StationView> stationViews = StationPlatformAndViewCache.INSTANCE.getStationViewCache();
		tile.getChildren().add(stationViews.get("R60").getStationView());
		tile.getChildren().add(stationViews.get("R50").getStationView());
		tile.getChildren().add(stationViews.get("R40").getStationView());
		tile.getChildren().add(stationViews.get("R30").getStationView());
		tile.getChildren().add(stationViews.get("R20").getStationView());
		tile.getChildren().add(stationViews.get("R10").getStationView());
		tile.getChildren().add(stationViews.get("C10").getStationView());
		tile.getChildren().add(stationViews.get("C20").getStationView());
		tile.getChildren().add(stationViews.get("C30").getStationView());
		tile.getChildren().add(stationViews.get("C40").getStationView());
		addBlankGrids(7,tile);
		space = new HBox();
		space.setMinWidth(60);
		tile.getChildren().add(space);
		tile.getChildren().add(stationViews.get("K30").getStationView());
		addBlankGrids(1,tile);
		space = new HBox();
		space.setMinWidth(15);
		tile.getChildren().add(space);
		tile.getChildren().add(stationViews.get("C50").getStationView());
		tile.getChildren().add(stationViews.get("C60").getStationView());
		tile.getChildren().add(stationViews.get("C70").getStationView());
		tile.getChildren().add(stationViews.get("C80").getStationView());
		addBlankGrids(5,tile);
		space = new HBox();
		space.setMinWidth(60);
		tile.getChildren().add(space);
		tile.getChildren().add(stationViews.get("K20").getStationView());
		
		addBlankGrids(5,tile);
		tile.getChildren().add(stationViews.get("M80").getStationView());
		tile.getChildren().add(stationViews.get("M90").getStationView());
		addBlankGrids(2,tile);
		space = new HBox();
		space.setMinWidth(35);
		tile.getChildren().add(space);
		tile.getChildren().add(stationViews.get("K10").getStationView());
		addBlankGrids(4,tile);
		tile.getChildren().add(stationViews.get("L30").getStationView());
		tile.getChildren().add(stationViews.get("M70").getStationView());
		tile.getChildren().add(stationViews.get("M60").getStationView());
		tile.getChildren().add(stationViews.get("M50").getStationView());
		tile.getChildren().add(stationViews.get("M40").getStationView());
		tile.getChildren().add(stationViews.get("M30").getStationView());
		tile.getChildren().add(stationViews.get("M20").getStationView());
		tile.getChildren().add(stationViews.get("M10").getStationView());
		addBlankGrids(3,tile);
		tile.getChildren().add(stationViews.get("L10").getStationView());
		tile.getChildren().add(stationViews.get("L20").getStationView());
		
		tile.getChildren().add(stationViews.get("W40").getStationView());
		tile.getChildren().add(stationViews.get("W30").getStationView());
		tile.getChildren().add(stationViews.get("W20").getStationView());
		tile.getChildren().add(stationViews.get("W10").getStationView());
		
		tile.getChildren().add(stationViews.get("A10").getStationView());
		tile.getChildren().add(stationViews.get("A20").getStationView());
		tile.getChildren().add(stationViews.get("A30").getStationView());
		tile.getChildren().add(stationViews.get("A40").getStationView());
		tile.getChildren().add(stationViews.get("A50").getStationView());
		tile.getChildren().add(stationViews.get("A60").getStationView());
		addBlankGrids(1,tile);
		tile.getChildren().add(stationViews.get("Y10").getStationView());
		addBlankGrids(3,tile);
		tile.getChildren().add(stationViews.get("A70").getStationView());
		tile.getChildren().add(stationViews.get("A80").getStationView());
		tile.getChildren().add(stationViews.get("A90").getStationView());
		tile.getChildren().add(stationViews.get("S20").getStationView());
		tile.getChildren().add(stationViews.get("S40").getStationView());
		tile.getChildren().add(stationViews.get("S50").getStationView());
		/*
		 * int testgap = 0; for (StationView stationView : stationViews) {
		 * gridpanes.add(stationView.getStationView());
		 * pv.addAll(stationView.getPlatformViews()); if(testgap % 3 == 0){
		 * GridPane spacer = new GridPane(); HBox space = new HBox();
		 * space.setMinWidth(190); spacer.add(space,0,0,3,3);
		 * 
		 * tile.getChildren().add(space); }
		 * 
		 * tile.getChildren().add(stationView.getStationView()); testgap++; //
		 * stationView.getStationView().setGridLinesVisible(true); }
		 */

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
	
	private void addBlankGrids(int num,FlowPane flow){
		HBox space;
		for(int i=0; i < num; i++){
			space = new HBox();
			space.setMinWidth(190);
			flow.getChildren().add(space);
		}
	}

	private void shutdownApp() {
		server.shutdownServer();
		try {
			Thread.sleep(1000);
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

		} else {
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
