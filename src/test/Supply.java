package test;

public class Supply {
	//variables
	private int supplyId;
	private int x;
	private int y;
	private int supplyTileId; //the current Tile Id of the tile where the supply is
	
	//constructors
    public Supply() {
        supplyId = 0;
        x = 0;
        y = 0;
        supplyTileId = 0;
    }
    public Supply(int supplyId, int x, int y, int supplyTileId) {
        this.supplyId = supplyId;
        this.x = x;
        this.y = y;
        this.supplyTileId = supplyTileId;
    }
    public Supply(Supply supply) {
        supplyId = supply.supplyId;
        x = supply.x;
        y = supply.y;
        supplyTileId = supply.supplyTileId;
    }
    //Getter & Setters
    public int getSupplyId() {
        return supplyId;
    }
    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
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
    public int getSupplyTileId() {
        return supplyTileId;
    }
    public void setSupplyTileId(int supplyTileId) {
        this.supplyTileId = supplyTileId;
    }
}
