package test;

import test.Supply;
import test.Tile;
import java.util.Random;

public class Board {
    public int N; //the dimensions of the board (NxN)
    public int S; //number of the initial supplies on board
    public int W; //number of walls on board
    Tile[] tiles;
    Supply[] supplies;

    public Board() {
        this.N = 0;
        this.S = 0;
        this.W = 0;
        tiles = new Tile[W];
        supplies = new Supply[S];
    }
    public Board(int N, int S, int W) {
        this.N = N;
        this.S = S;
        this.W = W;
        tiles = new Tile[W];
        supplies = new Supply[S];
    }

    public Board(Board board) {
        this.N = board.N;
        this.S = board.S;
        this.W = board.W;
        this.tiles = board.tiles;
        this.supplies = board.supplies;
    }
    //Getters & Setters
    public int getN() {
        return N;
    }

    public int getS() {
        return S;
    }

    public int getW() {
        return W;
    }

    public Tile[] getTiles() { return tiles; }

    public void setN(int n) {
        N = n;
    }

    public void setS(int s) {
        S = s;
    }

    public void setW(int w) {
        W = w;
    }
    /* @description: this function place walls randomly but it checks if there are 2 walls already on that tile */
    public void createTile() {
        for(int j = 0; j < N; j++) {
            for(int i = 0; i < N; i++) {
                int id = i * N + j;
                int x = i;
                int y = j;
                boolean down = false;
                boolean up = false;
                boolean right = false;
                boolean left = false;
                int count = 0; //holds the number of walls on each tile
                
                /*place the margins of the board and checks if there is wall on the tile beneath or on the tile on the left
                and counts the total number of walls so a tile can't has more than 2 walls*/
                if(i == 0 || tiles[id - N].isUp() == true) {
                    down = true;
                    count++;
                }
                if(j == 0 || tiles[id - 1].isRight() == true) {
                    left = true;
                    count++;
                }
                if(i == N - 1) {
                    up = true;
                    count++;
                }
                if(j == N - 1) {
                    right = true;
                    count++;
                }
                for(int k = 0; k < 2; k++) {
                    if (count < 2) {
                        Random rand = new Random();
                        int wall = rand.nextInt(3); //generate a random integer from 0 to 3. 0: PLACE NO WALLS, 1: PLACE A WALL RIGHT, 2: PLACE A WALL UP
                        //Also in the corners where every tile has already 2 walls, we pass immediately on the next tile
                        switch (wall) {
                            case 0:
                                break;
                            case 1:
                            	if(j == N - 2 && (i == N - 1 || i == 0)) {
                            		break;
                            	}
                                right = true;
                                count++;
                                break;
                            case 2:
                            	if(i == N - 2 && (j == N - 1 || j == 0)) {
                            		break;
                            	}
                                up = true;
                                count++;
                                break;
                        }
                    }
                }
                //create the new tile with correct number of walls, because everything was checked before
                tiles[id] = new Tile(id, x , y, up, down, right, left);
            }
        }
    }
    
    /* @description: this function place supplies randomly but it checks if there is already a supply on the tile
     * or is the tile where Thesseus and Minotauros are at the beginning of the game */
    public void createSupply() {
        Random rand = new Random();
        int count = 0; //holds the number of supplies that are successfully placed on the board
        
        //loop until we generate randomly but correct S supplies
        while(count < S) {
        	//generate randomly the coordinates of the supply
            int x = rand.nextInt(N);
            int y = rand.nextInt(N);

            int supplyTileId = N * x + y; //calculate the TileId of the tile where the supply is placed
            boolean flag = false;
            
            //checks if there is already a supply on that tile, if there is the flag becomes TRUE
            for(int j = 0; j < count; j++) {
                if(supplies[j].getSupplyTileId() == supplyTileId) {
                    flag = true;
                    break;
                }
            }
            //if the supply is on the Thesseus's or Minotauros's beginning tile or there is already a supply on that
            //tile, doesn't create a new supply and is go back at the beginning.
            if((x == 0 && y == 0) || (x == N / 2 && y == N / 2) || flag) {
                continue;
            }
            //if the supply is ok we create a new supply and increase the number of total supplies
            supplies[count] = new Supply(count, x, y, supplyTileId);
            count++;
        }
    }
    
    //creates the board by calling the 2 functions
    public void createBoard() {
        createSupply();
        createTile();
    }
    /*
     * @description: this functions implements the graphical interface based on the array of tiles, supplies and
     * Thesseus and Minotauros movement.
     * @param: thesseusTile and minotaurTile are the TileIds of the tile where the 2 player are
     * @return a 2-dimensional String array who contains strings with horizontals, verticals and null walls 
     */
    public String[][] getStringRepresentation(int theseusTile, int minotaurTile) {
        String[][] printBoard = new String[2 * N + 1][N];
        //the types of wall that there are on the board
        String upWall = "+---";
        String leftWall = "|";
        String nullWall = "+   ";
        
        int tileX = 0; //because the i is not going from 0 to N, we need a new variable - tileX - to calculate correctly the TileId
        for(int i = 0; i < 2 * N; i++) {
            for(int j = 0; j < N; j++) {
                if(i % 2 == 0) { //when the i is even, puts horizontals walls and when is odd puts verticals walls
                	
                    if(i == 0 && j == 0) {

                        printBoard[0][0] = nullWall;
                    }
                    else if(tiles[tileX * N + j].isDown()) {
                        printBoard[i][j] = upWall;
                    }
                    else {
                        printBoard[i][j] = nullWall;
                    }
                }
                else {
                    int index = 0;
                    boolean flag = false;
                    /*checks if there is any supply on the current tile. If there is any, the flag becomes TRUE and
                    we save the supplyId on the var index. With these informations, prints the supply with the supplyId*/
                    for(int k = 0; k < S; k++) {
                        if(supplies[k].getSupplyTileId() == tileX * N + j) {
                            flag = true;
                            index = k;
                            break;
                        }
                    }
                    if(tiles[tileX * N + j].isLeft()) {
                        printBoard[i][j] = leftWall;
                    }
                    else {
                        printBoard[i][j] = " ";
                    }
                    /*puts Thesseus, Minotauros and the supply on the correct Tile and prints 4 spaces if
                    the tile is empty*/
                    if(tileX * N + j == minotaurTile) {
                        printBoard[i][j] += " M ";
                    }
                    else if(tileX * N + j == theseusTile) {
                        printBoard[i][j] += " T ";
                    }
                    else if(flag) {
                        printBoard[i][j] += " s" + index;
                    }
                    else {
                        printBoard[i][j] += "   ";
                    }
                }
            }
            if(i % 2 == 1)  tileX++;
 
        }
        //puts the walls on the last line
        for(int j = 0; j < N; j++) {
        	printBoard[2 * N][j] = upWall;
        }
        return  printBoard;
    }
}