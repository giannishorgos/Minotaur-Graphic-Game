package test;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingDeque;

public class PlayerGraphics {
    private final String pathWalk;
    private final String pathIdle;

    private final int IDLE_LENGTH;
    private final int WALK_LENGTH;

    private Image[] idle;
    private Image[] walking;

    Player player;
    //HeuristicPlayer heuristicPlayer;

    public final int BLOCK_SIZE;
    public final int N; //number of tiles
    private final int newOrigin;    // added to everything inside the labyrinth so is
                                    // like the new origin is the left upper corner of
                                    // labyrinth.

    private long totalTime;
    private long previousTime;
    private long deltaTime;

    public boolean init;

    private int x, y, dx, dy;
    private int x_tile, y_tile;
    private int currentFrame;
    private int currentFrame_walk;
    private int period;
    private int scaleX, scaleY;

    public PlayerGraphics(MyPanel panel, Board board, Player player, HeuristicPlayer heuristicPlayer, String pathWalk, String pathIdle, int walkLength, int idleLength, int scaleX, int scaleY) {
        this.pathWalk = pathWalk;
        this.pathIdle = pathIdle;

        WALK_LENGTH = walkLength;
        IDLE_LENGTH = idleLength;

        walking = new Image[WALK_LENGTH];
        idle = new Image[IDLE_LENGTH];

        init = true;
        totalTime = 0;

        N = panel.N;
        BLOCK_SIZE = panel.BLOCK_SIZE;
        newOrigin = panel.newOrigin;

        if(player != null) this.player = player;
        else this.player = heuristicPlayer;

        if(this.player.getPlayerId() == 1) { // Thesseus
            x = newOrigin + 0;
            y = newOrigin + (N - 1) * BLOCK_SIZE;
            //x = y = 0;
        }
        else { // Minotaurus
            x = newOrigin + (N / 2) * BLOCK_SIZE;
            y = newOrigin + (N / 2) * BLOCK_SIZE;
            //x = y = N / 2;
        }

        x_tile = this.player.getX();
        y_tile = this.player.getY();

        this.scaleX = scaleX;
        this.scaleY = scaleY;

        dx = dy = 0;
        currentFrame = 0;
        currentFrame = 0;
        period = 0;
        deltaTime = (long) ((0.1 / WALK_LENGTH) * 10e9); //duration of each asset in nanosec

        loadImage();
    }

    public void loadImage() {
        for(int i = 0; i < WALK_LENGTH; i++) {
            String path = pathWalk + (i < 10 ? "00" : "0") + i;
            ImageIcon temp = new ImageIcon(path + ".png");
            walking[i] = temp.getImage();
        }
        for(int i = 0; i < IDLE_LENGTH; i++) {
            String path = pathIdle + (i < 10 ? "00" : "0") + i;
            System.out.println(path);
            ImageIcon temp = new ImageIcon(path + ".png");
            idle[i] = temp.getImage();
        }
    }

    public void move(int id) {
        if(player.getName() == "Thesseus") player.move(id);
        else player.move(player.getTileId());
    }

    public void drawWalk(Graphics2D g2, MyPanel panel) {
        if(init) {
            this.previousTime = System.nanoTime();
            init = false;
        }
        long now = System.nanoTime();
        totalTime += now - previousTime;
        previousTime = now;

        if(totalTime > deltaTime) {
            totalTime -= deltaTime;
            period++;
            //moved right
            if(x_tile < player.getX()) {
                dx = -1;
            }
            //moved left
            else if(x_tile > player.getX()) {
                dx = 1;
            }
            //moved up
            else if(y_tile < player.getY()) {
                dy = 1;
            }
            //moved down
            else if(y_tile > player.getY()) {
                dy = -1;
            }
            //does not moved at all
            else if(x_tile == player.getX() && y_tile == player.getY()){
                dx = 0;
                dy = 0;
                if(player.getPlayerId() == 1)
                    panel.thesseusisWalking = false;
                else
                    panel.minoisWalking = false;
            }
            //checks if the player reached the tile
            if(period == BLOCK_SIZE - 1) {
                dx = 0;
                dy = 0;

                x_tile = player.getX();
                y_tile = player.getY();

                period = 0;
                if(player.getPlayerId() == 1)
                    panel.thesseusisWalking = false;
                else
                    panel.minoisWalking = false;
            }
            x += dy;
            y += dx;
            if(currentFrame_walk < WALK_LENGTH - 1) currentFrame_walk++;
            else currentFrame_walk = 0;
        }
        g2.drawImage(walking[currentFrame_walk], x, y - (scaleY), BLOCK_SIZE + scaleX, BLOCK_SIZE + scaleY, panel);
    }

    public void drawIdle(Graphics2D g2, MyPanel panel) {
        if(init) {
            this.previousTime = System.nanoTime();
            init = false;
        }
        long now = System.nanoTime();
        totalTime += now - previousTime;
        previousTime = now;
        if(totalTime > deltaTime) {
            totalTime -= deltaTime;
            if(currentFrame < IDLE_LENGTH - 1) currentFrame++;
            else currentFrame = 0;
        }
        g2.drawImage(idle[currentFrame], x, y - (scaleY), BLOCK_SIZE + scaleX, BLOCK_SIZE + scaleY, panel);
    }
}
