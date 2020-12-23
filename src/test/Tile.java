package test;

public class Tile {
	//variables
	int tileId;
    int x;
    int y;
    boolean up;
    boolean down;
    boolean left;
    boolean right;

    //constructors
    public Tile() {
        tileId = 0;
        x = 0;
        y = 0;
        up = false;
        down = false;
        right = false;
        left = false;
    }
    public Tile(int tileId, int x, int y, boolean up, boolean down, boolean right, boolean left) {
        this.tileId = tileId;
        this.x = x;
        this.y = y;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
    }
    public Tile(Tile tile) {
        tileId = tile.tileId;
        x = tile.x;
        y = tile.y;
        up = tile.up;
        down = tile.down;
        right = tile.right;
        left = tile.left;
    }
    
    //Getters & Setters
    public int getTileId() {
        return tileId;
    }
    public void setTileId(int tileId) {
        this.tileId = tileId;
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
    public boolean isUp() {
        return up;
    }
    public void setUp(boolean up) {
        this.up = up;
    }
    public boolean isDown() {
        return down;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public boolean isRight() {
        return right;
    }
    public void setRight(boolean right) {
        this.right = right;
    }
}
