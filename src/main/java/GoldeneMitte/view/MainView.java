package GoldeneMitte.view;

import GoldeneMitte.model.VoxelModel;
import javafx.scene.layout.BorderPane;

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

    public SidebarView getSidebar() {
        return sidebar;
    }

    public SceneView getSceneView() {
        return sceneView;
    }

    public void showSelection(Integer selected) {
        sidebar.showSelection(selected);
        sceneView.showSelection(selected);
    }
}
