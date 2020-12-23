package test;

import javax.swing.*;
import java.awt.*;

public class MinoGraphics {
    private final String pathToMinotaurWalk = "src/test/Minotaur_animations/Minotaur_01/PNG Sequences/Walking/Minotaur_01_Walking_";
    private final String pathToMinotaurIdle = "src/test/Minotaur_animations/Minotaur_01/PNG Sequences/Idle/Minotaur_01_Idle_";

    private final int MINO_IDLE_LENGTH = 12;
    private final int MINO_WALK_LENGTH = 18;

    private Image[] minotaurIdle;
    private Image[] minotaurWalking;

    Player minotaurus;

    public final int BLOCK_SIZE;
    public final int N; //number of tiles
    private final int newOrigin;    // added to everything inside the labyrinth so is
                                    // like the new origin is the left upper corner of
                                    // labyrinth.

    private long totalTime;
    private long previousTime;
    private long deltaTime = (long) ((0.1 / MINO_IDLE_LENGTH) * 10e9); //duration of each asset in nanosec

    public boolean init;

    private int x, y;
    private int currentFrame;

    public MinoGraphics(MyPanel panel, Board board) {
        minotaurWalking = new Image[MINO_WALK_LENGTH];
        minotaurIdle = new Image[MINO_IDLE_LENGTH];

        init = true;
        totalTime = 0;

        N = panel.N;
        BLOCK_SIZE = panel.BLOCK_SIZE;
        newOrigin = panel.newOrigin;

        minotaurus = new Player(2, "Minotauros", board, 0, N / 2, N / 2);


        x = newOrigin + N / 2 * BLOCK_SIZE;
        y = newOrigin + N / 2 * BLOCK_SIZE;
        currentFrame = 0;

        loadImage();
    }

    public void loadImage() {
        for(int i = 0; i < MINO_WALK_LENGTH; i++) {
            String path = pathToMinotaurWalk + (i < 10 ? "00" : "0") + i;
            ImageIcon temp = new ImageIcon(path + ".png");
            minotaurWalking[i] = temp.getImage();
        }
        for(int i = 0; i < MINO_IDLE_LENGTH; i++) {
            String path = pathToMinotaurIdle + (i < 10 ? "00" : "0") + i;
            ImageIcon temp = new ImageIcon(path + ".png");
            minotaurIdle[i] = temp.getImage();
        }
    }
    public void move() {
        minotaurus.move(minotaurus.getTileId());
        x = newOrigin + minotaurus.getX() * BLOCK_SIZE;
        y = newOrigin + minotaurus.getY() * BLOCK_SIZE;
    }

    public void drawWalk(Graphics2D g2, MyPanel panel) {
        if(init) {
            this.previousTime = System.nanoTime();
            init = false;
        }
        totalTime += System.nanoTime() - previousTime;
        previousTime = System.nanoTime();
        if(totalTime > deltaTime) {
            totalTime = 0;
            g2.drawImage(minotaurWalking[currentFrame], x, y, BLOCK_SIZE, BLOCK_SIZE, panel);
        }
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
            if(currentFrame < MINO_IDLE_LENGTH - 1) currentFrame++;
            else currentFrame = 0;
        }
        g2.drawImage(minotaurIdle[currentFrame], x, y, BLOCK_SIZE, BLOCK_SIZE, panel);
    }
}
