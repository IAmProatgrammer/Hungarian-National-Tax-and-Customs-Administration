/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mygame;

/**
 *
 * @author MyPas
 */

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.light.SpotLight;
import com.jme3.scene.Node;

public class GameCamera {

    private Camera cam;
    private final InputManager inputManager;
    private final BulletAppState bulletAppState;
    private final Node rootNode;

    private CharacterControl player;

    private boolean forward, backward, left, right;
    private float yaw, pitch;

    private final float moveSpeed = 2f;
    private final float mouseSensitivity = 2f;

    // Flashlight
    private SpotLight flashlight;
    private boolean flashlightOn = true;

    public GameCamera(Camera cam, InputManager inputManager, BulletAppState bulletAppState, Node rootNode) {
        this.cam = cam;
        this.inputManager = inputManager;
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
    }

    public void init(Vector3f startPos) {
        setupPhysics(startPos);
        setupInput();
        setupFlashlight();
    }

    private void setupPhysics(Vector3f startPos) {
        player = new CharacterControl(
                new com.jme3.bullet.collision.shapes.CapsuleCollisionShape(0.5f, 1.8f),
                0.05f
        );
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(startPos);
        bulletAppState.getPhysicsSpace().add(player);
    }

    private void setupInput() {
        // WASD
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));

        // Flashlight toggle
        inputManager.addMapping("ToggleFlashlight", new KeyTrigger(KeyInput.KEY_F));

        // Mouse movement
        inputManager.addMapping("MouseX+", new MouseAxisTrigger(com.jme3.input.MouseInput.AXIS_X, false));
        inputManager.addMapping("MouseX-", new MouseAxisTrigger(com.jme3.input.MouseInput.AXIS_X, true));
        inputManager.addMapping("MouseY+", new MouseAxisTrigger(com.jme3.input.MouseInput.AXIS_Y, false));
        inputManager.addMapping("MouseY-", new MouseAxisTrigger(com.jme3.input.MouseInput.AXIS_Y, true));

        inputManager.addListener(actionListener, "Forward", "Backward", "Left", "Right", "ToggleFlashlight");
        inputManager.addListener(analogListener, "MouseX+", "MouseX-", "MouseY+", "MouseY-");
    }

    private void setupFlashlight() {
        flashlight = new SpotLight();
        flashlight.setColor(com.jme3.math.ColorRGBA.White.mult(100f));
        flashlight.setSpotRange(15f);
        flashlight.setSpotInnerAngle(FastMath.DEG_TO_RAD * 10f);
        flashlight.setSpotOuterAngle(FastMath.DEG_TO_RAD * 20f);
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection());

        rootNode.addLight(flashlight);
    }

    public void update(float tpf) {
        // --- Movement ---
        Vector3f dir = new Vector3f();
        Vector3f camDir = cam.getDirection().clone();
        camDir.y = 0;
        camDir.normalizeLocal();
        Vector3f camLeft = cam.getLeft().clone();
        camLeft.y = 0;
        camLeft.normalizeLocal();

        if (forward) dir.addLocal(camDir);
        if (backward) dir.addLocal(camDir.negate());
        if (left) dir.addLocal(camLeft);
        if (right) dir.addLocal(camLeft.negate());

        if (dir.lengthSquared() > 0)
            dir.normalizeLocal().multLocal(moveSpeed);

        player.setWalkDirection(dir);

        Vector3f pos = player.getPhysicsLocation();
        cam.setLocation(pos.add(0, 1.6f, 0));

        // --- Flashlight follows camera ---
        if (flashlightOn && flashlight != null) {
            flashlight.setPosition(cam.getLocation());
            flashlight.setDirection(cam.getDirection());

            // Flicker effect

        }
    }

    // ----------------------
    // Listeners
    // ----------------------
    private final ActionListener actionListener = (name, pressed, tpf) -> {
        switch (name) {
            case "Forward": forward = pressed; break;
            case "Backward": backward = pressed; break;
            case "Left": left = pressed; break;
            case "Right": right = pressed; break;
            case "ToggleFlashlight":
                if (pressed) flashlightOn = !flashlightOn;
                if (flashlight != null) flashlight.setEnabled(flashlightOn);
                break;
        }
    };

    private final AnalogListener analogListener = (name, value, tpf) -> {
        if (name.equals("MouseX+")) yaw -= value * mouseSensitivity;
        if (name.equals("MouseX-")) yaw += value * mouseSensitivity;
        if (name.equals("MouseY+")) pitch -= value * mouseSensitivity;
        if (name.equals("MouseY-")) pitch += value * mouseSensitivity;

        // Clamp pitch
        pitch = FastMath.clamp(pitch, -FastMath.HALF_PI + 0.01f, FastMath.HALF_PI - 0.01f);

        // --- Use quaternion rotation (old working code) ---
        Quaternion rot = new Quaternion();
        rot.fromAngles(pitch, yaw, 0);
        cam.setRotation(rot);
    };
}
