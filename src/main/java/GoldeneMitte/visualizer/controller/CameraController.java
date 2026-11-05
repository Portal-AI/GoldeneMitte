package GoldeneMitte.visualizer.controller;

import GoldeneMitte.visualizer.view.SceneView;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;

import java.util.function.Consumer;

public class CameraController {
    private static final double ROTATE_SPEED = 0.4;
    private static final double ZOOM_SPEED = 0.02;
    private static final double DRAG_THRESHOLD = 3;

    private final SceneView sceneView;
    private final Consumer<Integer> onSceneClicked;

    private double lastMouseX;
    private double lastMouseY;
    private double pressStartX;
    private double pressStartY;
    private boolean isDragging;

    CameraController(SceneView sceneView, Consumer<Integer> onSceneClicked) {
        this.sceneView = sceneView;
        this.onSceneClicked = onSceneClicked;
        setMouseHandlers();
    }

    private void setMouseHandlers() {
        SubScene subScene = sceneView.getSubScene();

        subScene.setOnMousePressed(event -> {
            lastMouseX = pressStartX = event.getSceneX();
            lastMouseY = pressStartY = event.getSceneY();
            isDragging = false;
        });

        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - lastMouseX;
            double deltaY = event.getSceneY() - lastMouseY;
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();

            double distanceFromPress = Math.hypot(event.getSceneX() - pressStartX, event.getSceneY() - pressStartY);
            if (distanceFromPress > DRAG_THRESHOLD) {
                isDragging = true;
            }

            sceneView.rotate(-deltaX * ROTATE_SPEED, deltaY * ROTATE_SPEED);
        });

        subScene.setOnMouseReleased(e -> {
            boolean isPlainLeftClick = !isDragging && e.getButton() == MouseButton.PRIMARY;
            if (isPlainLeftClick) {
                Integer brickId = sceneView.findBrickId(e.getPickResult().getIntersectedNode());
                onSceneClicked.accept(brickId);
            }
        });

        subScene.setOnScroll(e -> sceneView.zoom(e.getDeltaY() * ZOOM_SPEED));
    }

}
