import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import data.StationPlatformAndViewCache;
import fxviews.PlatformView;
import fxviews.StationView;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import protobuf.JdssAuditor;
import protobuf.JdssAuditor.DisplayData.AuditorMessageType;
import protobuf.JdssAuditor.DisplayData.Destination;
import protobuf.JdssAuditor.DisplayData.Eta;
import server.Server;

public class App extends Application {

	private final static Logger logger = LoggerFactory.getLogger("App");
	private boolean fullScreen = false;
	private static ArrayList<GridPane> gridpanes = new ArrayList<GridPane>();
	private static ArrayList<PlatformView> pv = new ArrayList<PlatformView>();
	private static Server server;

	private ScrollPane scroll;
	private static Double MAX_WIDTH;
	private static Double MAX_HEIGHT;
	private static boolean pause = false;

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
				if(pause) continue;
				AuditorMessageType type = message.getMessageType();
				Platform p = platformCache.get(message.getPlatform());
				if(null==p) {
					continue;
				}
				
				PlatformView platform = p.getPlatformView();
				
				logger.debug("Msg recieved type: {} text: {} ", type, message.toString());
				
				platform.configureMessage(message);
			
			} catch (InterruptedException e) {
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

	public static String buildDestinatoinMsg(Destination data) {
		StringBuilder destination = new StringBuilder("");
		int maxWidth = Math.max(data.getDestination().length(), data.getTrainLength().length());
		
		destination.append(String.format("%s\n", StringUtils.center(data.getDestination(), maxWidth)));
		destination.append(String.format("%s", StringUtils.center(data.getTrainLength(), maxWidth)));
		return destination.toString();
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Tile Pane Test");
		// TilePane tile = new TilePane();
		// tile.setPrefColumns(8);
		GridPane tile = new GridPane();

		MAX_WIDTH = tile.getWidth();
		MAX_HEIGHT = tile.getHeight();
		tile.setStyle("-fx-background-color: darkslategrey;");

		Map<String, StationView> stationViews = StationPlatformAndViewCache.INSTANCE.getStationViewCache();

		HBox row1 = new HBox();
		row1.setSpacing(2);

		row1.getChildren().add(stationViews.get("R60").getStationView());
		row1.getChildren().add(stationViews.get("R50").getStationView());
		row1.getChildren().add(stationViews.get("R40").getStationView());
		row1.getChildren().add(stationViews.get("R30").getStationView());
		row1.getChildren().add(stationViews.get("R20").getStationView());
		row1.getChildren().add(stationViews.get("R10").getStationView());
		row1.getChildren().add(stationViews.get("K30").getStationView());
		row1.getChildren().add(stationViews.get("C10").getStationView());
		row1.getChildren().add(stationViews.get("C20").getStationView());
		row1.getChildren().add(stationViews.get("C30").getStationView());
		row1.getChildren().add(stationViews.get("C40").getStationView());
		row1.getChildren().add(stationViews.get("C50").getStationView());
		row1.getChildren().add(stationViews.get("C60").getStationView());
		row1.getChildren().add(stationViews.get("C70").getStationView());
		row1.getChildren().add(stationViews.get("C80").getStationView());

		HBox row3 = new HBox();
		row3.setSpacing(2);
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(StationView.getBlankView());
		row3.getChildren().add(stationViews.get("K20").getStationView());

		HBox row4 = new HBox();
		row4.setSpacing(2);

		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(stationViews.get("K10").getStationView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(StationView.getBlankView());
		row4.getChildren().add(stationViews.get("L10").getStationView());
		row4.getChildren().add(stationViews.get("L20").getStationView());
		row4.getChildren().add(stationViews.get("L30").getStationView());

		HBox row5 = new HBox();
		row5.setSpacing(2);

		row5.getChildren().add(stationViews.get("M70").getStationView());
		row5.getChildren().add(stationViews.get("M60").getStationView());
		row5.getChildren().add(stationViews.get("M50").getStationView());
		row5.getChildren().add(stationViews.get("M40").getStationView());
		row5.getChildren().add(stationViews.get("M30").getStationView());
		row5.getChildren().add(stationViews.get("M20").getStationView());
		row5.getChildren().add(stationViews.get("M10").getStationView());
		row5.getChildren().add(stationViews.get("A10").getStationView());
		row5.getChildren().add(stationViews.get("A20").getStationView());
		row5.getChildren().add(stationViews.get("A30").getStationView());
		row5.getChildren().add(stationViews.get("A40").getStationView());
		row5.getChildren().add(stationViews.get("A50").getStationView());
		row5.getChildren().add(stationViews.get("A60").getStationView());
		row5.getChildren().add(stationViews.get("A70").getStationView());
		row5.getChildren().add(stationViews.get("A80").getStationView());
		row5.getChildren().add(stationViews.get("A90").getStationView());

		HBox row6 = new HBox();
		row6.setSpacing(2);
		row6.getChildren().add(stationViews.get("M80").getStationView());

		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(StationView.getBlankView());
		row6.getChildren().add(stationViews.get("S20").getStationView());
		row6.getChildren().add(stationViews.get("S40").getStationView());
		row6.getChildren().add(stationViews.get("S50").getStationView());

		HBox row8 = new HBox();
		row8.setSpacing(2);
		row8.getChildren().add(stationViews.get("M90").getStationView());
		row8.getChildren().add(stationViews.get("W10").getStationView());
		row8.getChildren().add(stationViews.get("W20").getStationView());
		row8.getChildren().add(stationViews.get("W30").getStationView());
		row8.getChildren().add(stationViews.get("W40").getStationView());

		HBox row7 = new HBox();
		row7.setSpacing(2);
		row7.getChildren().add(StationView.getBlankView());
		row7.getChildren().add(StationView.getBlankView());
		row7.getChildren().add(StationView.getBlankView());
		row7.getChildren().add(StationView.getBlankView());
		row7.getChildren().add(stationViews.get("Y10").getStationView());

		VBox boxtest = new VBox();
		boxtest.getChildren().addAll(row1, row3, row4, row5, row6, row8, row7);
		boxtest.setSpacing(2);
		tile.add(boxtest, 0, 0);

		for (Node n : tile.getChildren()) {
			GridPane.setMargin(n, new Insets(2, 2, 2, 2));

		}
		MAX_WIDTH = tile.getWidth();
		MAX_HEIGHT = tile.getHeight();

		scroll = new ScrollPane();
		scroll.setContent(tile);
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setPannable(true);
		// scroll.getStylesheets().add(App.class.getResource("style.css").toExternalForm());

		StackPane pane = new StackPane();
		pane.getChildren().add(scroll);

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		Scene scene = new Scene(pane, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();
		primaryStage.setOnCloseRequest(ce -> shutdownApp());

		JavaFxObservable.eventsOf(tile, MouseEvent.MOUSE_CLICKED).subscribe(mc -> handleMouseClick(mc));

		JavaFxObservable.eventsOf(scene, KeyEvent.KEY_RELEASED).map(KeyEvent::getCode)
				.subscribe(key -> handleKeyevent(key, primaryStage));

	}

	private void shutdownApp() {
		if (null != server) {
			server.shutdownServer();
			try {
				Thread.sleep(1000);
				System.exit(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.exit(0);
		}

	}

	private void handleKeyevent(KeyCode code, Stage primaryStage) {

		switch (code) {
		case SPACE:
			shutdownApp();
			break;
		case F11:
			primaryStage.setFullScreen(fullScreen = !fullScreen);
			break;
		default:
			break;

		}

	}

	private void handleMouseClick(MouseEvent event) {

		switch (event.getButton()) {
		case PRIMARY:
			// pane.getChildren().remove(scroll);
			//
			//
			// tile.setScaleX(tile.getScaleX() * 2);
			// tile.setScaleY(tile.getScaleY() * 2);
			//
			// tile.setMinWidth(tile.getWidth() * 2);
			// tile.setMinHeight(tile.getHeight() * 2);
			// tile.setTranslateX(tile.getWidth());
			// tile.setTranslateY(tile.getHeight());
			//
			// testBox = new HBox();
			// testBox.setAlignment(Pos.CENTER);
			// scroll = new ScrollPane();
			// testBox.getChildren().add(tile);
			// scroll.setContent(testBox);
			// pane.getChildren().add(scroll);
			break;
		case SECONDARY:
			//pause = !pause;
			// pane.getChildren().remove(scroll);
			// tile.setScaleX(1);
			// tile.setScaleY(1);
			// tile.setMinWidth(MAX_WIDTH);
			// tile.setMinHeight(MAX_HEIGHT);
			// tile.setTranslateX(0);
			// tile.setTranslateY(0);
			//
			// testBox = new HBox();
			// testBox.setAlignment(Pos.CENTER);
			// scroll = new ScrollPane();
			// testBox.getChildren().add(tile);
			// scroll.setContent(testBox);
			//
			// pane.getChildren().add(scroll);
			break;
		default:
			break;

		}

	}

}
