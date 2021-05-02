package Game;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.TimeUnit;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Disc extends ImageView {
 	private double HEIGHT = 110;
	private double WIDTH = 260;
	PathTransition pathTransition;
	
	private MediaPlayer addSound() {
        String path = "data/multimedia_button_click_029.mp3";  
        Media sfx = new Media( new File(path).toURI().toString() );
        return new MediaPlayer(sfx);
	}
	
	public Disc(char disc, double x, double y, double finY) throws Exception{
		addSound();
		FileInputStream fs;
		if( disc == 'y' ) {
			 fs = new FileInputStream("data\\imageedit_4_3850179333.png");
		}
		else if( disc == 'g' ) {
			 fs = new FileInputStream("data\\imageedit_1_5891050241.png");
		}
		else {
			throw new IllegalArgumentException();
		}
		this.setImage( new Image(fs) );
		this.setFitHeight(HEIGHT);
		this.setFitWidth(WIDTH);
		this.setVisible(false);
		this.setX(x);
		this.setY(y);
		
		pathTransition = new PathTransition();  
	    pathTransition.setDuration(Duration.millis(finY * 2));  
	    pathTransition.setNode(this);  
	    pathTransition.setPath(new Line(x, y, x, finY)  );  
	    pathTransition.setCycleCount(1);  
	    pathTransition.play();  
	    this.setVisible(true);
	    addSound().play();  
	}
	
	public PathTransition getTransition() {
		return pathTransition;
	}
}
