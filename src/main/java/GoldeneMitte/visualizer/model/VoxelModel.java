package GoldeneMitte.visualizer.model;

import GoldeneMitte.core.Box;
import GoldeneMitte.core.Brick;
import GoldeneMitte.core.Position;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;

public class VoxelModel {
    public static final int GOLD_BRICK_ID = -1;

    private final int gridSize;
    private final Map<Integer, List<Position>> cellsByBrickId = new LinkedHashMap<>();
    private final ObjectProperty<Integer> selectedBricks = new SimpleObjectProperty<>();

    public VoxelModel(Box box) {
        gridSize = box.getSize();
        Brick[][][] grid = box.getGrid();
        for (int z = 0; z < gridSize; z++)
            for (int y = 0; y < gridSize; y++)
                for (int x = 0; x < gridSize; x++) {
                    Brick brick = grid[x][y][z];
                    if (brick != null) cellsByBrickId.computeIfAbsent(brick.id(), k -> new ArrayList<>()).add(new Position(x, y, z));
                }
    }

    public int getGridSize() {
        return gridSize;
    }

    public List<Integer> getBrickIds() {
        List<Integer> ids = new ArrayList<>(cellsByBrickId.keySet());
        ids.sort(Comparator.comparingInt(id -> id == GOLD_BRICK_ID ? Integer.MAX_VALUE : id));
        return ids;
    }

    public List<Position> getCells(int brickId) {
        return Collections.unmodifiableList(cellsByBrickId.getOrDefault(brickId, List.of()));
    }

    public ObjectProperty<Integer> selectedProperty() {
        return selectedBricks;
    }

    public Integer getSelected() {
        return selectedBricks.get();
    }

    public void toggleSelection(int brickId) {
        selectedBricks.set(Objects.equals(selectedBricks.get(), brickId) ? null : brickId);
    }

    public void clearSelection() {
        selectedBricks.set(null);
    }
}
