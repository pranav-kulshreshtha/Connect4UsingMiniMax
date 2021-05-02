package Game;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Class that holds a couple of SFX required in the main game so that they can be
 * referenced from here, using various methods
 * @author Admin
 *
 */
public class GameMusic {
	
	private static MediaPlayer returnMusic( String path ) {
		Media sfx = new Media( new File(path).toURI().toString() );
        return new MediaPlayer(sfx);
	}
	
	//plays the SFX needed when the player wins the game
	public static MediaPlayer sfxYouWin() {
        return returnMusic( "data/zapsplat_multimedia_game_sound_slot_machine_positive_mallet_tone_65509.mp3" );
	}
	
	//plays the SFX needed when the CPU wins the game
	public static MediaPlayer sfxCPUWins() {
        return returnMusic( "data/zapsplat_multimedia_game_sound_sci_fi_futuristic_failure_error_002_64994.mp3" );
	}
	
}
