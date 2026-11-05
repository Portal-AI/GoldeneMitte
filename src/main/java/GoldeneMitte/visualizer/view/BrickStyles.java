package GoldeneMitte.visualizer.view;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.HashMap;
import java.util.Map;

public class BrickStyles {

    private static final Color GOLD_COLOR = Color.web("#D4AF37");
    private static final double HUE_STEP_PER_ID = 137;

    private final Map<Integer, BrickStyle> stylesByBrickId = new HashMap<>();

    public BrickStyle getBrickStyle(int brickId) {
        return stylesByBrickId.computeIfAbsent(brickId, BrickStyles::createStyle);
    }

    private static BrickStyle createStyle(int brickId) {
        Color color = getColor(brickId);
        return new BrickStyle(color, createMaterial(color));
    }

    static Color getColor(int brickId) {
        if (brickId == -1) return GOLD_COLOR;
        double hue = Math.abs(brickId * HUE_STEP_PER_ID) % 360;
        return Color.hsb(hue, 0.85, 1.0);
    }

    private static PhongMaterial createMaterial(Color color) {
        PhongMaterial material = new PhongMaterial(color);
        material.setSpecularColor(null);
        return material;
    }
}