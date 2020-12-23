package test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class MyFrame extends JFrame{
    public MyPanel panel;

    MyFrame() {
        panel = new MyPanel();
        panel.setBackground(Color.BLACK);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(panel.BLOCK_SIZE * panel.N + 4 * panel.BLOCK_SIZE,panel.BLOCK_SIZE * panel.N + 4 * panel.BLOCK_SIZE);
        this.setLocationRelativeTo(null);
        this.add(panel);
        this.setVisible(true);
    }
}
