package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.Random;
import test.PlayerGraphics;

public class MyPanel extends JPanel implements ActionListener, MouseListener {

    // contains the images for animation
    private Image[] minotaurWalking;
    private Image[] thesseusWalking;
    private Image[] walls;
    private Image[] ground;
    private Image start;

    private Timer timer;

    // the path to assets without the last digit the images
    private final String pathToMinotaurWalk = "src/test/Assets/Minotaur_animations/Minotaur_01/PNG Sequences/Walking/Minotaur_01_Walking_";
    private final String pathToThesseusWalk = "src/test/Assets/Warrior_animations/Right_Side/PNG Sequences/Warrior_clothes_1/Walk/0_Warrior_Walk_";
    private final String pathToMinotaurIdle = "src/test/Assets/Minotaur_animations/Minotaur_01/PNG Sequences/Idle/Minotaur_01_Idle_";
    private final String pathToThesseusIdle = "src/test/Assets/Warrior_animations/Right_Side/PNG Sequences/Warrior_clothes_1/Idle/0_Warrior_Idle_";
    private final String pathToSupply = "src/test/Assets/Supplies/shiny/";
    private final String pathToWalls = "src/test/Assets/Tiles/PNG/Walls/Labi/walls_Layer_";
    private final String pathToGround = "src/test/Assets/Tiles/PNG/xwma_";
    private final String pathToStart = "src/test/Assets/startButton.png";

    // total images per arrays for the animation
    private final int MINO_WALK_LENGTH = 18;
    private final int MINO_IDLE_LENGTH = 12;
    private final int THESSEUS_WALK_LENGTH = 30;
    private final int THESSEUS_IDLE_LENGTH = 30;
    private final int SUPPLY_LENGTH = 2;
    private final int WALL_LENGTH = 8;
    private final int GROUND_LENGTH = 5;

    // calculates the game fps
    private final int FPS = 30;
    private final int DELAY = (int) (1.0 / FPS * 1000); // convert fps in milliseconds

    //board characteristics
    public final int BLOCK_SIZE = 50;
    public final int N = 15; //number of tiles

    /*  added to everything inside the labyrinth so is
     *  like the new origin is the left upper corner of
     *  labyrinth
     */
    public final int newOrigin = 2 * BLOCK_SIZE;

    private final int S = 50; //number of the initial supplies on board
    private final int W = (N * N * 3 + 1) / 2; //max number of walls on board
    private SupplyGraphics[] supplies;

    //the 2 Players
    private PlayerGraphics thesseus;
    private PlayerGraphics minotaurus;

    int[] x; //random numbers for random ground tiles
    int[] grassRand; //random numbers for random grass tiles
    public boolean thesseusisWalking;
    private int thesseusTriggerMove = 0;
    public boolean minoisWalking;
    private int minoTriggerMove = 0;
    private boolean started;

    private Board board;
    private Tile[] tempTiles;

    private Font font;

    public MyPanel() {
        this.setBackground(Color.BLACK);

        font = new Font("Serif", Font.BOLD | Font.ITALIC, 20);

        minotaurWalking = new Image[MINO_WALK_LENGTH];
        thesseusWalking = new Image[THESSEUS_WALK_LENGTH];
        walls = new Image[WALL_LENGTH];
        ground = new Image[GROUND_LENGTH];

        timer = new Timer(DELAY, this);

        board = new Board(N, S, W);
        board.createBoard();

        started = false;

        supplies = new SupplyGraphics[S];
        for(int i = 0; i < S; i++) {
            supplies[i] = new SupplyGraphics(pathToSupply, SUPPLY_LENGTH, board.supplies[i],this);
        }

        tempTiles = board.getTiles();
        thesseusisWalking = false;
        minoisWalking = false;

        Player tempMino = new Player(2, "Minotauros", board, 0, N / 2, N / 2);
        HeuristicPlayer tempThesseus = new HeuristicPlayer(1, "Thesseus", board, 0, 0, 0);
        minotaurus = new PlayerGraphics(this, board, tempMino, null, pathToMinotaurWalk, pathToMinotaurIdle, MINO_WALK_LENGTH, MINO_IDLE_LENGTH, 0, 0);
        thesseus = new PlayerGraphics(this, board, null, tempThesseus, pathToThesseusWalk, pathToThesseusIdle, THESSEUS_WALK_LENGTH, THESSEUS_IDLE_LENGTH, 20, 20);

        Random random = new Random();
        x = new int[N * N];

        int grassNum = (N + 4) * (N + 4);
        grassRand = new int[grassNum];
        for(int i = 0; i < N * N; i++){
            x[i] = random.nextInt(2) + 3; //fill the array with random series of 3s and 4s
        }
        for(int i = 0; i < grassNum; i++) {
            grassRand[i] = random.nextInt(2) + 1; //fill the array with random series of 1s and 2s
        }

        printTerminalBoard();
        loadImage();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //clear the screen with a black square
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 1000, 1000);
        g2.setFont(font);

