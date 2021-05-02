package Graph;

import java.util.Arrays;

public class StateGraph {
	int turn = 0;
	
	public void printMatrix(int[][] matrix) {
		for( int[] arr : matrix ) {
			System.out.println( Arrays.toString(arr) );
		}
		System.out.println();
	}
	
	//method implementing the MiniMax algorithm to calculate the next move for the game
	public  int miniMax( State state, int depth, int MAX_DEPTH, int[] rows, boolean max) {
		if( depth == MAX_DEPTH ) {
			return state.getScore();
		}
		if( state.getScore() != 0 ) {
			return state.getScore();
		}
		int sum = 0;
		int next = max ? -100 : 100;
		int next_index = 3;
		for( int i = 0; i<7; i++ ) {
			if( rows[i] <= 4 ) {
				State child = new State(state.getMatrix());
				int[] childRows = Arrays.copyOf(rows, rows.length);
				child.setValue(4-rows[i], i, max ? 1 : -1 );
				childRows[i]++;
				
				if( depth == MAX_DEPTH - 1 ) {
					int sc = miniMax(child, depth+1, MAX_DEPTH, childRows, !max);
					if( (max && sc > next) || (!max && sc < next) ) {
						next = child.getScore();
						next_index = i;
					}
					sum += sc;
					
				}
			   else {
				   miniMax(child, depth+1, MAX_DEPTH, childRows, !max);
				   if( (max && child.getScore() > next) || (!max && child.getScore() < next) ) {
					   next = child.getScore();
					   next_index = i;
				}
			}
			}
		}
		
		if( sum!= 0 ) {
			state.updateScore(sum);
		}
		else {
			state.updateScore(next);
		}
		if( next_index == 0 && next == 0 ) {
			//System.out.println("three");
			int move = (new java.util.Random()).nextInt(3) + 2;
			if( rows[move] < 5 ) {
				return (new java.util.Random()).nextInt(3) + 2 ;
			}
			else { for( int i = 0; i < 7; i++ ) { if( rows[i] < 5 ) {return i;} } }
		}
		return next_index;
	}
	
	public static void main(String[] args) {
		//testing the class for minimax() method
		int[][] arr = { 
						{0, 0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0, 0},
						{0, 0, 0, 0, 0, 0, 0},
						{0, 1, 1, 1, -1, 0, 0}};
		State state = new State(arr);
		int[] row = {0, 1, 1, 1, 1, 0, 0 };
		StateGraph graph = new StateGraph();
		System.out.println( graph.miniMax(state, 1, 3, row, false) );
	}
 }
