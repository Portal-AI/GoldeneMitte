package GoldeneMitte.visualizer.view;

import GoldeneMitte.visualizer.model.VoxelModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntConsumer;

public class SidebarView extends VBox {
    private final Map<Integer, HBox> rows = new HashMap<>();
    private IntConsumer onBrickClicked = id -> {};

    public SidebarView(VoxelModel model, BrickStyles styles) {
        super(10);

        Label header = new Label("Bausteine (" + model.getBrickIds().size() + ")");
        header.setStyle(Theme.SIDEBAR_HEADER_STYLE);

        VBox list = new VBox(4);
        list.setPadding(new Insets(2));
        for (int id : model.getBrickIds()) {
            HBox row = createRow(id, BrickStyles.getColor(id), model.getCells(id).size());
            rows.put(id, row);
            list.getChildren().add(row);
        }

        ScrollPane sp = new ScrollPane(list);
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);

        getChildren().addAll(header, sp);
        setPadding(new Insets(12));
        setPrefWidth(240);
        setMinWidth(240);
        setStyle(Theme.SIDEBAR_STYLE);
    }

    public void setOnBrickClicked(IntConsumer handler) {
        this.onBrickClicked = handler;
    }

    public void showSelection(Integer selected) {
        rows.forEach((id, row) -> row.setStyle(id.equals(selected) ? Theme.ROW_STYLE_SELECTED : Theme.ROW_STYLE));
    }

    private HBox createRow(int brickId, Color color, int cellCount) {
        Circle dot = new Circle(8, color);
        dot.setStroke(Theme.DOT_STROKE_COLOR);

        Label id = new Label("ID: " + (brickId == VoxelModel.GOLD_BRICK_ID ? "G" : brickId));
        id.setStyle(Theme.ID_LABEL_STYLE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label labelCellCount = new Label(cellCount + " Voxel");
        labelCellCount.setStyle(Theme.COUNT_LABEL_STYLE);

        HBox row = new HBox(8, dot, id, spacer, labelCellCount);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 8, 6, 8));
        row.setStyle(Theme.ROW_STYLE);
        row.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) onBrickClicked.accept(brickId);
        });
        return row;
    }
}
