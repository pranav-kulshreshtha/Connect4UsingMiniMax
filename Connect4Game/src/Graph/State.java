package Graph;

import java.util.Arrays;
/**
 * class to represent a game state
 * @author Pranav Kulshreshtha
 *162 bytes
 */
public class State implements Comparable<Integer>{
	private int score;
	private int[][] stateMatrix;
	private boolean[][] right;
	private boolean[][] down;
	private boolean[][] dRight;
	private boolean[][] dLeft;
	
	public State() {
		this.score = 0;
		this.stateMatrix = new int[5][7];
		this.right = new boolean[5][7];
		this.down = new boolean[5][7];
		this.dRight = new boolean[5][7];
		this.dLeft = new boolean[5][7];
	}
	
	public State( int[][] otherMatrix ) {
		this.score = 0;
		this.stateMatrix = Arrays.stream(otherMatrix).map(int[]::clone).toArray(int[][]::new);
		this.right = new boolean[5][7];
		this.down = new boolean[5][7];
		this.dRight = new boolean[5][7];
		this.dLeft = new boolean[5][7];
	}
	
	//method to reset few properties of the state
	public void resetAll() {
		right = new boolean[5][7];
		down = new boolean[5][7];
		dRight = new boolean[5][7];
		dLeft = new boolean[5][7];
	}
	
	//metho to return the state matrix
	public int[][] getMatrix() {
		return stateMatrix;
	}
	
	//method to set the value of a certain location in the matrix and update the score
	public void setValue(int row, int col, int val) {
		stateMatrix[row][col] = val;
		setScore();
	}
	
	//helper method to determine score of the state based on winner id
	private int score(int winner) {
		return winner;
	}
	
	public int getScore() {
		return score;
	}
	
	//helper method to check for horizontal match
	private boolean checkMatchRight(int val, int serial, int r, int c) {
		
		if( stateMatrix[r][c] == val ) {
			right[r][c] = true;
			if( serial == 4 ) {
				return true;
			}
			else {
				return checkMatchRight(val, serial+1, r, c+1);
			}
		}
		
		return false;
	}
	
	//method to check if the state is a terminal draw
	public boolean isTerminalDraw() {
		for( int i = 0; i<7; i++ ) {
			if( stateMatrix[0][i] == 0 ) {
				return false;
			}
		}
		return score == 0;
	}
	
	//helper method to check for vertical match
	private boolean checkMatchDown(int val, int serial, int r, int c) {
				
		if( stateMatrix[r][c] == val ) {
			down[r][c] = true;
			if( serial == 4 ) {
				return true;
			}
			else {
				return checkMatchDown(val, serial+1, r+1, c);
			}
		}
		
		return false;
	}
	
	//helper method to check for diagonally rightwards match
	private boolean checkMatchDiagonalRight(int val, int serial, int r, int c) {
					
			if( stateMatrix[r][c] == val ) {
				dRight[r][c] = true;
				if( serial == 4 ) {
					return true;
				}
				else {
					return checkMatchDiagonalRight(val, serial+1, r+1, c+1);
				}
			}
			
			return false;
	}
		
	//helper method to check for diagonally rightwards match
	private boolean checkMatchDiagonalLeft(int val, int serial, int r, int c) {
			
				if( stateMatrix[r][c] == val ) {
					dLeft[r][c] = true;
					if( serial == 4 ) {
						return true;
					}
					else {
						return checkMatchDiagonalLeft(val, serial+1, r+1, c-1);
					}
				}
					
				return false;
	}
	
	//method to check for the victory of any of the players
	public int checkVictory() {
		resetAll();
		for( int i=0; i < 5; i++ ) {
			for( int j=0; j < 7; j++ ) {
				//System.out.println("Checking coordinate : "+i+" "+j);
				//checking if location is already visited, or is zero, or is ineligible to check
				if( !right[i][j] && stateMatrix[i][j] != 0 && j<=3 ) {
					//System.out.println("For right");
					right[i][j] = true;
					//checking for a 4-match horizontally rightwards
					if( checkMatchRight(stateMatrix[i][j], 2, i, j+1)  ) {
						//System.out.println("Right match");
						return stateMatrix[i][j];
					}	
				}
				
				if( !down[i][j] && stateMatrix[i][j] != 0 && i<=1 ) {
					//System.out.println("For down");
					down[i][j] = true;
					//checking for a 4-match vertically downwards
					if( checkMatchDown(stateMatrix[i][j], 2, i+1, j ) ) {
						//System.out.println("Down match");
						return stateMatrix[i][j];
					}
				}
				
				if( !dRight[i][j] && stateMatrix[i][j] != 0 && j<=3 && i<=1 ) {
					//System.out.println("For dRight");
					dRight[i][j] = true;
					//checking for a 4-match diagonally rightwards
					if( checkMatchDiagonalRight(stateMatrix[i][j], 2, i+1, j+1 ) ) {
						//System.out.println("DRight match");
						return stateMatrix[i][j];
					}
				}
				
				if( !dLeft[i][j] && stateMatrix[i][j] != 0 && j>=3 && i<=1 ) {
					//System.out.println("For dLeft");
					dLeft[i][j] = true;
					//checking for a 4-match diagonally leftwards
					if( checkMatchDiagonalLeft(stateMatrix[i][j], 2, i+1, j-1 ) ) {
						//System.out.println("DLeft match");
						return stateMatrix[i][j];
					}
				}
				
			}
		}
		return 0;
	}
	
	//method to set the score of this state
	private void setScore() {
		score = checkVictory();
	}
	
	//a simple helper method used for updating the
	public void updateScore(int s) {
		score = s;
	}
	
	public static void main(String[] arg) {
		int[][] array = {
				{1,1,1,2,0,0,0},
				{1,2,1,2,2,0,0},
				{1,2,1,2,2,1,0},
				{2,1,2,1,2,1,1},
				{2,2,1,2,1,2,2}
		};
		State state = new State(array);
		System.out.println( state.checkVictory() );
		
	}

	public int compareTo(Integer o) {
		return   (new Integer(score)).compareTo(o) ;
	}
}
