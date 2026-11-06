package GoldeneMitte.visualizer.controller;

import GoldeneMitte.visualizer.model.VoxelModel;
import GoldeneMitte.visualizer.view.MainView;

public class VisualizerController {
    private final VoxelModel model;
    private final MainView view;

    public VisualizerController(VoxelModel model, MainView view) {
        this.model = model;
        this.view = view;
        connectModelToView();
        connectViewToModel();
        new CameraController(view.getSceneView(), this::onSceneClicked);
    }

    private void connectModelToView() {
        model.selectedProperty().addListener(
                (observable, oldBrickId, newBrickId) -> view.showSelection(newBrickId));
        view.showSelection(model.getSelected());
    }

    private void connectViewToModel() {
        view.setOnBrickClicked(model::toggleSelection);
        view.setOnResetCameraRequested(view.getSceneView()::resetCamera);
    }
    private void onSceneClicked(Integer brickId) {
        if (brickId != null) {
            model.toggleSelection(brickId);
        } else {
            model.clearSelection();
        }
    }

}
