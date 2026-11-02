package GoldeneMitte.view;

import GoldeneMitte.Position;
import GoldeneMitte.model.VoxelModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class SceneView extends StackPane {

    private static final double DEFAULT_ROT_X = 25;
    private static final double DEFAULT_ROT_Y = -35;
    private static final double DEFAULT_ZOOM = -14;

    private final BrickStyles styles;
    private final Group world = new Group();
    private final Rotate rotX = new Rotate(DEFAULT_ROT_X, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(DEFAULT_ROT_Y, Rotate.Y_AXIS);
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final SubScene subScene;
    private final Button resetButton = new Button("Ansicht zurücksetzen");

    private final PhongMaterial edgeMat = new PhongMaterial(Color.BLACK);
    private final PhongMaterial dimEdgeMat = new PhongMaterial(Color.rgb(120, 120, 120, 0.4));
    private final List<VoxelNode> nodes = new ArrayList<>();

    private static class VoxelNode {
        final int brickId;
        final Box box;
        final List<Box> edges = new ArrayList<>();

        VoxelNode(int brickId, Box box) {
            this.brickId = brickId;
            this.box = box;
        }
    }

    public SceneView(VoxelModel model, BrickStyles styles) {
        this.styles = styles;

        camera.setFieldOfView(40);
        camera.setNearClip(0.1);
        camera.setFarClip(200);
        camera.setTranslateZ(DEFAULT_ZOOM);

        world.getTransforms().addAll(rotX, rotY);
        buildGrid(model.getGridSize());
        buildVoxels(model);

        Group sceneRoot = new Group(world);
        AmbientLight ambient = new AmbientLight(Color.gray(0.55));
        PointLight key = new PointLight(Color.WHITE);
        key.setTranslateX(-10); key.setTranslateY(-16); key.setTranslateZ(-14);
        PointLight fill = new PointLight(Color.web("#7dd3fc", 1.0).deriveColor(0, 1, 0.5, 1));
        fill.setTranslateX(10); fill.setTranslateY(8); fill.setTranslateZ(-8);
        sceneRoot.getChildren().addAll(ambient, key, fill);

        subScene = new SubScene(sceneRoot, 900, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.web("#090d16"));
        subScene.setCamera(camera);

        StackPane.setAlignment(resetButton, Pos.TOP_RIGHT);
        StackPane.setMargin(resetButton, new Insets(12));

        getChildren().addAll(subScene, resetButton);
        setMinSize(0, 0);
        subScene.widthProperty().bind(widthProperty());
        subScene.heightProperty().bind(heightProperty());
    }

    private void buildGrid(int size) {
        double floorY = (size - 1) / 2.0 + 0.55;
        PhongMaterial gridMat = new PhongMaterial(Color.web("#3b82f6", 0.6));
        for (int i = 0; i <= size; i++) {
            double p = -size / 2.0 + i;
            Box a = new Box(size, 0.02, 0.02);
            a.setMaterial(gridMat); a.setTranslateY(floorY); a.setTranslateZ(p);
            Box b = new Box(0.02, 0.02, size);
            b.setMaterial(gridMat); b.setTranslateY(floorY); b.setTranslateX(p);
            a.setMouseTransparent(true); b.setMouseTransparent(true);
            world.getChildren().addAll(a, b);
        }
    }

    private void buildVoxels(VoxelModel model) {
        final double size = 0.92, t = 0.025, h = size / 2;
        final double off = (model.getGridSize() - 1) / 2.0;

        for (int brickId : model.getBrickIds()) {
            for (Position p : model.getCells(brickId)) {
                Box box = new Box(size, size, size);
                box.setUserData(brickId);
                VoxelNode node = new VoxelNode(brickId, box);
                Group group = new Group(box);

                for (int axis = 0; axis < 3; axis++) {
                    for (int i = 0; i < 4; i++) {
                        int a = (i & 1) == 0 ? -1 : 1;
                        int b = (i & 2) == 0 ? -1 : 1;
                        Box e = new Box(axis == 0 ? size + t : t,
                                axis == 1 ? size + t : t,
                                axis == 2 ? size + t : t);
                        e.setMaterial(edgeMat);
                        e.setMouseTransparent(true);
                        switch (axis) {
                            case 0 -> { e.setTranslateY(a * h); e.setTranslateZ(b * h); }
                            case 1 -> { e.setTranslateX(a * h); e.setTranslateZ(b * h); }
                            default -> { e.setTranslateX(a * h); e.setTranslateY(b * h); }
                        }
                        node.edges.add(e);
                        group.getChildren().add(e);
                    }
                }

                group.setTranslateX(p.x() - off);
                group.setTranslateY(-(p.z() - off));
                group.setTranslateZ(p.y() - off);
                world.getChildren().add(group);
                nodes.add(node);
            }
        }
    }

    public void showSelection(Integer selected) {
        for (VoxelNode n : nodes) {
            boolean isDimmed = selected != null && n.brickId != selected;
            BrickStyle s = styles.getBrickStyle(n.brickId);

            if (isDimmed) {
                n.box.setVisible(false);
                for (Box e : n.edges) {
                    e.setMaterial(dimEdgeMat);
                }
            } else {
                n.box.setVisible(true);
                n.box.setMaterial(s.material());
                for (Box e : n.edges) {
                    e.setMaterial(edgeMat);
                }
            }
        }
    }

    public void rotate(double deltaYaw, double deltaPitch) {
        rotY.setAngle(rotY.getAngle() + deltaYaw);
        rotX.setAngle(Math.max(-89, Math.min(89, rotX.getAngle() + deltaPitch)));
    }

    public void zoom(double delta) {
        camera.setTranslateZ(Math.max(-40, Math.min(-5, camera.getTranslateZ() + delta)));
    }

    public void resetCamera() {
        rotX.setAngle(DEFAULT_ROT_X);
        rotY.setAngle(DEFAULT_ROT_Y);
        camera.setTranslateZ(DEFAULT_ZOOM);
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public void setOnReset(Runnable handler) {
        resetButton.setOnAction(e -> handler.run());
    }

    public Integer brickIdAt(Node n) {
        while (n != null) {
            if (n.getUserData() instanceof Integer id) return id;
            n = n.getParent();
        }
        return null;
    }
}