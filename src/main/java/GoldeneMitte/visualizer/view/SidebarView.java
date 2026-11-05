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

    private static final String ROW_STYLE = "-fx-background-color: rgba(15,23,42,0.5); -fx-border-color: #334155;"
            + "-fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;";
    private static final String ROW_STYLE_SELECTED =
            "-fx-background-color: rgba(59,130,246,0.25); -fx-border-color: #3b82f6;"
                    + "-fx-border-radius: 4; -fx-background-radius: 4; -fx-cursor: hand;";

    private final Map<Integer, HBox> rows = new HashMap<>();
    private IntConsumer onBrickClicked = id -> {};

    public SidebarView(VoxelModel model, BrickStyles styles) {
        super(10);

        Label header = new Label("Bausteine (" + model.getBrickIds().size() + ")");
        header.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #f1f5f9;");

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
        setStyle("-fx-background-color: #0f172a; -fx-border-color: #1e293b; -fx-border-width: 0 1 0 0;");
    }

    public void setOnBrickClicked(IntConsumer handler) {
        this.onBrickClicked = handler;
    }

    public void showSelection(Integer selected) {
        rows.forEach((id, row) -> row.setStyle(id.equals(selected) ? ROW_STYLE_SELECTED : ROW_STYLE));
    }

    private HBox createRow(int brickId, Color color, int cellCount) {
        Circle dot = new Circle(8, color);
        dot.setStroke(Color.web("#ffffff", 0.4));

        Label id = new Label("ID: " + (brickId == VoxelModel.GOLD_BRICK_ID ? "G" : brickId));
        id.setStyle("-fx-font-family: monospace; -fx-font-weight: bold; -fx-text-fill: #e2e8f0;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label cnt = new Label(cellCount + " Voxel");
        cnt.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");

        HBox row = new HBox(8, dot, id, spacer, cnt);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(6, 8, 6, 8));
        row.setStyle(ROW_STYLE);
        row.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) onBrickClicked.accept(brickId);
        });
        return row;
    }
}
