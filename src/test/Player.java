package test;

import test.Board;
import java.util.Random;

public class Player {
	protected int playerId;
    protected String name;
    protected Board board;
    protected int score; //every collected supply increases the score by one
    protected int x;
    protected int y;
    protected int tileid; //the current Tile Id of the tile where the player is
    
    //constructors
    public Player(){
        playerId = 0;
        name=null;
        board= new Board();
        score=0;
        x=0;
        y=0;
        tileid = 0;
    }
    public Player(int playerId, String name, Board board, int score, int x, int y){
        this.playerId = playerId;
        this.name= name;
        this.board = new Board(board);
        this.score = score;
        this.x = x;
        this.y = y;
        tileid = board.getN() * x + y;
    }

    public Player(Player player){
        this.playerId = player.getPlayerId();
        this.name= player.getName();
        this.board = player.getBoard();
        this.score = player.getScore();
        this.x = player.getX();
        this.y = player.getY();
        this.tileid = player.getTileId();
    }
    
    //Getters & Setters
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTileId() {
        return tileid;
    }

    public void setTileId(int tileid) {
        this.tileid = tileid;
    }
    /*
     * @description: in that functions the players are moving randomly
     * 
     * @param id: the current Tile Id of the tile where the player is
     * 
     * @return: an array of integers who contains, the new tileId where the player is moved, the x,y coordinates
     *  of the player and the supplyId of the supply that Thessus collected or -1 if there isn't any supply collected
     */
    int[] move(int id) {
        Random rand = new Random();
        int randomMove = rand.nextInt(4); //random integer for 0 to 4.	0: UP, 1: RIGHT, 2: DOWN, 3: LEFT
        
        /*for each random number, checks if the player can move in that direction, and if he can, updates
        the player's coordinates. Also, here is printing each move of the players*/
        switch (randomMove) {
            case 0:
                if (board.tiles[id].isUp()) {
                    System.out.println("Tries to move up, hits a wall.");
                } else {
                    x++;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves up.");
                }
                break;
            case 1:
                if (board.tiles[id].isRight()) {
                    System.out.println("Tries to move right, hits a wall.");
                } else {
                    y++;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves right.");
                }
                break;
            case 2:
                if (board.tiles[id].isDown()) {
                    System.out.println("Tries to move down, hits a wall.");
                } else {
                    x--;
                    tileid = x * board.getN() + y;
                    System.out.println("Moves down.");
                }
                break;
            case 3:
                if (board.tiles[id].isLeft()) {
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
}
