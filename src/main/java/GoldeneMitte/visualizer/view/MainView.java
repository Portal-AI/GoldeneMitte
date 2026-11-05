package GoldeneMitte.visualizer.view;

import GoldeneMitte.visualizer.model.VoxelModel;
import javafx.scene.layout.BorderPane;

import java.util.function.IntConsumer;

public class MainView extends BorderPane {

    private final SidebarView sidebar;
    private final SceneView sceneView;

    public MainView(VoxelModel model) {
        BrickStyles styles = new BrickStyles();
        sidebar = new SidebarView(model, styles);
        sceneView = new SceneView(model, styles);

        setStyle("-fx-base: #1e293b; -fx-background: #0f172a;");
        setLeft(sidebar);
        setCenter(sceneView);
    }

    public SceneView getSceneView() {
        return sceneView;
    }

    public void showSelection(Integer selected) {
        sidebar.showSelection(selected);
        sceneView.showSelection(selected);
    }

    public void setOnBrickClicked(IntConsumer handler) {
        sidebar.setOnBrickClicked(handler);
    }

    public void setOnResetCameraRequested(Runnable handler) {
        sceneView.setReset(handler);
    }
}