        //Prints the walls around the board
        drawGround(g2);
        drawBoard(g2);

        //text on screen with total supplies collected
        g2.drawString("Supplies: " + SupplyGraphics.collected + " / " + S, 0, 100);

        //draws the supplies on the board
        for(int i = 0; i < S; i++) {
            if(board.supplies[i].getSupplyTileId() == -1) {
                supplies[i].isPicked = true;
            }
            supplies[i].drawSupply(g2, this);
        }

        //Thesseus wallking animation
        if(thesseusisWalking) {
            thesseus.drawWalk(g2, this);
        }

        //Thesseus standing animation
        else {
            thesseus.drawIdle(g2, this);
        }

        //Minos walking animation
        if(minoisWalking) {
            minotaurus.drawWalk(g2,this);
        }

        //Minos standing animation
        else {
            minotaurus.drawIdle(g2, this);
        }

        // START screen
        if(!started) {
            g2.drawImage(start, 0, 0, BLOCK_SIZE * N + 4 * BLOCK_SIZE,BLOCK_SIZE * N + 4 * BLOCK_SIZE, this);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        addMouseListener(this);

        if(started) {
            thesseusTriggerMove++;
            minoTriggerMove++;
        }
        if(thesseusTriggerMove > (int) (2000.0 / DELAY) && !thesseusisWalking){
            thesseus.move(minotaurus.player.getTileId());
            thesseusisWalking = true;
            thesseusTriggerMove = 0;
        }
        if(minoTriggerMove > (int) (2000.0 / DELAY) && !minoisWalking){
            minotaurus.move(minotaurus.player.getTileId());
            minoisWalking = true;
            minoTriggerMove = 0;
        }
        this.repaint();
    }

