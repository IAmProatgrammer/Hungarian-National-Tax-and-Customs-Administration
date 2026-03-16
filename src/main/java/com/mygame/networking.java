package com.mygame;

import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.Client;

import com.simsilica.ethereal.EtherealHost;
import com.simsilica.ethereal.EtherealClient;

import java.io.IOException;

public class networking {

    public static final int PORT = 5110;

    private Server server;
    private Client client;

    private EtherealHost etherealHost;
    private EtherealClient etherealClient;

    // Start LAN server
    public void startServer() {
        try {
            etherealHost = new EtherealHost(); 
            server = Network.createServer(PORT);
            server.start();

            System.out.println("Server started on port " + PORT);

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Connect to LAN server
    public void startClient(String ip) {
        try {
            etherealClient = new EtherealClient();
            client = Network.connectToServer(ip, PORT);
            client.start();

            System.out.println("Connected to server: " + ip);

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public EtherealHost getEtherealHost() {
        return etherealHost;
    }

    public EtherealClient getEtherealClient() {
        return etherealClient;
    }
}