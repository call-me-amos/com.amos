package com.test.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovingBall2 extends JPanel implements ActionListener {
    private int x = 50;
    private int y = 50;
    private int dx = 3;
    private int dy = 3;

    public MovingBall2() {
        Timer timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(x, y, 50, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        x += dx;
        y += dy;
        if (x <= 0 || x >= getWidth() - 50) {
            dx = -dx;
        }
        if (y <= 0 || y >= getHeight() - 50) {
            dy = -dy;
        }
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Moving Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.add(new MovingBall());
        frame.setVisible(true);
    }
}