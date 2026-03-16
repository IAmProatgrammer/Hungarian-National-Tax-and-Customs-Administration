package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;

public class Main extends SimpleApplication {

    private BulletAppState bulletAppState;
    private GameCamera gameCamera;
    private networking net = new networking();

    private boolean cursorHidden = false;

    // Multiplayer settings
    private static boolean isHost;
    private static String serverIP;

    public static void main(String[] args) {

        // Launch Swing menu instead of the game directly
        mainmenu menu = new mainmenu();
        menu.showMenu();
    }

    public static void startGame(boolean host, String ip) {

        isHost = host;
        serverIP = ip;

        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // Disable default fly cam & HUD
        flyCam.setEnabled(false);
        setDisplayStatView(false);
        setDisplayFps(false);

        // Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Start networking
        if (isHost) {
            System.out.println("Hosting LAN server...");
            net.startServer();
        } else {
            System.out.println("Joining server: " + serverIP);
            net.startClient(serverIP);
        }

        // Initialize scene
        intscene();
    }

    public void intscene() {

        // Physics mesh for entire scene
        RigidBodyControl sceneControl = new RigidBodyControl(
                CollisionShapeFactory.createMeshShape(rootNode),
                0f
        );

        rootNode.addControl(sceneControl);
        bulletAppState.getPhysicsSpace().add(sceneControl);

        // Initialize player camera
        gameCamera = new GameCamera(cam, inputManager, bulletAppState, rootNode);
        gameCamera.init(new Vector3f(6, 0, 6));
    }

    @Override
    public void simpleUpdate(float tpf) {

        // Hide cursor once
        if (!cursorHidden && inputManager != null) {
            inputManager.setCursorVisible(false);
            inputManager.setMouseCursor(null);
            cursorHidden = true;
        }

        // Update camera
        if (gameCamera != null) {
            gameCamera.update(tpf);
        }
    }
}