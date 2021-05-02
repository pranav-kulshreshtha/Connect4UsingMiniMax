package Game;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitleScreen extends Application {
	private long defWidth = 800;
	private long defHeight = 500;
	private char startingColor = 'y';
	private int difficulty = 3;
	private Label title;
	MediaPlayer bgPlayer;
	
	
	//helper method to add background music to the scene
	private void addSound() {
        String path = "data/Komiku_-_05_-_Surfing.mp3";  
        Media bg = new Media( new File(path).toURI().toString() );
        bgPlayer = new MediaPlayer(bg);
        bgPlayer.setAutoPlay(true);
        bgPlayer.setOnEndOfMedia( new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("End of media");
				bgPlayer.seek( Duration.ZERO );
			}
        	
        } );
	}
	
	private void customFontSettings( ObservableList<javafx.scene.Node> list) throws Exception{
		for( javafx.scene.Node node : list ) {
			try {
			((javafx.scene.control.Labeled)node).setFont( new Font("Elephant", 17) );
			((javafx.scene.control.Labeled)node).setTextFill(Color.SADDLEBROWN);
			}
			catch(Exception e) {continue;}
		}
	}
	
	private EventHandler<ActionEvent> launchEvent(Stage mainStage, Slider col, ToggleGroup toggle){
		return new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				difficulty = (int)col.getValue();
				String color =  ((RadioButton)toggle.getSelectedToggle()).getText() ;
				if( color.equals("Yellow") ) {
					startingColor = 'y';
				}
				if( color.equals("Green") ) {
					startingColor = 'g';
				}
					
				try {
					bgPlayer.stop();
					(new MainGame(startingColor, true, difficulty) ).start(mainStage);
				}
				catch(Exception e) {
					System.out.println(e);
					mainStage.close();
				}
				}
			};
			
	}
 	
	private void showInitSettings(VBox vb, Stage mainStage) throws Exception{
		Slider diff = new Slider(1, 3, 2);
		ToggleGroup colorSelector = new ToggleGroup();
		RadioButton redChoice = new RadioButton("Yellow");
		RadioButton blueChoice = new RadioButton("Green");
		redChoice.setToggleGroup(colorSelector);
		blueChoice.setToggleGroup(colorSelector);
				
		FlowPane diffPrompt = new FlowPane();
		FlowPane colorPrompt = new FlowPane();
		diffPrompt.getChildren().addAll( new Label("Game difficulty :    Low "), diff, new Label("    High") );
		diffPrompt.setAlignment(Pos.CENTER);
		colorPrompt.getChildren().addAll( new Label("Disc color :   "), redChoice, blueChoice );
		colorPrompt.setAlignment(Pos.CENTER);
		
		customFontSettings( diffPrompt.getChildren() );
		customFontSettings( colorPrompt.getChildren() );
		
		Button launch = new Button("Launch");
		launch.setPrefWidth(300);
		launch.setAlignment(Pos.CENTER);
		launch.setOnAction( launchEvent(mainStage, diff, colorSelector) );
		
		vb.getChildren().addAll(diffPrompt, colorPrompt, launch);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//adding the background image of the title screen
		addSound();
		FileInputStream fs = new FileInputStream("data/pexels-pixabay-262488.jpg");
		Image bgImage = new Image(fs);
		ImageView bgView = new ImageView(bgImage);
		bgView.setFitHeight(defHeight);
		bgView.setFitWidth(defWidth);
		
		title = new Label("CONNECT - 4!");
		title.setFont( new Font("Snap ITC", 60) );
		title.setAlignment(Pos.CENTER);
		title.setTextFill(Color.ROYALBLUE);
		title.setBackground( new Background( new BackgroundFill(Color.WHITESMOKE, null, null) ) );
		
		//setting the background image into a BorderPane
		BorderPane background = new BorderPane();
		background.getChildren().add(bgView);
		background.setAlignment(title, Pos.TOP_CENTER);
		background.setBottom( new Label("IMAGE BORROWED FROM PEXELS.COM") );
		background.setTop(title);
		
		//setting all the buttons on a VBox
		VBox content = new VBox();
		content.setSpacing(40);
		Button gameCPU = new Button("Player vs CPU");
		gameCPU.setPrefWidth(300);
		gameCPU.setWrapText(true);
		content.getChildren().add(gameCPU);
		gameCPU.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				content.getChildren().clear();
				showInitSettings(content, primaryStage);
				//(new MainGame('y', true) ).start(primaryStage);
				}
				catch(Exception e) { System.out.println(e); }
			}
			
		} );
		content.setAlignment(Pos.CENTER);
		
		StackPane mainPane = new StackPane();
		mainPane.getChildren().addAll(background, content);
		
		Scene scene = new Scene(mainPane, defWidth, defHeight);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect 4");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
