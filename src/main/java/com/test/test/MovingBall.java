package com.test.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovingBall extends JFrame {
    private int x = 20;
    private int y = 20;
    private int xSpeed = 5;
    private int ySpeed = 5;

    public MovingBall() {
        setTitle("Moving Ball");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBall();
                repaint();
            }
        });
        timer.start();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void moveBall() {
        x += xSpeed;
        y += ySpeed;

        if (x > getWidth() - 30 || x < 0) {
            xSpeed = -xSpeed;
        }

        if (y > getHeight() - 30 || y < 0) {
            ySpeed = -ySpeed;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLUE);
        g.fillOval(x, y, 30, 30);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MovingBall();
            }
        });
    }
}
