package GoldeneMitte.visualizer.view;

import java.util.List;

public record BrickDescriptor(int id, boolean gold, List<CellCoordinate> cells) {}