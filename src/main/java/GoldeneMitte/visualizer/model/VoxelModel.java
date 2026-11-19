package GoldeneMitte.visualizer.model;

import GoldeneMitte.core.Box;
import GoldeneMitte.core.Brick;
import GoldeneMitte.core.Position;

import java.util.*;
import java.util.function.Consumer;

public class VoxelModel {
    public static final int GOLD_BRICK_ID = -1;

    private final int gridSize;
    private final Map<Integer, List<Position>> cellsByBrickId = new LinkedHashMap<>();
    private final List<Consumer<Integer>> selectionListeners = new ArrayList<>();
    private Integer selectedBrickId;

    public VoxelModel(Box box) {
        gridSize = box.getSize();
        Brick[][][] grid = box.getGrid();
        for (int z = 0; z < gridSize; z++)
            for (int y = 0; y < gridSize; y++)
                for (int x = 0; x < gridSize; x++) {
                    Brick brick = grid[x][y][z];
                    if (brick != null) cellsByBrickId.computeIfAbsent(brick.id(), id -> new ArrayList<>()).add(new Position(x, y, z));
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

    public Integer getSelected() {
        return selectedBrickId;
    }

    public void toggleSelection(int brickId) {
        setSelected(Objects.equals(selectedBrickId, brickId) ? null : brickId);
    }

    public void addSelectionListener(Consumer<Integer> listener) {
        selectionListeners.add(Objects.requireNonNull(listener));
    }

    private void setSelected(Integer newSelection) {
        if (Objects.equals(selectedBrickId, newSelection)) return;
        selectedBrickId = newSelection;
        for (Consumer<Integer> listener : List.copyOf(selectionListeners)) {
            listener.accept(newSelection);
        }
    }

    public void clearSelection() {
        setSelected(null);
    }
}
