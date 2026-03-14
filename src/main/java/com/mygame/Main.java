package com.mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */

public class Main extends SimpleApplication {

    private BulletAppState bulletAppState;
    
    private GameCamera gameCamera;

    private boolean cursorHidden = false;

    public static void main(String[] args) {
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

        // Initialize scene
        
        

        // Optional: Add physics for entire scene mesh (not required if Scene adds physics per object)
        RigidBodyControl sceneControl = new RigidBodyControl(
                CollisionShapeFactory.createMeshShape(rootNode),
                0f
        );
        rootNode.addControl(sceneControl);
        bulletAppState.getPhysicsSpace().add(sceneControl);

        // Initialize player camera
        gameCamera = new GameCamera(cam, inputManager, bulletAppState, rootNode);
        gameCamera.init(new Vector3f(6, 0, 6));

        // Lighting

        

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
