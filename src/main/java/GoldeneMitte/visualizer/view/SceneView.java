package GoldeneMitte.visualizer.view;

import GoldeneMitte.core.Position;
import GoldeneMitte.visualizer.model.VoxelModel;
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

    private static final double DEFAULT_ROTATION_X = 25;
    private static final double DEFAULT_ROTATION_Y = -35;
    private static final double DEFAULT_ZOOM = -14;
    private static final double CUBE_SIZE = 0.9;

    private final BrickStyles styles;
    private final Group world = new Group();
    private final Rotate rotX = new Rotate(DEFAULT_ROTATION_X, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(DEFAULT_ROTATION_Y, Rotate.Y_AXIS);
    private final PerspectiveCamera camera = new PerspectiveCamera(true);

    private final SubScene subScene;
    private final Button resetButton = new Button("Ansicht zurücksetzen");

    private final PhongMaterial outlineMaterial = new PhongMaterial(Theme.OUTLINE_EDGE);
    private final PhongMaterial dimmedEdgeMaterial = new PhongMaterial(Theme.DIMMED_EDGE);
    private final List<VoxelNode> voxelNodes = new ArrayList<>();

    public SceneView(VoxelModel model, BrickStyles styles) {
        this.styles = styles;
        configureCamera();

        world.getTransforms().addAll(rotX, rotY);
        buildFloorGrid(model.getGridSize());
        buildVoxels(model);

        subScene = createSubScene();
        createResetViewButton();

        setMinSize(0, 0);
        subScene.widthProperty().bind(widthProperty());
        subScene.heightProperty().bind(heightProperty());
    }

    private void configureCamera(){
        camera.setFieldOfView(40);
        camera.setNearClip(0.1);
        camera.setFarClip(200);
        camera.setTranslateZ(DEFAULT_ZOOM);
    }

    private SubScene createSubScene(){
        Group sceneRoot = new Group(world);
        sceneRoot.getChildren().addAll(createLights());

        SubScene subScene= new SubScene(sceneRoot, 900, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Theme.SUB_SCENE_COLOR);
        subScene.setCamera(camera);
        return subScene;
    }

    private List<Node> createLights(){
        AmbientLight ambient = new AmbientLight(Theme.AMBIENT_COLOR);
        PointLight keyLight  = new PointLight(Theme.KEY_LIGHT_COLOR);
        keyLight.setTranslateX(-10);
        keyLight.setTranslateY(-16);
        keyLight.setTranslateZ(-14);
        PointLight fill = new PointLight(Theme.POINT_LIGHT_COLOR_FILL);
        fill.setTranslateX(10);
        fill.setTranslateY(8);
        fill.setTranslateZ(-8);
        return List.of(ambient, keyLight, fill);
    }

    private void createResetViewButton(){
        StackPane.setAlignment(resetButton, Pos.TOP_RIGHT);
        StackPane.setMargin(resetButton, new Insets(12));
        getChildren().addAll(subScene, resetButton);
    }

    private void buildFloorGrid(int size) {
        double floorY = (size - 1) / 2.0 + 0.55;
        PhongMaterial gridMat = new PhongMaterial(Theme.FLOOR_COLOR);
        for (int row = 0; row <= size; row++) {
            double p = -size / 2.0 + row;

            Box lineAlongX = new Box(size, 0.02, 0.02);
            lineAlongX.setMaterial(gridMat);
            lineAlongX.setTranslateY(floorY);
            lineAlongX.setTranslateZ(p);

            Box lineAlongZ = new Box(0.02, 0.02, size);
            lineAlongZ.setMaterial(gridMat);
            lineAlongZ.setTranslateY(floorY);
            lineAlongZ.setTranslateX(p);
            lineAlongX.setMouseTransparent(true);
            lineAlongZ.setMouseTransparent(true);
            world.getChildren().addAll(lineAlongX, lineAlongZ);
        }
    }

    private void buildVoxels(VoxelModel model) {
        double centerOffset = (model.getGridSize() - 1) / 2.0;

        for (int brickId : model.getBrickIds()) {
            for (Position cell : model.getCells(brickId)) {
                VoxelNode voxel = createVoxel(brickId);
                placeInScene(voxel.group, cell, centerOffset);
                world.getChildren().add(voxel.group);
                voxelNodes.add(voxel);
            }
        }
    }

    private static void placeInScene(Group group, Position cell, double centerOffset) {
        group.setTranslateX(cell.x() - centerOffset);
        group.setTranslateY(-(cell.z() - centerOffset));
        group.setTranslateZ(cell.y() - centerOffset);
    }

    private VoxelNode createVoxel(int brickId) {
        Box cube = new Box(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE);
        cube.setUserData(brickId);
        return new VoxelNode(brickId, cube, createCubeOutline());
    }

    private List<Box> createCubeOutline() {
        double half = CUBE_SIZE / 2;
        double length = CUBE_SIZE + 0.02;
        double t = 0.02;

        List<Box> bars = new ArrayList<>();
        for (int corner = 0; corner < 4; corner++) {
            double sign1 = (corner & 1) == 0 ? -half : half;
            double sign2 = (corner & 2) == 0 ? -half : half;
            bars.add(createOutlineBar(length, t, t, 0, sign1, sign2));
            bars.add(createOutlineBar(t, length, t, sign1, 0, sign2));
            bars.add(createOutlineBar(t, t, length, sign1, sign2, 0));
        }
        return bars;
    }

    private Box createOutlineBar(double width, double height, double depth, double x, double y, double z) {
        Box bar = new Box(width, height, depth);
        bar.setMaterial(outlineMaterial);
        bar.setMouseTransparent(true);
        bar.setTranslateX(x);
        bar.setTranslateY(y);
        bar.setTranslateZ(z);
        return bar;
    }
    public void showSelection(Integer selected) {
        for (VoxelNode voxel : voxelNodes) {
            boolean isVisible = selected == null || voxel.brickId == selected;
            voxel.cube.setVisible(isVisible);
            voxel.cube.setMaterial(styles.getBrickStyle(voxel.brickId).material());
            voxel.setOutlineMaterial(isVisible ? outlineMaterial : dimmedEdgeMaterial);
        }
    }

    public Integer findBrickId(Node n) {
        for (Node node = n; node != null; node = node.getParent()) {
            if (node.getUserData() instanceof Integer brickId) {
                return brickId;
            }
        }
        return null;
    }

    public void rotate(double deltaY, double deltaX) {
        rotY.setAngle(rotY.getAngle() + deltaY);
        rotX.setAngle(Math.clamp(rotX.getAngle() + deltaX, -89, 89));
    }

    public void zoom(double delta) {
        camera.setTranslateZ(Math.clamp(camera.getTranslateZ() + delta, -40, -5));
    }

    public void resetCamera() {
        rotX.setAngle(DEFAULT_ROTATION_X);
        rotY.setAngle(DEFAULT_ROTATION_Y);
        camera.setTranslateZ(DEFAULT_ZOOM);
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public void setReset(Runnable handler) {
        resetButton.setOnAction(e -> handler.run());
    }
}