package GoldeneMitte.visualizer.controller;

import GoldeneMitte.visualizer.model.VoxelModel;
import GoldeneMitte.visualizer.view.BrickDescriptor;
import GoldeneMitte.visualizer.view.CellCoordinate;

import java.util.List;

public class ViewDataMapper {
    public static List<BrickDescriptor> toBrickDescriptors(VoxelModel model) {
        return model.getBrickIds().stream().map(id -> new BrickDescriptor(id,
                        id == VoxelModel.GOLD_BRICK_ID, model.getCells(id).stream()
                        .map(p -> new CellCoordinate(p.x(), p.y(), p.z()))
                        .toList())).toList();
    }
}
