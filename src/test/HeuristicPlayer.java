package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HeuristicPlayer extends Player {
    private ArrayList<Integer[]> path;
    private double movepoints; // the points he collects 
    private int[] supplydistance; // is an array of 4 integers because holds the distance for supplies in the four directions 
    private int[] minosdistance; // is an array of 4 integers because holds the distance for supplies in the four directions 

	// constructors
    public HeuristicPlayer(){
        super();
        path = new ArrayList<Integer[]>();
        movepoints = 0;
        supplydistance = new int[4];
        minosdistance = new int[4];
    }
    
    public HeuristicPlayer(int playerId, String name, Board board, int score, int x, int y) {
    	super( playerId, name, board, score, x, y);
    	path = new ArrayList<Integer[]>();
        movepoints = 0;
        supplydistance = new int[4];
        minosdistance = new int[4];
    }
	
	/*
	 * @description calculates the points as is at the current tile and calls recursively his self on the next tile with the 
	 * 				same dice and increases the n by one. In that way, the function caluculates the points until n = 3, 3 tiles
	 * 				away for the initial current position
	 * 
	 * @param	currentPos: the current position of the player
	 * 			dice: the four directions 0: UP,  1: RIGHT 2: DOWN, 3: LEFT
	 * 			minosTile: the tile ID of the tile where minotaurus is
	 * 			n: tiles away for the initial current position
	 * 
	 * @return the points based on the supplies he founds, the distance for minotaurus and if there are walls along his way
	 *
	 */
    public double evaluate(int currentPos, int dice, int minosTile, int n) {
    	double sum = 0;
    	switch( dice){
    	case 0: // UP
    		if( board.tiles[currentPos].isUp()) {
    				sum -=2;
    			break;
    		}
    		for( int i=0; i<board.getS(); i++) {
    			if( board.supplies[i].getSupplyTileId() == currentPos + board.getN()) {
    				sum += 3;
    				supplydistance[dice] = n;
    			}
    		}
    		if( minosTile == currentPos + board.getN()) {
    			sum -= 6;
    			minosdistance[dice] = n;
    		}
        	n++;
			if( n<4) { // n can gets values from 1 to 3 cause the player can see only 3 tiles ahead from him
				
				/* divides the points that collects with n (tiles away from the player) so player gets 
				 * more points if the supply is near him and fewer if the supply is far away. After this
				 * we add the points in the movepoints.
				 */
				movepoints += evaluate( currentPos +  board.getN(), dice, minosTile, n) / (float)n; 
			}
			break;
    	case 1: // RIGHT
    		if( board.tiles[currentPos].isRight()) {
    			sum -=2;
    			break;
    		}
    		for( int i=0; i<board.getS(); i++) {
    			if( board.supplies[i].getSupplyTileId() == currentPos + 1) {
    				sum += 3;
    				supplydistance[dice] = n;
    			}
    		}
    		if( minosTile == currentPos + 1) {
    			sum -= 6;
    			minosdistance[dice] = n;
    		}
        	n++;
			if( n<4) { // n can gets values from 1 to 3 cause the player can see only 3 tiles ahead from him
				
				/* divides the points that collects with n (tiles away from the player) so player gets 
				 * more points if the supply is near him and fewer if the supply is far away. After this
				 * we add the points in the movepoints.
				 */
        		movepoints += evaluate( currentPos + 1, dice, minosTile, n) / (float)n;
        	}
    		break;
    	case 2: //DOWN
    		if( board.tiles[currentPos].isDown()) {
    			sum -=2;
    			break;
    		}
    		for( int i=0; i<board.getS(); i++) {
    			if( board.supplies[i].getSupplyTileId() == currentPos - board.getN()) {
    				sum += 3;
    				supplydistance[dice] = n;
    			}
    		}
    		if( minosTile == currentPos - board.getN()) {
    			sum -= 6;
    			minosdistance[dice] = n;
    		}
        	n++;
			if(n<4) { // n can gets values from 1 to 3 cause the player can see only 3 tiles ahead from him
				
				/* divides the points that collects with n (tiles away from the player) so player gets 
				 * more points if the supply is near him and fewer if the supply is far away. After this
				 * we add the points in the movepoints.
				 */
        		movepoints += evaluate( currentPos -  board.getN(), dice, minosTile, n) / (float)n;
        	}
    		break;
    	case 3: //LEFT
    		if( board.tiles[currentPos].isLeft()) {
    			sum -= 2;
    			break;
    		}
    		for( int i=0; i<board.getS(); i++) {
    			if( board.supplies[i].getSupplyTileId() == currentPos - 1) {
    				sum += 3;
    				supplydistance[dice] = n;
    			}
    		}
    		if( minosTile == currentPos - 1) {
    			sum -= 6;
    			minosdistance[dice] = n;
    		}
        	n++;
			if(n < 4) { // n can gets values from 1 to 3 cause the player can see only 3 tiles ahead from him
				
				/* divides the points that collects with n (tiles away from the player) so player gets 
				 * more points if the supply is near him and fewer if the supply is far away. After this
				 * we add the points in the movepoints.
				 */
        		movepoints += evaluate( currentPos - 1, dice, minosTile, n) / (float)n;
        	}
    		break;
    		
    	} 
    	return sum + movepoints;
    }
	/*
	 * @description this functions calls the evaluate for all four directions and saves the results in a map.
	 * 				Then finds the biggest value in the map and return the direction that corresponds with the biggest value
	 * 
	 * @param	currentPos: the current position of the player
	 * 			minosTile: the tile ID of the tile where minotaurus is
	 * 
	 * @return an integer for 0 to 3 which corresponds to the four directions 0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
	 */
    public int getNextMove(int currentPos, int minosTile) {
    	Map <String, Double> availableMoves = new HashMap<String, Double>();
    	
		movepoints = 0; // erase the previous values for movepoints
    	availableMoves.put("Up", evaluate(currentPos, 0, minosTile, 1));
		movepoints = 0;
    	availableMoves.put("Right", evaluate(currentPos, 1, minosTile, 1));
		movepoints = 0;
    	availableMoves.put("Down", evaluate(currentPos, 2, minosTile, 1));
		movepoints = 0;
    	availableMoves.put("Left", evaluate(currentPos, 3, minosTile, 1));
    	
    	// checks if the same as last move and if it is, decreases the points in that direction so the player avoid to go in that direction again
    	if( path.size() != 0) {
    		switch( path.get(path.size() -1)[0]) {
    		case 0:
    			availableMoves.replace("Down", availableMoves.get("Down"), availableMoves.get("Down") -1);
    			break;
    		case 1: 
    			availableMoves.replace("Left", availableMoves.get("Left"), availableMoves.get("Left") -1);
    			break;
    		case 2:
    			availableMoves.replace("Up", availableMoves.get("Up"), availableMoves.get("Up") -1);
    			break;
    		case 3:
    			availableMoves.replace("Right", availableMoves.get("Right"), availableMoves.get("Right") -1);
    			break;
    		}
    	}
    	 	

    	double max=-99999;
		String key = "";
		// finds the max value in the map
    	for( String srt: availableMoves.keySet()) {
    		if( availableMoves.get(srt) > max) {
    			max = availableMoves.get(srt);
    			key = srt;
    		}
    	}
		
		// return an integer that corresponds with the directions with the biggest value
    	switch( key){
    		case "Up":
    			path.add(new Integer[] { 0, supplydistance[0]==1 ? 1 : 0, supplydistance[0], minosdistance[0]});
    			clearArray();
    			return 0;
    		case "Right":
    			path.add(new Integer[] { 1, supplydistance[1]==1 ? 1 : 0, supplydistance[1], minosdistance[1]});
    			clearArray();
    			return 1;
    		case "Down":
    			path.add(new Integer[] { 2, supplydistance[2]==1 ? 1 : 0, supplydistance[2], minosdistance[2]});
    			clearArray();
    			return 2;
    		default:
    			path.add(new Integer[] { 3, supplydistance[3]==1 ? 1 : 0, supplydistance[3], minosdistance[3]});
    			clearArray();
				return 3;
    	}
    	
    }
	
	/*
     * @description: in that functions the players are moving randomly
     * 
     * @param 	id: the current Tile Id of the tile where the player is
	 * 			minosTile: the tile ID of the tile where minotaurus is
     * 
     * @return: an array of integers who contains, the new tileId where the player is moved, the x,y coordinates
     *  of the player and the supplyId of the supply that Thessus collected or -1 if there isn't any supply collected
     */
    int[] move(int minosTile) {
        
    	int bestMove = getNextMove(tileid, minosTile);
        switch (bestMove) {
            case 0:
                if (board.tiles[tileid].isUp()) {
                    System.out.println("Tries to move up, hits a wall.");
                } else {
                    x++;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves up.");
                }
                break;
            case 1:
                if (board.tiles[tileid].isRight()) {
                    System.out.println("Tries to move right, hits a wall.");
                } else {
                    y++;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves right.");
                }
                break;
            case 2:
                if (board.tiles[tileid].isDown()) {
                    System.out.println("Tries to move down, hits a wall.");
                } else {
                    x--;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves down.");
                }
                break;
            case 3:
                if (board.tiles[tileid].isLeft()) {
                    System.out.println("Tries to move left, hits a wall.");
                } else {
                    y--;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves left.");
                }
                break;
        }
        /*Checks if there is any supply on the tile where Thesseus is (with playerId = 1), and if there is any, 
         * increases the score by one and changes the supply coordinates and Supply Tile Id to -1 and in that way 
         * the supply is disappearing from the board*/
        int Sid=-1; //the id of the supply
        if (playerId == 1) {
            for (int i = 0; i < board.getS(); i++) {
                if (board.supplies[i].getSupplyTileId() == x * board.getN() + y) {
                    Sid = board.supplies[i].getSupplyId();
                    System.out.println("Collected supply No:" + Sid);
                    board.supplies[i].setX(-1);
                    board.supplies[i].setY(-1);
                    board.supplies[i].setSupplyTileId(-1);
                    score++;
                }
            }
        }
        int[] temp =  {x * board.getN() + y, x, y, Sid};
        return temp;
    }
	
	/*
	 * @description	when the game finishes, print a resume of the player's moves that contains
	 * 				the direction of the move, if he collects a supply, the distance for supplies or the Minotaurus
	 * 				and the totals supplies he collects, all that for every round
	 */
    public void statistics() {
    	int timesUp=0, timesRight=0, timesDown=0, timesLeft=0, suppliesCollected=0;
    	for( int i=0; i< path.size(); i++ ) {
    		System.out.println( "\nRound: " +i);
    		switch( path.get(i)[0]) {
    			case 0: 
    				System.out.print("Player moved Up.");
    				timesUp++;
    				break;
    			case 1:
    				System.out.print("Player moved Right.");
    				timesRight++;
    				break;
    			case 2:
    				System.out.print("Player moved Down.");
    				timesDown++;
    				break;
    			case 3:
    				System.out.print("Player moved Left.");
    				timesLeft++;
    				break;
    		}
    		if( path.get(i)[1] == 1) {
    			suppliesCollected++;
    			System.out.print(" He collected a supply. Supplies Collected: " + suppliesCollected + ".");
    		}
    		if( path.get(i)[2] == 0) {
    			System.out.print(" But he can't spot a supply.");
    		}
    		else {
    			System.out.print(" Closest supply is " + path.get(i)[2] + " tiles away.");
    		}
    		if( path.get(i)[3] == 0) {
    			System.out.print(" He can't spot Minotauros.");
    		}
    		else {
    			System.out.print(" Minotauros is " + path.get(i)[3] + " tiles away.");
    		}
    	}
    	System.out.println("\nTimes the player moved up: " + timesUp);
    	System.out.println("Times the player moved right: " + timesRight);
    	System.out.println("Times the player moved down: " + timesDown);
    	System.out.println("Times the player moved left: " + timesLeft);
	}
	
	/*
	 * @description clears the arrays supplydistance and minosdistance from previous values
	 */
	public void clearArray() {
		for(int i = 0; i < 4; i++) {
			supplydistance[i] = 0;
			minosdistance[i] = 0;
		}
	}
}
