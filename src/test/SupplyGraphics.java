package test;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class SupplyGraphics {
    private final String pathToSupply;
    private final int SUPPLY_LENGTH;
    private Image supply;

    private Supply boardSupply;
    private int x, y, dy, dx;
    private double a; //the constant factor of the function (of trajectory of supply pick animation)
    private int stepX, stepY;
    public boolean isPicked; //checks if the supply is picked by Thesseus, is checked in MyPanel
    private boolean picked; //true if the pick animation is over
    public static int collected = 0; // number of total collected supplies
    public int tempCollected; //collected supplies at the moment player collects a supply
    private boolean firstInPickMode = false;

    private int scaleX, scaleY;

    private long totalTime;
    private final long deltaTime;
    private long previousTime;
    private boolean init;

    private static int rowCounter; // at this row the supplies will be showed after Thesseus pick them

    private int currentFrame;
    private int totalFrames;

    private int BLOCK_SIZE;
    private int N;
    private int newOrigin;

    public SupplyGraphics(String pathToSupply, int SUPPLY_LENGTH, Supply boardSupply, MyPanel panel) {
        this.pathToSupply = pathToSupply;
        this.SUPPLY_LENGTH = SUPPLY_LENGTH;

        isPicked = false;
        picked = false;

        this.boardSupply = boardSupply;
        x = boardSupply.getY();
        y = boardSupply.getX();

        Random rand = new Random();
        dy = rand.nextInt(3) - 1;
        if(dy == 0) dy = 1;
        dx = 0;
        stepX = 0;
        stepY = 0;

        scaleX = 0;
        scaleY = 0;

        totalTime = 0;
        deltaTime = (int) (0.1 / 10 * 10e9);
        init = true;
        currentFrame = 0;
        totalFrames = 4;

        BLOCK_SIZE = panel.BLOCK_SIZE;
        N = panel.N;
        newOrigin = panel.newOrigin;

        rowCounter = 0;

        loadImage();
    }

    private void loadImage() {
        Random rand = new Random();
        ImageIcon temp = new ImageIcon(pathToSupply + rand.nextInt(SUPPLY_LENGTH) + ".png");
        supply = temp.getImage();
    }

    public void drawSupply(Graphics2D g2, MyPanel panel) {
        if(init) {
            this.previousTime = System.nanoTime();
            init = false;
        }
        long now = System.nanoTime();
        totalTime += now - previousTime;
        previousTime = now;
        if(!isPicked) {
            if(totalTime > deltaTime) {
                totalTime -= deltaTime;
                if(currentFrame < totalFrames - 1) currentFrame++;
                else {
                    currentFrame = 0;
                    dy = -dy;
                }
                stepY += dy;
            }
            g2.drawImage(supply, newOrigin + x * BLOCK_SIZE + stepX, newOrigin + (N - 1 - y) * BLOCK_SIZE + stepY, BLOCK_SIZE + scaleX, BLOCK_SIZE + scaleY, panel);
        }
        else {
            if(!firstInPickMode) {
                x = newOrigin + x * BLOCK_SIZE + stepX;
                y = newOrigin + (N - 1 - y) * BLOCK_SIZE + stepY;
            }
            pickSupply(g2, panel);
        }
    }
    public void pickSupply(Graphics2D g2, MyPanel panel) {
        int compress = 20; //how close the supplies will be after Thesseus pick them
        if(!firstInPickMode) {
            firstInPickMode = true;
            tempCollected = collected;
            collected++;
            a = y / Math.sqrt(Math.abs(x - tempCollected * (panel.BLOCK_SIZE - compress))); //calculate the factor of the sqrt function
            this.previousTime = System.nanoTime();
        }

        long now = System.nanoTime();
        totalTime += now - previousTime;
        previousTime = now;

        if(totalTime > deltaTime && !picked) {
            if(y > 0) {
                if(x >= tempCollected * (panel.BLOCK_SIZE - compress))
                    x--;
                else
                    x++;
                y = (int) (a * Math.sqrt(x - tempCollected * (panel.BLOCK_SIZE - compress)));
                if(scaleX < 30) {
                    scaleX++;
                    scaleY++;
                }
                else if(scaleX > 0) {
                    scaleX--;
                    scaleY--;
                }
            }
            else {
                scaleX = 0;
                scaleY = 0;
                x = tempCollected * (panel.BLOCK_SIZE - compress);
                picked = true;
            }
        }
        g2.drawImage(supply, x, y, BLOCK_SIZE + scaleX, BLOCK_SIZE + scaleY, panel);
    }
}
