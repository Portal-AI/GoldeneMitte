package GoldeneMitte.visualizer.view;

import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.function.IntConsumer;

public class MainView extends BorderPane {

    private final SidebarView sidebar;
    private final SceneView sceneView;

    public MainView(int gridSize, List<BrickDescriptor> bricks) {
        sidebar = new SidebarView(bricks);
        sceneView = new SceneView(gridSize, bricks);

        setStyle(Theme.MAIN_STYLE);
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
