package Game;

import java.io.File;
import java.io.FileInputStream;

import Graph.State;
import Graph.StateGraph;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainGame extends Application{
	private BorderPane pane;
	private BorderPane first;
	private StackPane mainPane;
	private double checkX = 184;
	private final int nMax = 6;
	private double initX = 207;
	private double initY = 120;
	private double finalY = 374;
	private double gap = 64.7;
	private char playerColor = 'y';
	private char altColor = 'g';
	private boolean hasCPU = true;
	private int difficulty = 3;
	Stage mainStage;
	Label turnMessage;
	MediaPlayer bgPlayer;
	
	private boolean playerTurn = true;
	private State current  = new State();
	private StateGraph graph = new StateGraph();
	//array to store the number of discs present in each row
	private int rows[] = new int[7];
	
	MainGame(char playerCol, boolean CPU, int diff) {
		this.playerColor = playerCol;
		this.hasCPU = CPU;
		this.difficulty = diff;
		this.altColor = playerColor == 'y' ? 'g' : 'y';
	}
	
	//helper method to add background music to the scene
	private void addSound() {
        String path = "data/Monplaisir_-_15_-_Bass.mp3";  
        Media bgMusic = new Media( new File(path).toURI().toString() );
        bgPlayer = new MediaPlayer(bgMusic);
        bgPlayer.play();
        //bgPlayer.setAutoPlay(true);
        bgPlayer.setOnEndOfMedia( new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("End of media");
				bgPlayer.seek( Duration.ZERO );
			}
        	
        } );
	}
	
	//helper method to return a pane containing the background of stage
	private BorderPane addBackground() {
		try {
		FileInputStream fs = new FileInputStream("data/pexels-henry-&-co-1939485.jpg");
		Image bgImage = new Image(fs);
		ImageView bgView = new ImageView(bgImage);
		bgView.setFitHeight(500);
		bgView.setFitWidth(800);
		
		BorderPane background = new BorderPane();
		background.getChildren().add(bgView);
		background.setBottom( new Label("IMAGE BORROWED FROM PEXELS.COM (HENRY & CO.)") );
		return background;
		}
		catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	//helper method to add a disc at the appropriate place 
	private void addDisc(int n, char color) throws Exception{
		//bgPlayer.play(); 
		if(rows[n] >= 5 ) {
			return;
		}
		double bias = 0;
		if( n == 6 ) {
			bias = 5.5;
		}
		else if( n == 1 ) {
			bias = -2;
		}
		Disc toAdd = new Disc(color, (gap*n) + initX + bias, initY, finalY - (61.7*rows[n]) );
		pane.getChildren().add( toAdd );
		current.setValue(4-rows[n], n,  color == playerColor ? 1 : -1 );
		System.out.println("Updated value");
		rows[n]++;
		checkForGameOver();
		if( playerTurn && hasCPU ) {
			toAdd.getTransition().setOnFinished( new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					try {
					playTurn();
					}catch(Exception e) { System.out.println(e);}
				}
				
			} );
		}
	}
	
	//method to check if the player has clicked at the right place to spawn a disc, and to spawn it if so
	private void checkMove(double mouseX) throws Exception{
		double res = mouseX - checkX;
		int n = 0;
		while( n <= nMax ) {
			if( n != 0 ) {
				res -= 66;
			}
			if( res < 0 ) {
				return;
			}
			if( res > 0 && res <= 41 ) {
				//adding disc if it's player 1's turn
				if( playerTurn ) {
					addDisc(n, playerColor);
				}
				// adding disc in case it's a P. vs P. and it's player 2's turn
				else if( !playerTurn && !hasCPU ) {
					addDisc(n, 'g');
				}
			}
			n++;
		}
	}
	
	//method to freeze the game at Game Over
	private void gameOver() {
		//EventHandler<MouseEvent> handler = MouseEvent::consume;
		//first.addEventFilter(MouseEvent.ANY, handler);
		displayPostGO();
	}
	
	//method to check if a Game Over occurred
	private void checkForGameOver() {
		//System.out.println("checking");
		if( current.isTerminalDraw() ) {
			GameMusic.sfxCPUWins().play();
			turnMessage.setText("Draw");
			gameOver();
		}
		else if( current.getScore() == 1) {
			GameMusic.sfxYouWin().play();
			turnMessage.setText("You win!");
			gameOver();
		}
		else if( current.getScore() == -1 ) {
			GameMusic.sfxCPUWins().play();
			turnMessage.setText("CPU wins!");
			System.out.println("CPU wins");
			gameOver();
		}
	}
	
	//helper method to make the CPU take its turn by running minimax algorithm
	private void playTurn() throws Exception{
		if( current.getScore() != 0 ) {
			return;
		}
		playerTurn = !playerTurn;
		turnMessage.setText("CPU's turn!");
		int move = graph.miniMax(current, 1, (difficulty +1) * 2, rows, false);
	    //Thread.sleep(2000);
		addDisc(move, altColor);
		playerTurn = !playerTurn;
		if( current.getScore() == 0 ) {
			turnMessage.setText("Player's turn!");
		}
	}
	
	//display options post gameover
	private void displayPostGO() {
		Button replayButton = new Button("Play again");
		Button mainMenu = new Button("Main Menu");
		replayButton.setMinWidth(70);
		//replayButton.setAlignment(Pos.CENTER_LEFT);
		mainMenu.setMinWidth(70);
		//mainMenu.setAlignment(Pos.CENTER_LEFT);
		VBox postGO = new VBox();
	
		replayButton.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				bgPlayer.stop();
				(new MainGame(playerColor, hasCPU, difficulty)).start(mainStage); }
				catch(Exception e) { System.out.println(e); }
			}
			
		} );
		
		mainMenu.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
				bgPlayer.stop();
				( new TitleScreen() ).start(mainStage);
				}
				catch(Exception e) {
					System.out.println(e);
				}
			}
			
		} );

		postGO.getChildren().addAll(replayButton, mainMenu);
		postGO.setSpacing(20);
		postGO.setAlignment(Pos.CENTER_LEFT);
		//first.setLeft(postGO);
		mainPane.getChildren().add(postGO);
	}

	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		mainStage = primaryStage;
		FileInputStream boardFPath = new FileInputStream("C:\\Users\\Admin\\New workspace\\Tutorial JavaFx\\data\\connect4board.png");
		Image board = new Image(boardFPath);
		ImageView boardView = new ImageView(board);
		boardView.setY(44);
		boardView.setX(2);
		boardView.setFitWidth(886);
		boardView.setFitHeight(390);
		
		first = new BorderPane();
		first.getChildren().add(boardView);
		turnMessage = new Label("Player's turn!");
		turnMessage.setFont( new Font("Bauhaus 93", 36) );
		turnMessage.setTextFill(Color.RED);
		first.setTop(turnMessage);
		first.setAlignment(turnMessage, Pos.TOP_CENTER);
		
		pane = new BorderPane();
		
		mainPane = new StackPane();
		mainPane.getChildren().addAll(addBackground(), pane, first);
		
		Scene gameScreen = new Scene(mainPane, 800, 500, Color.WHITE);
		primaryStage.setScene(gameScreen);
		primaryStage.setTitle("Connect 4");
		primaryStage.setResizable(false);
		primaryStage.show();
		addSound();
		
		//setting up the action for mouse-click event 
		first.setOnMouseClicked( new EventHandler<MouseEvent>() {

			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					if( playerTurn || (!playerTurn && !hasCPU) ) {
						checkMove(arg0.getX());
					}
				}
				catch(Exception e) {
					System.out.println(e);
				}
			}
			
		} );
		//displayPostGO();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
