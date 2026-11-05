package GoldeneMitte.visualizer.view;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import javafx.scene.Group;
import java.util.List;

public class VoxelNode {
    public final int brickId;
    public final Box cube;
    public final List<Box> outlineBars;
    public final Group group;

    VoxelNode(int brickId, Box cube, List<Box> outlineBars) {
        this.brickId = brickId;
        this.cube = cube;
        this.outlineBars = outlineBars;
        this.group = new Group(cube);
        this.group.getChildren().addAll(outlineBars);
    }

    void setOutlineMaterial(PhongMaterial material) {
        for (Box bar : outlineBars) {
            bar.setMaterial(material);
        }
    }
}