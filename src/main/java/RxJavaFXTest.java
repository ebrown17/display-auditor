import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import data.Platform;
import data.StationCache;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.observers.JavaFxObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Application;
import javafx.beans.binding.Binding;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class RxJavaFXTest extends Application {
	private final static Logger logger = LoggerFactory.getLogger("RxJavaFXTest");
	private final Button incrementBttn;
	private final Label incrementLabel;
	private final Binding<String> binding1;
	private static ListView<String> listView = new ListView<>();
	private final TextField textInput;
	private final Label flippedTextLabel;
	private final Binding<String> binding2;
	int count = 0;
	private final Spinner<Integer> spinner;
	private Label spinnerChangesLabel;
	private Disposable disposable;

	public static void main(String... args) {
		new Thread(() -> {
			launch(args);
		}).start();

		Map<String, ArrayList<Platform>> stations = StationCache.INSTANCE.getStationCache();

		Observable<Entry<String, ArrayList<Platform>>> source2 = Observable.fromIterable(stations.entrySet());
		
		source2.subscribe((station) -> logger.info("Station: {} Platforms: {}", station.getKey(), station.getValue()));

		Observable<Integer> source = Observable.range(1, 5).subscribeOn(Schedulers.newThread());
		// Subscriber 1
		source.subscribe(i -> System.out
				.println("Subscriber 1 receiving " + i + " on thread " + Thread.currentThread().getName()));
		// Subscriber 2
		source.subscribe(i -> System.out
				.println("Subscriber 2 receiving " + i + " on thread " + Thread.currentThread().getName()));

		Single<List<String>> test = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon").delay(3, TimeUnit.SECONDS, Schedulers.io()).toList();

		test.observeOn(JavaFxScheduler.platform()).subscribe(list -> listView.getItems().setAll(list));

		

	}

	public RxJavaFXTest() {

		// initialize increment
		// demoTurns button events into Binding
		incrementBttn = new Button("Increment");
		incrementLabel = new Label("");

		Observable<ActionEvent> bttnEvents = JavaFxObservable.actionEventsOf(incrementBttn);

		binding1 = bttnEvents.map(e -> 1).scan(0, (x, y) -> x + y).map(Object::toString).to(JavaFxObserver::toBinding);

		incrementLabel.textProperty().bind(binding1);

		// initialize text flipper
		// Schedules on computation Scheduler for text flip calculation
		// Then resumes on JavaFxScheduler thread to update Binding
		textInput = new TextField();
		flippedTextLabel = new Label();

		Observable<String> textInputs = JavaFxObservable.valuesOf(textInput.textProperty());

		binding2 = textInputs.observeOn(Schedulers.computation()).map(s -> new StringBuilder(s).reverse().toString())
				.observeOn(JavaFxScheduler.platform()).to(JavaFxObserver::toBinding);

		flippedTextLabel.textProperty().bind(binding2);

		// initialize Spinner value changes
		// Emits Change items containing old and new value
		// Uses RxJava Subscription instead of Binding just to show that option
		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
		spinner = new Spinner<>();
		spinner.setValueFactory(svf);
		spinner.setEditable(true);

		spinnerChangesLabel = new Label();
		disposable = JavaFxObservable.changesOf(spinner.valueProperty())
				.map(change -> "OLD: " + change.getOldVal() + " NEW: " + change.getNewVal())
				.subscribe(s -> spinnerChangesLabel.setText(s));

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		VBox vBox = new VBox();
		Button button = new Button("Press Me");
		Label countLabel = new Label("0");

		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100);
		Spinner<Integer> spinner = new Spinner<>();
		spinner.setValueFactory(svf);
		spinner.setEditable(true);

		spinnerChangesLabel = new Label();
		disposable = JavaFxObservable.changesOf(spinner.valueProperty())
				.map(change -> "OLD: " + change.getOldVal() + " NEW: " + change.getNewVal())
				.subscribe(s -> spinnerChangesLabel.setText(s));

		JavaFxObservable.actionEventsOf(button).map(ae -> 2).scan(1, (x, y) -> x * y)
				.subscribe(clickCount -> countLabel.setText(clickCount.toString()));

		JavaFxObservable.eventsOf(listView, KeyEvent.KEY_TYPED).map(KeyEvent::getCharacter)
				.filter(s -> s.matches("[0-9]")).subscribe(s -> listView.getSelectionModel().select(s));

		Label positionLabel = new Label();
		Rectangle rectangle = new Rectangle(200, 200);
		rectangle.setFill(Color.RED);

		JavaFxObservable.eventsOf(rectangle, MouseEvent.MOUSE_CLICKED).map(me -> Color.ROYALBLUE)
				.subscribe(rectangle::setFill);

		JavaFxObservable.eventsOf(rectangle, MouseEvent.MOUSE_ENTERED).map(me -> Color.ROYALBLUE)
				.subscribe(rectangle::setFill);

		JavaFxObservable.eventsOf(rectangle, MouseEvent.MOUSE_EXITED).map(me -> Color.RED)
				.subscribe(rectangle::setFill);

		Label label = new Label("Input a 6-character String");

		TextField input = new TextField();
		Button button1 = new Button("Proceed");

		JavaFxObservable.valuesOf(input.textProperty()).map(s -> s.length() != 6)
				.subscribe(b -> button1.disableProperty().setValue(b));

		vBox.getChildren().addAll(countLabel, button, spinner, spinnerChangesLabel, listView, positionLabel, rectangle,
				label, input, button1);
		primaryStage.setScene(new Scene(vBox));
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();

		binding1.dispose();
		binding2.dispose();
		disposable.dispose();
	}
}
