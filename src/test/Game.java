/**
 * 
 * @author Ioan-Sebastian Horgos, AEM 10077, phone: 6946930146
 * @author Alexandros-Victor Doukoglou, AEM 10078, phone: 6972075934
 * 
 * @date 10 November 2020
 * @email_chorgkos chorgkos@ece.auth.gr
 * @email_doukoglou doukoglou@ece.auth.gr
 * 
 * @Description The first part of the University Project on Data Structures Lecture 2020
 *
 */
package test;

import test.Player;


public class Game {
	//variables
	private static int round;
	
	//constructors
    public Game(){
        round = 0;
    }
    public Game(int r){
        round = r;
    }

    
    
    public static void main(String[] args){
    	boolean minoWins = false; //check if Minotauros wins
        int N = 15; //the dimensions of the board (NxN)
        int S = 4; //number of the initial supplies on board
        int W = (N * N * 3 + 1) / 2; //max number of walls on board
        
        //creates the board with supplies and walls placed randomly
        Board board = new Board(N, S, W);
        board.createBoard();
        
        //creates the 2 players
        Player Thesseus = new Player(1, "Thesseus", board, 0, 0, 0);
        Player Minotauros = new Player(2, "Minotauros", board, 0, N / 2, N / 2);
        
        //prints the board before any move
        System.out.println("The initial status of the board");
        String[][] boardLayout = board.getStringRepresentation( Thesseus.getTileId(), Minotauros.getTileId());
        for(int i=0; i < 2 * N + 1; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(boardLayout[2 * N - i][j]);
            }
            if(i % 2 == 0){
                System.out.print("+ \n");
            }
            else{
                System.out.print("| \n");
            }
        }
        System.out.print("\n\n\n");
        
        //The game-loop
        while(round < 50){
            System.out.println("Round: " + round++);

            System.out.print("Thesseus: ");
            Thesseus.move(Thesseus.getTileId()); //Thesseus move
            if(Thesseus.getTileId() == Minotauros.getTileId()) { //checks if Minotauros wins
            	minoWins = true;
            }
            System.out.print("Minotauros: ");
            Minotauros.move(Minotauros.getTileId()); //Minotauros move
            if(Thesseus.getTileId() == Minotauros.getTileId()) { //checks again if Minotauros wins
            	minoWins = true;
            }
            
            //the board is printing
            boardLayout = board.getStringRepresentation(Thesseus.getTileId(), Minotauros.getTileId());
            if(round > 0) boardLayout[0][0] = "+---"; //put a down wall on the tile(0,0) after first round
            for(int i = 0; i < 2 * N + 1; i++) {
                for (int j = 0; j < N; j++) {
                    System.out.print(boardLayout[2 * N - i][j]);
                }
                if(i % 2 == 0){
                    System.out.print("+ \n");
                }
                else{
                    System.out.print("| \n");
                }
            }
            System.out.print("\n\n\n");
            //Prints the final Winner if there is any
            if(minoWins) {
            	System.out.print("~~~ Minotauros WINS !!! ~~~");
            	return;
            }
            if(Thesseus.getScore() == S) {
            	System.out.print("~~~ Thesseus WINS !!! ~~~");
            	return;
            }
        }
        System.out.print("~~~ THERE IS NO WINNER !!! ~~~");
    }
}