    // loads the animation images on the correct array
    private void loadImage() {
        for (int i = 0; i < GROUND_LENGTH; i++) {
            String path = pathToGround + (i < 10 ? "00" : "0") + i;
            ImageIcon temp = new ImageIcon(path + ".png" );
            System.out.println(path);
            ground[i] = temp.getImage();
        }

        ImageIcon temp = new ImageIcon(pathToWalls + "000.png");
        walls[0] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "002.png");
        walls[1] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "003.png");
        walls[2] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "004.png");
        walls[3] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "005.png");
        walls[4] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "001.png");
        walls[5] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "006.png");
        walls[6] = temp.getImage();

        temp = new ImageIcon(pathToWalls + "007.png");
        walls[7] = temp.getImage();

        temp = new ImageIcon(pathToStart);
        start = temp.getImage();


    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {
        started = true;
    }


    // draws the ground tiles on board
    private void drawGround(Graphics2D g2) {
        //draw inside the labyrinth
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                g2.drawImage(ground[x[i + N * j]], newOrigin + i * BLOCK_SIZE, newOrigin + j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, this);
            }
        }
        //draw around the labyrinth
        for(int i = 0; i < N + 4; i++) {
            for(int j = 0; j < N + 4; j++) {
                if(j < 2 || j > N + 1) {
                    g2.drawImage(ground[grassRand[i + N * j]], i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, this);
                }
                else if(i < 2 || i > N + 1) {
                    g2.drawImage(ground[grassRand[i + N * j]], i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, this);
                }
            }
        }
    }

    //draw the walls on the board
    private void drawBoard(Graphics2D g2) {
        for (int i = 0; i < N; i++) { //y
            for (int j = 0; j < N; j++) { //x
                if (i == 0 && j == 0) { //left down corner
                    g2.drawImage(walls[7], newOrigin + 0, newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / (2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5) , this);
                    g2.drawImage(walls[6], newOrigin + 0, newOrigin + (N - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.12)), (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 5, this);
                    g2.drawImage(walls[1], newOrigin + 0, newOrigin + (N - 1) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, this);

                } else if (i == 0 && j == N - 1) { //RIGHT DOWN CORNER
                    g2.drawImage(walls[7], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + N * BLOCK_SIZE -  (int) (BLOCK_SIZE / (2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                    g2.drawImage(walls[6], newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / (3.12)), newOrigin + (N - 1) * BLOCK_SIZE, (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 5, this);
                    g2.drawImage(walls[3], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + (N - 1) * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, this);
                } else if (i == N - 1 && j == 0) { //LEFT UP CORNER
                    g2.drawImage(walls[7], newOrigin + 0, newOrigin + 0, BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                    g2.drawImage(walls[6], newOrigin + 0, newOrigin + 0, (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 5, this);
                    g2.drawImage(walls[0], newOrigin + 0, newOrigin + 0, BLOCK_SIZE, BLOCK_SIZE, this);
                } else if (i == N - 1 && j == N - 1) { //RIGHT_UP CORNER
                    g2.drawImage(walls[7], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + 0, BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                    g2.drawImage(walls[6], newOrigin + N * BLOCK_SIZE  - (int) (BLOCK_SIZE / (3.12)) , newOrigin + 0, (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 5, this);
                    g2.drawImage(walls[2], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + 0, BLOCK_SIZE, BLOCK_SIZE, this);
                } else if (i == 0) { //first row
                    if (tempTiles[j + i * N].isLeft()) {
                        g2.drawImage(walls[6], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.12)), newOrigin + (N - 1) * BLOCK_SIZE - 10, (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 10, this);
                        g2.drawImage(walls[5], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), newOrigin + (N - 1) * BLOCK_SIZE - 10, (int) (BLOCK_SIZE / 4), BLOCK_SIZE + 10, this);
                    }
                    g2.drawImage(walls[7], newOrigin + j * BLOCK_SIZE, newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / (2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                    g2.drawImage(walls[4], newOrigin + j * BLOCK_SIZE, newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / (3.22)),  BLOCK_SIZE, (int) (BLOCK_SIZE / 3.22), this);
                } else if (i == N - 1) { //last row
                    if(tempTiles[j + i * N].isLeft()) {
                        g2.drawImage(walls[6], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.12)), newOrigin + 0, (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 10, this);
                        g2.drawImage(walls[5], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), newOrigin + 0, (int) (BLOCK_SIZE / 4), BLOCK_SIZE + 10, this);
                    }
                    if (tempTiles[j + i * N].isDown()) {
                        g2.drawImage(walls[7], newOrigin + j * BLOCK_SIZE, newOrigin +  BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                        g2.drawImage(walls[4], newOrigin + j * BLOCK_SIZE, newOrigin +  BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), BLOCK_SIZE, (int) (BLOCK_SIZE / 4), this);
                    }
                    g2.drawImage(walls[7], newOrigin + j * BLOCK_SIZE, newOrigin + 0, BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                    g2.drawImage(walls[4], newOrigin + j * BLOCK_SIZE, newOrigin + 0,  BLOCK_SIZE, (int) (BLOCK_SIZE / 3.22), this);
                } else if (j == 0) { //first column
                    if (tempTiles[j + i * N].isUp()) {
                        g2.drawImage(walls[7], newOrigin + 0, newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                        g2.drawImage(walls[4], newOrigin + 0, newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)),  BLOCK_SIZE, (int) (BLOCK_SIZE / 4), this);
                    }
                    g2.drawImage(walls[6], newOrigin + 0, newOrigin + (N - i - 1) * BLOCK_SIZE, (int) (BLOCK_SIZE / 1.5),BLOCK_SIZE + 10, this);
                    g2.drawImage(walls[5], newOrigin + 0, newOrigin + (N - i - 1) * BLOCK_SIZE,  (int) (BLOCK_SIZE / 3.22), BLOCK_SIZE + 5, this);
                } else if (j == N - 1) { //last column
                    if (tempTiles[j + i * N].isUp()) {
                        g2.drawImage(walls[7], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                        g2.drawImage(walls[4], newOrigin + (N - 1) * BLOCK_SIZE, newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), BLOCK_SIZE, (int) (BLOCK_SIZE / 4), this);
                    }
                    g2.drawImage(walls[6], newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / 3.12), newOrigin + i * BLOCK_SIZE, (int) (BLOCK_SIZE / 1.5),BLOCK_SIZE + 10, this);
                    g2.drawImage(walls[5], newOrigin + N * BLOCK_SIZE - (int) (BLOCK_SIZE / 3.22), newOrigin + i * BLOCK_SIZE,  (int) (BLOCK_SIZE / 3.22), BLOCK_SIZE, this);
                } else {
                    if (tempTiles[j + i * N].isDown() || tempTiles[j + i * N - N].isUp()) {
                        g2.drawImage(walls[7], newOrigin + j * BLOCK_SIZE, newOrigin + (N - i) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 2.54)), BLOCK_SIZE + 10, (int) (BLOCK_SIZE / 1.5), this);
                        g2.drawImage(walls[4], newOrigin + j * BLOCK_SIZE, newOrigin + (N - i) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), BLOCK_SIZE, (int) (BLOCK_SIZE / 4 /*3.22*/), this);
                    }
                    if (tempTiles[j + i * N].isLeft() || tempTiles[j + i * N - 1].isRight()) {
                        g2.drawImage(walls[6], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.12)), newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.12)), (int) (BLOCK_SIZE / 1.75), BLOCK_SIZE + 5, this);
                        g2.drawImage(walls[5], newOrigin + j * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), newOrigin + (N - i - 1) * BLOCK_SIZE - (int) (BLOCK_SIZE / (2 * 3.22)), (int) (BLOCK_SIZE / 4 /*3.22*/), BLOCK_SIZE, this);
                    }
                }
            }
        }
    }

    // prints the teriminal representation of the board
    public void printTerminalBoard() {
        String[][] boardLayout = board.getStringRepresentation( thesseus.player.getTileId(), minotaurus.player.getTileId());
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
    }
}
