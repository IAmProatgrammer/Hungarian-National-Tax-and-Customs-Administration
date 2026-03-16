package com.mygame;

import javax.swing.*;
import java.awt.*;

public class mainmenu {

    public void showMenu() {

    JFrame frame = new JFrame("My Multiplayer Game");
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(3, 1));

    JButton hostButton = new JButton("Host LAN Game");
    JButton joinButton = new JButton("Join LAN Game");
    JButton quitButton = new JButton("Quit");

    hostButton.addActionListener(e -> {
        frame.dispose();
        new Thread(() -> Main.startGame(true, null)).start(); // <-- run jME in a new thread
    });

    joinButton.addActionListener(e -> {
        String ip = JOptionPane.showInputDialog("Enter Server IP:");
        frame.dispose();
        new Thread(() -> Main.startGame(false, ip)).start(); // <-- run jME in a new thread
    });

    quitButton.addActionListener(e -> System.exit(0));

    frame.add(hostButton);
    frame.add(joinButton);
    frame.add(quitButton);

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}
}