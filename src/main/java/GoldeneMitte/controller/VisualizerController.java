package GoldeneMitte.controller;

import GoldeneMitte.model.VoxelModel;
import GoldeneMitte.view.MainView;
import GoldeneMitte.view.SceneView;
import javafx.scene.SubScene;
import javafx.scene.input.MouseButton;

public class VisualizerController {

    private static final double ROTATE_SPEED = 0.4;
    private static final double ZOOM_SPEED = 0.02;
    private static final double DRAG_THRESHOLD = 3;

    private final VoxelModel model;
    private final MainView view;

    private double lastX, lastY, pressX, pressY;
    private boolean dragged;

    public VisualizerController(VoxelModel model, MainView view) {
        this.model = model;
        this.view = view;
        bind();
        installMouse();
    }

    private void bind() {
        model.selectedProperty().addListener((obs, old, sel) -> view.showSelection(sel));
        view.showSelection(model.getSelected());

        view.getSidebar().setOnBrickClicked(model::toggleSelection);
        view.getSceneView().setOnReset(view.getSceneView()::resetCamera);
    }

    private void installMouse() {
        SceneView sv = view.getSceneView();
        SubScene s = sv.getSubScene();

        s.setOnMousePressed(e -> {
            lastX = pressX = e.getSceneX();
            lastY = pressY = e.getSceneY();
            dragged = false;
        });
        s.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - lastX;
            double dy = e.getSceneY() - lastY;
            lastX = e.getSceneX();
            lastY = e.getSceneY();
            if (Math.hypot(e.getSceneX() - pressX, e.getSceneY() - pressY) > DRAG_THRESHOLD) dragged = true;
            sv.rotate(-dx * ROTATE_SPEED, dy * ROTATE_SPEED);
        });
        s.setOnMouseReleased(e -> {
            if (dragged || e.getButton() != MouseButton.PRIMARY) return;
            Integer id = sv.brickIdAt(e.getPickResult().getIntersectedNode());
            if (id != null) model.toggleSelection(id);
            else model.clearSelection();
        });
        s.setOnScroll(e -> sv.zoom(e.getDeltaY() * ZOOM_SPEED));
    }
}
